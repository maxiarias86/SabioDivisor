package org.example.repository;

import org.example.dao.DebtDAO;
import org.example.dao.UserDAO;
import org.example.model.*;

import java.sql.*;
import java.util.*;

public class ExpenseRepository {
    private final Map<Integer, User> userCache = new HashMap<>();
    private final UserDAO userDAO = UserDAO.getInstance();  // Instancia única

    private final Connection conn;

    public ExpenseRepository(Connection conn) {
        this.conn = conn;
    }

    // Método para obtener un usuario con caché
    private User getUserFromCache(int userId) {
        if (userCache.containsKey(userId)) {
            return userCache.get(userId);
        } else {
            Response<User> response = userDAO.read(userId);
            if (response.isSuccess()) {
                User user = response.getObj();
                userCache.put(userId, user);
                return user;
            } else {
                return null;
            }
        }
    }

    public List<Expense> loadAllExpenses(ResultSet rs) throws SQLException {
        List<Expense> expenses = new ArrayList<>();
        Map<Integer, Expense> expenseCache = new HashMap<>();

        // 1. Armar gastos base
        while (rs.next()) {
            Expense e = new Expense(
                    rs.getInt("id"),
                    rs.getDouble("amount"),
                    rs.getDate("date").toLocalDate(),
                    rs.getInt("installments"),
                    null, null, null,
                    rs.getString("description")
            );
            expenseCache.put(e.getId(), e);
            expenses.add(e);
        }

        // 2. Cargar payers
        PreparedStatement psPayers = conn.prepareStatement("SELECT * FROM expense_payers");
        ResultSet rsPayers = psPayers.executeQuery();
        while (rsPayers.next()) {
            int expenseId = rsPayers.getInt("expense_id");
            int userId = rsPayers.getInt("user_id");
            double amount = rsPayers.getDouble("amount");

            Expense e = expenseCache.get(expenseId);
            if (e != null) {
                Map<User, Double> payers = e.getPayers();
                if (payers == null) {
                    payers = new HashMap<>();
                    e.setPayers(payers);
                }
                User user = getUserFromCache(userId);
                if (user != null) {
                    payers.put(user, amount);
                }
            }
        }

        // 3. Cargar debtors
        PreparedStatement psDebtors = conn.prepareStatement("SELECT * FROM expense_debtors");
        ResultSet rsDebtors = psDebtors.executeQuery();
        while (rsDebtors.next()) {
            int expenseId = rsDebtors.getInt("expense_id");
            int userId = rsDebtors.getInt("user_id");
            double amount = rsDebtors.getDouble("amount");

            Expense e = expenseCache.get(expenseId);
            if (e != null) {
                Map<User, Double> debtors = e.getDebtors();
                if (debtors == null) {
                    debtors = new HashMap<>();
                    e.setDebtors(debtors);
                }
                User user = getUserFromCache(userId);
                if (user != null) {
                    debtors.put(user, amount);
                }
            }
        }

        // 4. Cargar deudas
        DebtDAO debtDAO = DebtDAO.getInstance();
        Response<Debt> debtResponse = debtDAO.readAll();
        if (debtResponse.isSuccess()) {
            for (Debt d : debtResponse.getData()) {
                Expense e = expenseCache.get(d.getExpense().getId());
                if (e != null) {
                    List<Debt> debts = e.getDebts();
                    if (debts == null) {
                        debts = new ArrayList<>();
                        e.setDebts(debts);
                    }
                    debts.add(d);
                }
            }
        }

        return expenses;
    }
}
