package org.example.dao;

import org.example.dto.ExpenseDTO;
import org.example.model.Debt;
import org.example.model.Expense;
import org.example.model.Response;

import java.sql.*;
import java.sql.Date;
import java.util.*;

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
                        null,
                        rs.getString("description")
                );

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
    public Response<Expense> update(Expense expenseToUpdate) {
        String sql = "UPDATE " + tableName + " SET amount = ?, date = ?, installments = ?, description = ? WHERE id = ?";// Prepara la consulta SQL para actualizar un gasto
        try {
            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setDouble(1, expenseToUpdate.getAmount());
            ps.setDate(2, Date.valueOf(expenseToUpdate.getDate()));
            ps.setInt(3, expenseToUpdate.getInstallments());
            ps.setString(4, expenseToUpdate.getDescription());
            ps.setInt(5, expenseToUpdate.getId());

            int rows = ps.executeUpdate();

            if (rows == 0) {
                return new Response<>(false, "404", "No se encontró el gasto para actualizar");
            }

            return new Response<>(true, "200", "Gasto actualizado con éxito", expenseToUpdate);

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

            List<Expense> expenses = new ArrayList<>();
            //Para no tener que hacer un readAll de deudas en cada iteración creo un cache que se elimina después de usar este metodo.
            Map<Integer, List<Debt>> debtMap = new HashMap<>();//Map para almacenar las deudas por expenseId (Clave: expenseId, Valor: Lista de deudas).
            Response<Debt> allDebts = DebtDAO.getInstance().readAll();// Obtiene todas las deudas

            if (allDebts.isSuccess()) {// Si la respuesta es exitosa, procesa las deudas
                List<Debt> allDebtList = allDebts.getData();// Obtiene la lista de deudas
                for (Debt d: allDebtList) {// Recorre la lista de deudas con un foreach
                    int expenseId = d.getExpenseId();// Obtiene el ID del gasto asociado a la deuda
                    if (!debtMap.containsKey(expenseId)) {//Si el Map no contiene el expenseId, lo agrega
                        debtMap.put(expenseId, new ArrayList<Debt>());// Crea una nueva lista de deudas para ese expenseId
                    }
                    debtMap.get(expenseId).add(d);// Agrega la deuda a la lista correspondiente en el Map
                }
            } else {
                return new Response<>(false, "500", "Error al obtener las deudas: " + allDebts.getMessage());
            }
            while (rs.next()) {
                // Crea un nuevo objeto Expense a partir del ResultSet
                // y asigna las deudas relacionadas desde el Map
                List<Debt> relatedDebts = debtMap.getOrDefault(rs.getInt("id"), new ArrayList<>());

                Expense expense = new Expense(
                        rs.getInt("id"),
                        rs.getDouble("amount"),
                        rs.getDate("date").toLocalDate(),
                        rs.getInt("installments"),
                        relatedDebts, // Asigna las deudas relacionadas
                        rs.getString("description")
                );
                // Agrega el gasto a la lista
                expenses.add(expense);
            }

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

    public Response<Expense> readAllInSet(HashSet<Integer> expensesIDs) {
        if(expensesIDs == null || expensesIDs.isEmpty()) {// Verifica si el Set de IDs está vacío o es nulo
            return new Response<>(false, "400", "El conjunto de IDs está vacío");
        }
        StringBuilder sql = new StringBuilder("SELECT * FROM " + tableName + " WHERE id IN (");// Inicia la query con un IN
        for (Integer id : expensesIDs) {// Recorre el Set de IDs y los agrega a la query
            sql.append(id).append(",");// Agrega cada ID seguido de una coma
        }
        sql.deleteCharAt(sql.length() - 1); // Elimina la última coma
        sql.append(")");// Cierra el paréntesis del IN

        try {
            Statement stmt = conn.createStatement();//Crea un Statement para ejecutar la query
            ResultSet rs = stmt.executeQuery(sql.toString());// Ejecuta la query y obtiene el ResultSet

            List<Expense> expenses = new ArrayList<>();// Crea una lista para almacenar los gastos

            //Para no tener que hacer un readAll de deudas en cada iteración creo un cache que se elimina después de usar este metodo.
            Map<Integer, List<Debt>> debtMap = new HashMap<>();//Map para almacenar las deudas por expenseId (Clave: expenseId, Valor: Lista de deudas).
            Response<Debt> allDebts = DebtDAO.getInstance().readAll();// Obtiene todas las deudas

            if (allDebts.isSuccess()) {// Si la respuesta es exitosa, procesa las deudas
                List<Debt> allDebtList = allDebts.getData();// Obtiene la lista de deudas
                for (Debt d: allDebtList) {// Recorre la lista de deudas con un foreach
                    int expenseId = d.getExpenseId();// Obtiene el ID del gasto asociado a la deuda
                    if (!debtMap.containsKey(expenseId)) {//Si el Map no contiene el expenseId, lo agrega
                        debtMap.put(expenseId, new ArrayList<Debt>());// Crea una nueva lista de deudas para ese expenseId
                    }
                    debtMap.get(expenseId).add(d);// Agrega la deuda a la lista correspondiente en el Map
                }
            } else {
                return new Response<>(false, "500", "Error al obtener las deudas: " + allDebts.getMessage());
            }

            while (rs.next()) {
                // Crea un nuevo objeto Expense a partir del ResultSet

                // y asigna las deudas relacionadas desde el Map
                List<Debt> relatedDebts = debtMap.getOrDefault(rs.getInt("id"), new ArrayList<>());

                Expense expense = new Expense(
                        rs.getInt("id"),
                        rs.getDouble("amount"),
                        rs.getDate("date").toLocalDate(),
                        rs.getInt("installments"),
                        relatedDebts, // Asigna las deudas relacionadas
                        rs.getString("description")
                );
                // Agrega el gasto a la lista
                expenses.add(expense);
            }
            return new Response<>(true, "200", "Gastos encontrados", expenses);// Devuelve una lista de gastos

        } catch (SQLException e) {
            return new Response<>(false, "500", e.getMessage());
        }
    }
}
