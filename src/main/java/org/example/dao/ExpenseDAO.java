package org.example.dao;

import org.example.dto.ExpenseDTO;
import org.example.model.Debt;
import org.example.model.Expense;
import org.example.model.Response;

import java.sql.*;
import java.sql.Date;
import java.util.*;

public class ExpenseDAO extends BaseDAO<Expense> {
    private final String tableName = "expenses";// Nombre de la tabla en la base de datos
    private static final ExpenseDAO instance = new ExpenseDAO();

    public static ExpenseDAO getInstance() {
        return instance;
    }

    private ExpenseDAO() {
        super();
    }

    @Override
    public Response<Expense> create(Expense entity) {// Metodo para crear un nuevo gasto en la base de datos. NO se usa actualmente en la aplicacion, pero lo dejo por si en un futuro se necesita y porque esta en su clase padre.
        String sql = "INSERT INTO " + tableName + " (amount, date, installments, description) VALUES (?, ?, ?, ?)";

        try {
            PreparedStatement ps = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);// Prepara la consulta SQL para insertar un nuevo gasto. Se trae el ID generado que después lo voy a usar.
            ps.setDouble(1, entity.getAmount());
            ps.setDate(2, Date.valueOf(entity.getDate()));
            ps.setInt(3, entity.getInstallments());
            ps.setString(4, entity.getDescription());
            int rows = ps.executeUpdate();

            if (rows == 0) {
                return new Response<>(false, "500", "No se pudo crear el gasto");
            }

            ResultSet generatedKeys = ps.getGeneratedKeys();// Obtiene las claves generadas por la base de datos
            if (generatedKeys.next()) {// Si hay una clave generada, significa que se insertó correctamente
                int expenseId = generatedKeys.getInt(1);// Recupera el ID del gasto insertado
                entity.setId(expenseId);// Asigna el ID al Expense
            } else {
                return new Response<>(false, "500", "No se obtuvo el ID del gasto insertado");
            }
            return new Response<>(true, "200", "Gasto creado con éxito", entity);// Devuelve una respuesta exitosa con el gasto creado que contiene el ID generado.
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

            if (rs.next()) {// Si se encuentra un gasto con el ID pasado por parámetro
                UserDAO userDAO = UserDAO.getInstance();//SACARLO. ESTABA PORQUE ANTES LA EXPENSE TENIA USUARIOS ASOCIADOS.
                DebtDAO debtDAO = DebtDAO.getInstance();

                Expense expense = new Expense(// Crear el gasto sin deudas
                        rs.getInt("id"),
                        rs.getDouble("amount"),
                        rs.getDate("date").toLocalDate(),
                        rs.getInt("installments"),
                        null,
                        rs.getString("description")
                );

                // Cargar deudas asociadas
                Response<Debt> allDebts = debtDAO.readAll();// Obtiene todas las deudas de la base de datos
                if (allDebts.isSuccess()) {// Si la respuesta es exitosa, procesa las deudas
                    List<Debt> relatedDebts = new ArrayList<>();// Lista para almacenar las deudas relacionadas con el gasto
                    for (Debt d : allDebts.getData()) {// Recorre la lista de deudas
                        if (d.getExpenseId() == expense.getId()) {// Si la deuda está asociada al gasto actual, agrega la deuda a la lista de deudas relacionadas
                            relatedDebts.add(d);
                        }
                    }
                    expense.setDebts(relatedDebts);// Asigna las deudas relacionadas al gasto
                }
                return new Response<>(true, "200", "Gasto encontrado", expense);// Devuelve el gasto encontrado con sus deudas asociadas
            } else {
                return new Response<>(false, "404", "No se encontró el gasto");
            }
        } catch (SQLException e) {
            return new Response<>(false, "500", e.getMessage());
        }
    }

    @Override
    public Response<Expense> update(Expense expenseToUpdate) {// Metodo para actualizar un gasto en la base de datos
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
            return new Response<>(true, "200", "Gasto actualizado con éxito", expenseToUpdate);// Devuelve una respuesta exitosa con el gasto actualizado
        } catch (SQLException e) {
            return new Response<>(false, "500", e.getMessage());
        }
    }

    @Override
    public Response<Expense> delete(int id) {
        String sql = "DELETE FROM " + tableName + " WHERE id = ?";// Prepara la consulta SQL para eliminar un gasto por su ID

        try {
            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setInt(1, id);// Establece el ID del gasto a eliminar
            int rows = ps.executeUpdate();// Ejecuta la consulta de eliminación y obtiene el número de filas afectadas

            if (rows == 1) {// Si se eliminó una fila, significa que el gasto fue eliminado correctamente
                return new Response<>(true, "200", "Gasto eliminado correctamente");// Devuelve una respuesta exitosa indicando que el gasto fue eliminado
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

    public Response<Integer> save(ExpenseDTO dto, List<Debt> debts) {//En este metodo cuando inserte el Expense tengo que copiarme el id, y luego insertar las deudas con ese expenseId
        String insertExpenseSQL = "INSERT INTO expenses (amount, date, installments, description) VALUES (?, ?, ?, ?)";// Prepara la consulta SQL para insertar un nuevo gasto
        String insertDebtSQL = "INSERT INTO debts (amount, due_date, debtor_id, creditor_id, expense_id, installment_number) VALUES (?, ?, ?, ?, ?, ?)";// Prepara la consulta SQL para insertar una nueva deuda

        try {
            conn.setAutoCommit(false);//Hace que no se haga un commit en la base de datos hasta que el código lo indique.

            // Insertar Expense
            PreparedStatement expenseStmt = conn.prepareStatement(insertExpenseSQL, Statement.RETURN_GENERATED_KEYS);// Prepara la consulta SQL para insertar un nuevo gasto y obtener el ID generado por la base de datos.
            expenseStmt.setDouble(1, dto.getAmount());
            expenseStmt.setDate(2, Date.valueOf(dto.getDate()));
            expenseStmt.setInt(3, dto.getInstallments());
            expenseStmt.setString(4, dto.getDescription());
            expenseStmt.executeUpdate();

            ResultSet generatedKeys = expenseStmt.getGeneratedKeys();// Obtiene las claves generadas por la base de datos después de insertar el gasto.
            if (!generatedKeys.next()) {// Si no se obtuvo una clave generada, significa que la inserción falló.
                conn.rollback();//Hace un rollback y no queda nada guardado.
                return new Response<>(false, "500", "No se pudo obtener el ID del gasto insertado.");
            }

            int expenseId = generatedKeys.getInt(1);//Recupera el expenseId

            // Insertar Debts
            if (debts != null && !debts.isEmpty()) {// Verifica que la lista de deudas no es nula ni está vacía
                PreparedStatement debtStmt = conn.prepareStatement(insertDebtSQL);
                for (Debt debt : debts) {// Recorre la lista de deudas y las inserta en la base de datos
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
            return new Response<>(true, "201", "Gasto y deudas insertados correctamente.", expenseId);// Devuelve una respuesta exitosa con el ID del gasto insertado.
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

    public Response<Expense> readAllInSet(HashSet<Integer> expensesIDs) {// Metodo para leer todos los gastos cuyos IDs están en el Set pasado por parámetro. Lo iba a usar en la pantalla de edición, pero al final no lo hice.
        if(expensesIDs == null || expensesIDs.isEmpty()) {// Verifica si el Set de IDs está vacío o es nulo
            return new Response<>(false, "400", "El conjunto de IDs está vacío");
        }
        StringBuilder sql = new StringBuilder("SELECT * FROM " + tableName + " WHERE id IN (");// Inicia la query con un IN. Se usa un StringBuilder para ir haciendo appends.
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
