package org.example.dao;

import org.example.model.Payment;
import org.example.model.Response;
import org.example.model.User;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class PaymentDAO extends BaseDAO<Payment> {
    private final String tableName = "payments";
    private static final PaymentDAO instance = new PaymentDAO();

    public static PaymentDAO getInstance() {
        return instance;
    }

    private PaymentDAO() {
        super();
    }


    @Override
    public Response<Payment> create(Payment entity) {
        String sql = "INSERT INTO " + tableName + " (payer_id, payee_id, amount, date) VALUES (?, ?, ?, ?)";

        try {
            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setInt(1, entity.getPayer().getId());
            ps.setInt(2, entity.getRecipient().getId());
            ps.setDouble(3, entity.getAmount());
            ps.setDate(4, Date.valueOf(entity.getDate()));

            ps.executeUpdate();
            return new Response<>(true, "200", "Pago registrado exitosamente");

        } catch (SQLException e) {
            return new Response<>(false, "500", e.getMessage());
        }
    }

    @Override
    public Response<Payment> read(int id) {
        String sql = "SELECT * FROM " + tableName + " WHERE id = ?";

        try {
            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setInt(1, id);
            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                UserDAO userDAO = UserDAO.getInstance();

                int payerId = rs.getInt("payer_id");
                int recipientId = rs.getInt("payee_id");

                Response<User> payerResponse = userDAO.read(payerId);
                Response<User> recipientResponse = userDAO.read(recipientId);

                if (!payerResponse.isSuccess() || !recipientResponse.isSuccess()) {
                    System.out.println("⚠️ Error al obtener usuarios para el pago con ID: " + id);
                    System.out.println("Error en 'payer': " + payerResponse.getMessage());
                    System.out.println("Error en 'recipient': " + recipientResponse.getMessage());
                    return new Response<>(false, "404", "No se pudieron obtener los usuarios asociados");
                }

                User payer = payerResponse.getObj();
                User recipient = recipientResponse.getObj();

                Payment payment = new Payment(
                        rs.getInt("id"),
                        rs.getDouble("amount"),
                        rs.getDate("date").toLocalDate(),
                        payer,
                        recipient
                );

                return new Response<>(true, "200", "Pago encontrado", payment);
            } else {
                return new Response<>(false, "404", "No se encontró el pago");
            }

        } catch (SQLException e) {
            return new Response<>(false, "500", e.getMessage());
        }
    }



    @Override
    public Response<Payment> update(Payment entity) {
        String sql = "UPDATE " + tableName + " SET payer_id = ?, payee_id = ?, amount = ?, date = ? WHERE id = ?";

        try {
            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setInt(1, entity.getPayer().getId());
            ps.setInt(2, entity.getRecipient().getId());
            ps.setDouble(3, entity.getAmount());
            ps.setDate(4, Date.valueOf(entity.getDate()));
            ps.setInt(5, entity.getId());

            int rows = ps.executeUpdate();

            if (rows == 1) {
                return new Response<>(true, "200", "Pago actualizado correctamente");
            } else if (rows == 0) {
                return new Response<>(false, "404", "No se encontró el pago");
            } else {
                return new Response<>(false, "500", "Error: se afectaron múltiples registros");
            }

        } catch (SQLException e) {
            return new Response<>(false, "500", e.getMessage());
        }
    }

    @Override
    public Response<Payment> delete(int id) {
        String sql = "DELETE FROM " + tableName + " WHERE id = ?";

        try {
            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setInt(1, id);
            int rows = ps.executeUpdate();

            if (rows == 1) {
                return new Response<>(true, "200", "Pago eliminado correctamente");
            } else {
                return new Response<>(false, "404", "No se encontró el pago");
            }

        } catch (SQLException e) {
            return new Response<>(false, "500", e.getMessage());
        }
    }

    @Override
    public Response<Payment> readAll() {
        String sql = "SELECT * FROM " + tableName;
        List<Payment> lista = new ArrayList<>();

        try {
            Statement stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery(sql);
            UserDAO userDAO = UserDAO.getInstance();

            while (rs.next()) {
                int payerId = rs.getInt("payer_id");
                int recipientId = rs.getInt("payee_id");

                Response<User> payerResponse = userDAO.read(payerId);
                Response<User> recipientResponse = userDAO.read(recipientId);

                if (!payerResponse.isSuccess() || !recipientResponse.isSuccess()) {
                    System.out.println("⚠️ Pago con datos incompletos (ID: " + rs.getInt("id") + ")");
                    System.out.println("Error en 'payer': " + payerResponse.getMessage());
                    System.out.println("Error en 'recipient': " + recipientResponse.getMessage());
                    continue;
                }

                User payer = payerResponse.getObj();
                User recipient = recipientResponse.getObj();

                Payment payment = new Payment(
                        rs.getInt("id"),
                        rs.getDouble("amount"),
                        rs.getDate("date").toLocalDate(),
                        payer,
                        recipient
                );

                lista.add(payment);
            }

            return new Response<>(true, "200", "Listado de pagos obtenido", lista);

        } catch (SQLException e) {
            return new Response<>(false, "500", e.getMessage());
        }
    }



}
