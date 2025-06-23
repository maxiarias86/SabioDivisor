package org.example.dao;

import org.example.dto.ExpenseDTO;
import org.example.model.Debt;
import org.example.model.Expense;
import org.example.model.Response;
import org.example.model.User;
import org.example.repository.ExpenseRepository;

import java.sql.*;
import java.util.List;
import java.util.Map;

public class ExpenseDAO extends BaseDAO<Expense> {
    private final String tableName = "expenses";
    private static final ExpenseDAO instance = new ExpenseDAO();

    public static ExpenseDAO getInstance() {
        return instance;
    }

    private ExpenseDAO() {
        super();
    }

    @Override
    public Response<Expense> create(Expense entity) {
        String sql = "INSERT INTO " + tableName + " (amount, date, installments, description) VALUES (?, ?, ?, ?)";

        try {
            PreparedStatement ps = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
            ps.setDouble(1, entity.getAmount());
            ps.setDate(2, Date.valueOf(entity.getDate()));
            ps.setInt(3, entity.getInstallments());
            ps.setString(4, entity.getDescription());

            int rows = ps.executeUpdate();

            if (rows == 0) {
                return new Response<>(false, "500", "No se pudo crear el gasto");
            }

            ResultSet generatedKeys = ps.getGeneratedKeys();
            if (generatedKeys.next()) {
                int expenseId = generatedKeys.getInt(1);
                entity.setId(expenseId);
            } else {
                return new Response<>(false, "500", "No se obtuvo el ID del gasto insertado");
            }

            // Insertar payers
            if (entity.getPayers() != null && !entity.getPayers().isEmpty()) {
                String payerSQL = "INSERT INTO expense_payers (expense_id, user_id, amount) VALUES (?, ?, ?)";
                PreparedStatement payerPS = conn.prepareStatement(payerSQL);
                for (Map.Entry<User, Double> entry : entity.getPayers().entrySet()) {
                    payerPS.setInt(1, entity.getId());
                    payerPS.setInt(2, entry.getKey().getId());
                    payerPS.setDouble(3, entry.getValue());
                    payerPS.addBatch();
                }
                payerPS.executeBatch();
            }

            // Insertar debtors
            if (entity.getDebtors() != null && !entity.getDebtors().isEmpty()) {
                String debtorSQL = "INSERT INTO expense_debtors (expense_id, user_id, amount) VALUES (?, ?, ?)";
                PreparedStatement debtorPS = conn.prepareStatement(debtorSQL);
                for (Map.Entry<User, Double> entry : entity.getDebtors().entrySet()) {
                    debtorPS.setInt(1, entity.getId());
                    debtorPS.setInt(2, entry.getKey().getId());
                    debtorPS.setDouble(3, entry.getValue());
                    debtorPS.addBatch();
                }
                debtorPS.executeBatch();
            }

            return new Response<>(true, "200", "Gasto creado con éxito", entity);

        } catch (SQLException e) {
            return new Response<>(false, "500", e.getMessage());
        }
    }


    @Override
    public Response<Expense> read(int id) {
        String sql = "SELECT * FROM " + tableName + " WHERE id = ?";

        try {
            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setInt(1, id);
            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                UserDAO userDAO = UserDAO.getInstance();
                DebtDAO debtDAO = DebtDAO.getInstance();

                // Crear el gasto sin usuarios ni deudas
                Expense expense = new Expense(
                        rs.getInt("id"),
                        rs.getDouble("amount"),
                        rs.getDate("date").toLocalDate(),
                        rs.getInt("installments"),
                        null, null, null,
                        rs.getString("description")
                );

                // Cargar payers
                PreparedStatement payersPS = conn.prepareStatement("SELECT * FROM expense_payers WHERE expense_id = ?");
                payersPS.setInt(1, expense.getId());
                ResultSet payersRS = payersPS.executeQuery();
                Map<User, Double> payersMap = new java.util.HashMap<>();
                while (payersRS.next()) {
                    int userId = payersRS.getInt("user_id");
                    double amount = payersRS.getDouble("amount");
                    Response<User> userResp = userDAO.read(userId);
                    if (userResp.isSuccess()) {
                        payersMap.put(userResp.getObj(), amount);
                    }
                }
                expense.setPayers(payersMap);

                // Cargar debtors
                PreparedStatement debtorsPS = conn.prepareStatement("SELECT * FROM expense_debtors WHERE expense_id = ?");
                debtorsPS.setInt(1, expense.getId());
                ResultSet debtorsRS = debtorsPS.executeQuery();
                Map<User, Double> debtorsMap = new java.util.HashMap<>();
                while (debtorsRS.next()) {
                    int userId = debtorsRS.getInt("user_id");
                    double amount = debtorsRS.getDouble("amount");
                    Response<User> userResp = userDAO.read(userId);
                    if (userResp.isSuccess()) {
                        debtorsMap.put(userResp.getObj(), amount);
                    }
                }
                expense.setDebtors(debtorsMap);

                // Cargar deudas asociadas
                Response<Debt> allDebts = debtDAO.readAll();
                if (allDebts.isSuccess()) {
                    List<Debt> relatedDebts = allDebts.getData().stream()
                            .filter(d -> d.getExpenseId() == expense.getId())
                            .toList();
                    expense.setDebts(relatedDebts);
                }

                return new Response<>(true, "200", "Gasto encontrado", expense);
            } else {
                return new Response<>(false, "404", "No se encontró el gasto");
            }

        } catch (SQLException e) {
            return new Response<>(false, "500", e.getMessage());
        }
    }


    @Override
    public Response<Expense> update(Expense entity) {
        String sql = "UPDATE " + tableName + " SET amount = ?, date = ?, installments = ?, description = ? WHERE id = ?";

        try {
            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setDouble(1, entity.getAmount());
            ps.setDate(2, Date.valueOf(entity.getDate()));
            ps.setInt(3, entity.getInstallments());
            ps.setString(4, entity.getDescription());
            ps.setInt(5, entity.getId());

            int rows = ps.executeUpdate();

            if (rows == 0) {
                return new Response<>(false, "404", "No se encontró el gasto para actualizar");
            }

            // Eliminar registros anteriores en expense_payers y expense_debtors
            PreparedStatement delPayers = conn.prepareStatement("DELETE FROM expense_payers WHERE expense_id = ?");
            delPayers.setInt(1, entity.getId());
            delPayers.executeUpdate();

            PreparedStatement delDebtors = conn.prepareStatement("DELETE FROM expense_debtors WHERE expense_id = ?");
            delDebtors.setInt(1, entity.getId());
            delDebtors.executeUpdate();

            // Insertar nuevos payers
            if (entity.getPayers() != null && !entity.getPayers().isEmpty()) {
                String payerSQL = "INSERT INTO expense_payers (expense_id, user_id, amount) VALUES (?, ?, ?)";
                PreparedStatement payerPS = conn.prepareStatement(payerSQL);
                for (Map.Entry<User, Double> entry : entity.getPayers().entrySet()) {
                    payerPS.setInt(1, entity.getId());
                    payerPS.setInt(2, entry.getKey().getId());
                    payerPS.setDouble(3, entry.getValue());
                    payerPS.addBatch();
                }
                payerPS.executeBatch();
            }

            // Insertar nuevos debtors
            if (entity.getDebtors() != null && !entity.getDebtors().isEmpty()) {
                String debtorSQL = "INSERT INTO expense_debtors (expense_id, user_id, amount) VALUES (?, ?, ?)";
                PreparedStatement debtorPS = conn.prepareStatement(debtorSQL);
                for (Map.Entry<User, Double> entry : entity.getDebtors().entrySet()) {
                    debtorPS.setInt(1, entity.getId());
                    debtorPS.setInt(2, entry.getKey().getId());
                    debtorPS.setDouble(3, entry.getValue());
                    debtorPS.addBatch();
                }
                debtorPS.executeBatch();
            }

            return new Response<>(true, "200", "Gasto actualizado con éxito", entity);

        } catch (SQLException e) {
            return new Response<>(false, "500", e.getMessage());
        }
    }


    @Override
    public Response<Expense> delete(int id) {
        String sql = "DELETE FROM " + tableName + " WHERE id = ?";

        try {
            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setInt(1, id);
            int rows = ps.executeUpdate();

            if (rows == 1) {
                return new Response<>(true, "200", "Gasto eliminado correctamente");
            } else {
                return new Response<>(false, "404", "No se encontró el gasto");
            }

        } catch (SQLException e) {
            return new Response<>(false, "500", e.getMessage());
        }
    }

    @Override
    public Response<Expense> readAll() {
        String sql = "SELECT * FROM " + tableName;

        try {
            Statement stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery(sql);

            ExpenseRepository repository = new ExpenseRepository(conn);
            List<Expense> expenses = repository.loadAllExpenses(rs);

            return new Response<>(true, "200", "Listado de gastos obtenido", expenses);

        } catch (SQLException e) {
            return new Response<>(false, "500", e.getMessage());
        }
    }

    public Response<Integer> save(ExpenseDTO dto, List<Debt> debts) {
        //En este metodo cuando inserte el Expense tengo que copiarme el id, y luego insertar las deudas con ese expenseId
        String insertExpenseSQL = "INSERT INTO expenses (amount, date, installments, description) VALUES (?, ?, ?, ?)";
        String insertDebtSQL = "INSERT INTO debts (amount, due_date, debtor_id, creditor_id, expense_id, installment_number) VALUES (?, ?, ?, ?, ?, ?)";

        try {
            conn.setAutoCommit(false);//Hace que no se haga un commir en la base de datos hasta que el codigo lo indique.

            // Insertar Expense
            PreparedStatement expenseStmt = conn.prepareStatement(insertExpenseSQL, Statement.RETURN_GENERATED_KEYS);
            expenseStmt.setDouble(1, dto.getAmount());
            expenseStmt.setDate(2, Date.valueOf(dto.getDate()));
            expenseStmt.setInt(3, dto.getInstallments());
            expenseStmt.setString(4, dto.getDescription());
            expenseStmt.executeUpdate();

            ResultSet generatedKeys = expenseStmt.getGeneratedKeys();
            if (!generatedKeys.next()) {
                conn.rollback();//Hace un rollback y no queda nada guardado.
                return new Response<>(false, "500", "No se pudo obtener el ID del gasto insertado.");
            }

            int expenseId = generatedKeys.getInt(1);//Recupera el expenseId

            // Insertar Debts
            if (debts != null && !debts.isEmpty()) {
                PreparedStatement debtStmt = conn.prepareStatement(insertDebtSQL);
                for (Debt debt : debts) {
                    debtStmt.setDouble(1, debt.getAmount());
                    debtStmt.setDate(2, Date.valueOf(debt.getDueDate()));
                    debtStmt.setInt(3, debt.getDebtor().getId());
                    debtStmt.setInt(4, debt.getCreditor().getId());
                    debtStmt.setInt(5, expenseId);//Le cambia a todas las Debts el expenseId que antes era 0
                    debtStmt.setInt(6, debt.getInstallmentNumber());
                    debtStmt.addBatch();//Agrega una linea a la query insert
                }
                debtStmt.executeBatch();//Ejecuta la query
                debtStmt.close();// Es una buena practica cerrar cada PreparedStatement
            }
            conn.commit();//Finalmente hace el commit cuando no fallo nada.
            return new Response<>(true, "201", "Gasto y deudas insertados correctamente.", expenseId);

        } catch (SQLException e) {
            try {
                conn.rollback();
            } catch (SQLException rollbackEx) {
                return new Response<>(false, "500", "Error al hacer rollback: " + rollbackEx.getMessage());
            }
            return new Response<>(false, "500", "Error al guardar el gasto y las deudas: " + e.getMessage());
        } finally {
            try {
                conn.setAutoCommit(true);//Vuelve a setear AutoCommit a true
            } catch (SQLException e) {
                return new Response<>(false, "500", e.getMessage());
            }
        }
    }













}
