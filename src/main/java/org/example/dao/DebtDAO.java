package org.example.dao;

import org.example.cache.UserCache;
import org.example.dto.UserDTO;
import org.example.model.Debt;
import org.example.model.Expense;
import org.example.model.Response;
import org.example.model.User;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class DebtDAO extends BaseDAO<Debt> {
    private final String tableName = "debts";
    private static final DebtDAO instance = new DebtDAO();

    public static DebtDAO getInstance() {
        return instance;
    }

    private DebtDAO() {
        super();
    }

    @Override
    public Response<Debt> create(Debt entity) {
        String sql = "INSERT INTO " + tableName + " (amount, due_date, debtor_id, creditor_id, expense_id, installment_number) VALUES (?, ?, ?, ?, ?, ?)";

        try {
            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setDouble(1, entity.getAmount());
            ps.setDate(2, Date.valueOf(entity.getDueDate()));
            ps.setInt(3, entity.getDebtor().getId());
            ps.setInt(4, entity.getCreditor().getId());
            ps.setInt(5, entity.getExpenseId());
            ps.setInt(6, entity.getInstallmentNumber());


            ps.executeUpdate();
            return new Response<>(true, "200", "Deuda registrada exitosamente");

        } catch (SQLException e) {
            return new Response<>(false, "500", e.getMessage());
        }
    }

    @Override
    public Response<Debt> read(int id) {
        String sql = "SELECT * FROM " + tableName + " WHERE id = ?";

        try {
            PreparedStatement ps = conn.prepareStatement(sql);
            //El 1 significa que le pasa al primer "placeholder"->? el id
            ps.setInt(1, id);
            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                UserDAO userDAO = UserDAO.getInstance();
                ExpenseDAO expenseDAO = ExpenseDAO.getInstance();

                Response<User> debtorResponse = userDAO.read(rs.getInt("debtor_id"));
                Response<User> creditorResponse = userDAO.read(rs.getInt("creditor_id"));
                Response<Expense> expenseResponse = expenseDAO.read(rs.getInt("expense_id"));

                if (!debtorResponse.isSuccess() || !creditorResponse.isSuccess() || !expenseResponse.isSuccess()) {
                    return new Response<>(false, "404", "Error al obtener los datos relacionados de la deuda");
                }

                Debt debt = new Debt(
                        rs.getInt("id"),
                        rs.getDouble("amount"),
                        debtorResponse.getObj(),
                        creditorResponse.getObj(),
                        rs.getInt("expense_id"),
                        rs.getDate("due_date").toLocalDate(),
                        rs.getInt("installment_number")

                );

                return new Response<>(true, "200", "Deuda encontrada", debt);
            } else {
                return new Response<>(false, "404", "No se encontró la deuda");
            }

        } catch (SQLException e) {
            return new Response<>(false, "500", e.getMessage());
        }
    }

    @Override
    public Response<Debt> update(Debt entity) {
        String sql = "UPDATE " + tableName + " SET amount = ?, due_date = ?, debtor_id = ?, creditor_id = ?, expense_id = ?, installment_number = ? WHERE id = ?";

        try {
            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setDouble(1, entity.getAmount());
            ps.setDate(2, Date.valueOf(entity.getDueDate()));
            ps.setInt(3, entity.getDebtor().getId());
            ps.setInt(4, entity.getCreditor().getId());
            ps.setInt(5, entity.getExpenseId());
            ps.setInt(6, entity.getInstallmentNumber());
            ps.setInt(7, entity.getId());


            int rows = ps.executeUpdate();

            if (rows == 1) {
                return new Response<>(true, "200", "Deuda actualizada correctamente");
            } else if (rows == 0) {
                return new Response<>(false, "404", "No se encontró la deuda");
            } else {
                return new Response<>(false, "500", "Error: se afectaron múltiples registros");
            }

        } catch (SQLException e) {
            return new Response<>(false, "500", e.getMessage());
        }
    }

    @Override
    public Response<Debt> delete(int id) {
        String sql = "DELETE FROM " + tableName + " WHERE id = ?";

        try {
            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setInt(1, id);
            int rows = ps.executeUpdate();

            if (rows == 1) {
                return new Response<>(true, "200", "Deuda eliminada correctamente");
            } else {
                //Como id es unique en la tabla no puede encontrar más de uno. Lo encuentra o no.
                return new Response<>(false, "404", "No se encontró la deuda");
            }

        } catch (SQLException e) {
            return new Response<>(false, "500", e.getMessage());
        }
    }

    @Override
    public Response<Debt> readAll() {
        String sql = "SELECT * FROM debts ORDER BY due_date DESC";
        List<Debt> lista = new ArrayList<>();
        UserDAO userDAO = UserDAO.getInstance();

        try {
            Statement stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery(sql);

            while (rs.next()) {
                int debtorId = rs.getInt("debtor_id");
                int creditorId = rs.getInt("creditor_id");

                Response<User> debtorResponse = userDAO.read(debtorId);
                Response<User> creditorResponse = userDAO.read(creditorId);

                if (!debtorResponse.isSuccess() || !creditorResponse.isSuccess()) {
                    System.out.println("Deuda con datos incompletos (ID: " + rs.getInt("id") + ")");
                    System.out.println("Error en 'debtor': " + debtorResponse.getMessage());
                    System.out.println("Error en 'creditor': " + creditorResponse.getMessage());
                    continue; // Saleta la creación de la deuda que le falten datos
                }

                Debt d = new Debt(
                        rs.getInt("id"),
                        rs.getDouble("amount"),
                        debtorResponse.getObj(),
                        creditorResponse.getObj(),
                        rs.getInt("expense_id"),
                        rs.getDate("due_date").toLocalDate(),
                        rs.getInt("installment_number")
                );

                lista.add(d);
            }

            return new Response<>(true, "200", "Listado de deudas obtenido", lista);

        } catch (SQLException e) {
            return new Response<>(false, "500", e.getMessage());
        }
    }



    public Response<Debt> readAllByUser(UserDTO user) {
        String sql = "SELECT * FROM debts WHERE creditor_id = ? OR debtor_id = ? ORDER BY due_date DESC";
        List<Debt> lista = new ArrayList<>();
        UserDAO userDAO = UserDAO.getInstance();

        try {
            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setInt(1, user.getId());
            ps.setInt(2, user.getId());
            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                int debtorId = rs.getInt("debtor_id");
                int creditorId = rs.getInt("creditor_id");

                Response<User> debtorResponse = userDAO.read(debtorId);
                Response<User> creditorResponse = userDAO.read(creditorId);

                if (!debtorResponse.isSuccess() || !creditorResponse.isSuccess()) {
                    System.out.println("Deuda con datos incompletos (ID: " + rs.getInt("id") + ")");
                    System.out.println("Error en 'debtor': " + debtorResponse.getMessage());
                    System.out.println("Error en 'creditor': " + creditorResponse.getMessage());
                    continue; // Saleta la creación de la deuda que le falten datos
                }

                Debt d = new Debt(
                        rs.getInt("id"),
                        rs.getDouble("amount"),
                        creditorResponse.getObj(),
                        debtorResponse.getObj(),
                        rs.getInt("expense_id"),
                        rs.getDate("due_date").toLocalDate(),
                        rs.getInt("installment_number")
                );
                lista.add(d);
            }

            return new Response<>(true, "200", "Listado de deudas obtenido", lista);

        } catch (SQLException e) {
            return new Response<>(false, "500", e.getMessage());
        }
    }

    public Response<Debt> readAllByExpenseAndUser(int expenseId, UserDTO user) {
        String sql = "SELECT * FROM debts WHERE expense_id = ? AND (creditor_id = ? OR debtor_id = ?) ORDER BY due_date DESC";// Consulta para obtener deudas de un gasto específico y del usuario logueado(como deudor o acreedor)
        List<Debt> lista = new ArrayList<>();// Armo un arrayList para guardar las deudas del gasto y del usuario logueado.
        UserDAO userDAO = UserDAO.getInstance();// Obtengo el DAO de usuarios para poder obtener los datos de los usuarios deudores y acreedores.

        try {
            PreparedStatement ps = conn.prepareStatement(sql);// Preparo la consulta
            ps.setInt(1, expenseId);
            ps.setInt(2, user.getId());
            ps.setInt(3, user.getId());
            ResultSet rs = ps.executeQuery();// Ejecuta la consulta

            UserCache userCache = UserCache.getInstance();//Armo un Cache de usuarios para no tener que hacer una consulta todas las deudas

            while (rs.next()) {
                int debtorId = rs.getInt("debtor_id");// Obtiene el ID del deudor
                int creditorId = rs.getInt("creditor_id");// Obtiene el ID del acreedor
                //Busco el usuario deudor y acreedor en el cache
                Response<User> debtorResponse = userCache.getFalseUserById(debtorId);
                Response<User> creditorResponse = userCache.getFalseUserById(creditorId);

                if (!debtorResponse.isSuccess() || !creditorResponse.isSuccess()) {
                    System.out.println("Deuda con datos incompletos (ID: " + rs.getInt("id") + ")");
                    System.out.println("Error en 'debtor': " + debtorResponse.getMessage());
                    System.out.println("Error en 'creditor': " + creditorResponse.getMessage());
                    continue; // Saleta la creación de la deuda que le falten datos
                }

                Debt d = new Debt(
                        rs.getInt("id"),
                        rs.getDouble("amount"),
                        debtorResponse.getObj(),
                        creditorResponse.getObj(),
                        rs.getInt("expense_id"),
                        rs.getDate("due_date").toLocalDate(),
                        rs.getInt("installment_number")
                );

                lista.add(d);
            }

            return new Response<>(true, "200", "Listado de deudas obtenido", lista);

        } catch (SQLException e) {
            return new Response<>(false, "500", e.getMessage());
        }
    }
    public Response deleteByExpenseId(int id) {
        String sql = "DELETE FROM " + tableName + " WHERE expense_id = ?";

        try {
            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setInt(1, id);
            int rows = ps.executeUpdate();

            if (rows > 0) {
                return new Response<>(true, "200", "Deuda eliminada correctamente");
            } else {
                //Como id es unique en la tabla no puede encontrar más de uno. Lo encuentra o no.
                return new Response<>(false, "404", "No se encontró la deuda");
            }
        } catch (SQLException e) {
            return new Response<>(false, "500", e.getMessage());
        }
    }
}
