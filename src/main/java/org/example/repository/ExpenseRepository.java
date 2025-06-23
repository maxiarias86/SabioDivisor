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
                    null,
                    rs.getString("description")
            );
            expenseCache.put(e.getId(), e);
            expenses.add(e);
        }

        // 4. Cargar deudas
        DebtDAO debtDAO = DebtDAO.getInstance();
        Response<Debt> debtResponse = debtDAO.readAll();
        if (debtResponse.isSuccess()) {
            for (Debt d : debtResponse.getData()) {
                Expense e = expenseCache.get(d.getExpenseId());
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
