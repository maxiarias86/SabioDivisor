package org.example.dao;

import org.example.dto.UserDTO;
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
        String sql = "INSERT INTO " + tableName + " (payer_id, recipient_id, amount, date) VALUES (?, ?, ?, ?)";

        try {
            PreparedStatement ps = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);//Devuelve el ID generado para que la respuesta contenga el objeto Payment con su ID asignado.
            ps.setInt(1, entity.getPayer().getId());
            ps.setInt(2, entity.getRecipient().getId());
            ps.setDouble(3, entity.getAmount());
            ps.setDate(4, Date.valueOf(entity.getDate()));
            int rows = ps.executeUpdate();

            if (rows == 1) {
                ResultSet keys = ps.getGeneratedKeys();//Obtiene el ID generado por el insert.
                if (keys.next()) {//Verifica si hay un ID generado.
                    int generatedId = keys.getInt(1);
                    entity.setId(generatedId);
                    return new Response<>(true, "201", "Pago registrado exitosamente.", entity);//Devuelve la respuesta con el objeto Payment que contiene el ID generado.
                } else {
                    return new Response<>(false, "500", "No se pudo obtener el ID del pago registrado.");
                }
            } else {
                return new Response<>(false, "500", "No se pudo registrar el pago.");
            }
        } catch (SQLException e) {
            return new Response<>(false, "500", e.getMessage());
        }
    }

    @Override
    public Response<Payment> read(int id) {
        String sql = "SELECT * FROM " + tableName + " WHERE id = ?";// Query para obtener un pago por su ID.
        try {
            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setInt(1, id);
            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                UserDAO userDAO = UserDAO.getInstance();// Llama a UserDAO para poder obtener los usuarios asociados al pago.
                int payerId = rs.getInt("payer_id");
                int recipientId = rs.getInt("recipient_id");

                Response<User> payerResponse = userDAO.read(payerId);// El UserDAO obtiene el usuario pagador.
                Response<User> recipientResponse = userDAO.read(recipientId);// El UserDAO obtiene el usuario receptor.

                if (!payerResponse.isSuccess() || !recipientResponse.isSuccess()) {// Verifica que ambos usuarios fueron obtenidos.
                    System.out.println("Error al obtener usuarios para el pago con ID: " + id);
                    System.out.println("Error en 'payer': " + payerResponse.getMessage());
                    System.out.println("Error en 'recipient': " + recipientResponse.getMessage());
                    return new Response<>(false, "404", "No se pudieron obtener los usuarios asociados");
                }

                User payer = payerResponse.getObj();// Obtiene el objeto User del pagador.
                User recipient = recipientResponse.getObj();// Obtiene el objeto User del receptor.

                Payment payment = new Payment(// Crea un nuevo objeto Payment con los datos obtenidos.
                        rs.getInt("id"),
                        rs.getDouble("amount"),
                        rs.getDate("date").toLocalDate(),
                        payer,
                        recipient);

                return new Response<>(true, "200", "Pago encontrado", payment);// Devuelve la respuesta con el objeto Payment encontrado.
            } else {
                return new Response<>(false, "404", "No se encontró el pago");
            }
        } catch (SQLException e) {
            return new Response<>(false, "500", e.getMessage());
        }
    }

    @Override
    public Response<Payment> update(Payment entity) {
        String sql = "UPDATE " + tableName + " SET payer_id = ?, recipient_id = ?, amount = ?, date = ? WHERE id = ?";

        try {
            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setInt(1, entity.getPayer().getId());
            ps.setInt(2, entity.getRecipient().getId());
            ps.setDouble(3, entity.getAmount());
            ps.setDate(4, Date.valueOf(entity.getDate()));
            ps.setInt(5, entity.getId());
            int rows = ps.executeUpdate();

            if (rows == 1) {// Verifica si se actualizó un solo registro.
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
        List<Payment> lista = new ArrayList<>();//Armo un arrayList para guardar los pagos.
        try {
            Statement stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery(sql);

            while (rs.next()) {//Recorre los resultados
                int payerId = rs.getInt("payer_id");//Obtiene el ID del pagador
                int recipientId = rs.getInt("recipient_id");//Obtiene el ID del receptor

                Response<User> payerResponse = UserDAO.getInstance().read(payerId);//Llama a UserDAO para obtener el usuario pagador
                Response<User> recipientResponse = UserDAO.getInstance().read(recipientId);//Llama a UserDAO para obtener el usuario receptor

                if (!payerResponse.isSuccess() || !recipientResponse.isSuccess()) {//Verifica que ambos usuarios fueron obtenidos
                    System.out.println("Pago con datos incompletos (ID: " + rs.getInt("id") + ")");
                    System.out.println("Error en 'payer': " + payerResponse.getMessage());
                    System.out.println("Error en 'recipient': " + recipientResponse.getMessage());
                    continue; //Saltea el registro si hay un error. Evita guardarlo
                }

                User payer = payerResponse.getObj();//Obtiene el objeto User del pagador
                User recipient = recipientResponse.getObj();//Obtiene el objeto User del receptor

                Payment payment = new Payment(//Crea un nuevo objeto Payment con los datos obtenidos
                        rs.getInt("id"),
                        rs.getDouble("amount"),
                        rs.getDate("date").toLocalDate(),
                        payer,
                        recipient                );

                lista.add(payment);// Agrega el pago a la lista
            }
            return new Response<>(true, "200", "Listado de pagos obtenido", lista);//Devuelve la respuesta con la lista de pagos obtenidos.
        } catch (SQLException e) {
            return new Response<>(false, "500", e.getMessage());
        }
    }

    public Response<Payment> readAllByUser(UserDTO user) {//Se usa para crear el PaymentCache del usuario logueado
        String sql = "SELECT * FROM " + tableName + " WHERE payer_id = ? OR recipient_id = ? ORDER BY date DESC";// Query para obtener todos los pagos en los que participa el usuario logueado, ya sea como pagador o receptor. Se ordena en sentido descendente por fecha para mostrarlo así en la vista.
        List<Payment> lista = new ArrayList<>();//ArrayList para guardar los pagos en los que participa el usuario logueado.

        try {
            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setInt(1, user.getId());
            ps.setInt(2, user.getId());
            ResultSet rs = ps.executeQuery();//Ejecuta la consulta

            while (rs.next()) {//Recorre los resultados de a uno.
                int payerId = rs.getInt("payer_id");//Obtiene el ID del pagador
                int recipientId = rs.getInt("recipient_id");//Obtiene el ID del receptor

                Response<User> payerResponse = UserDAO.getInstance().read(payerId);//Obtiene el usuario pagador
                Response<User> recipientResponse = UserDAO.getInstance().read(recipientId);//Obtiene el usuario receptor

                if (!payerResponse.isSuccess() || !recipientResponse.isSuccess()) {
                    System.out.println("Pago con datos incompletos (ID: " + rs.getInt("id") + ")");
                    System.out.println("Error en 'payer': " + payerResponse.getMessage());
                    System.out.println("Error en 'recipient': " + recipientResponse.getMessage());
                    continue; //Saltea el registro si hay un error. Evita guardarlo
                }

                User payer = payerResponse.getObj();//Obtiene el objeto User del pagador
                User recipient = recipientResponse.getObj();//Obtiene el objeto User del receptor

                Payment payment = new Payment(//Crea un nuevo objeto Payment con los datos obtenidos
                        rs.getInt("id"),// Obtiene el ID del pago
                        rs.getDouble("amount"),// Obtiene el monto del pago
                        rs.getDate("date").toLocalDate(),//
                        payer,// Asigna el pagador
                        recipient);// Asigna el receptor

                lista.add(payment);// Agrega el pago a la lista
            }
            return new Response<>(true, "200", "Listado de pagos obtenido", lista);// Devuelve la respuesta con la lista de pagos obtenidos.
        } catch (SQLException e) {
            return new Response<>(false, "500", e.getMessage());
        }
    }
}