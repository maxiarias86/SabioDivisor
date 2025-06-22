package org.example.cache;

import org.example.dao.DebtDAO;
import org.example.dao.ExpenseDAO;
import org.example.dao.PaymentDAO;
import org.example.dao.UserDAO;
import org.example.dto.BillDTO;
import org.example.model.Bill;
import org.example.model.Debt;
import org.example.model.Expense;
import org.example.model.User;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

/*
Lo armo como un Singleton porque siempre va a haber un solo BillCache. Cuando se haga el login se inicia
y con el logout se limpia.
*/

public class BillCache {

    private static BillCache instance;

    private final DebtDAO debtDAO;
    private final ExpenseDAO expenseDAO;
    private final UserDAO userDAO;
    private final PaymentDAO paymentDAO;


    private ArrayList<BillDTO> bills;

    public static BillCache getInstance() {
        if (instance == null) {
            instance = new BillCache();
        }
        return instance;
    }

    private BillCache() {
        bills = new ArrayList<>();
        this.debtDAO = DebtDAO.getInstance();
        this.expenseDAO = ExpenseDAO.getInstance();
        this.userDAO = UserDAO.getInstance();
        this.paymentDAO = PaymentDAO.getInstance();
    }

    public void clearCache() {
        bills.clear();
        instance = null; // elimina la instancia para forzar creacion de nuevo cache con nuevo login
    }

    public void createBillCache(User user, List<Debt> allDebts) {
        bills.clear();

        for (Debt debt : allDebts) {
            boolean isCreditor = debt.getCreditor().equals(user);
            boolean isDebtor = debt.getDebtor().equals(user);

            if (isCreditor || isDebtor) {
                //Si el user isCreditor asigno el Debtor a other, si no lo es asigno el Creditor.
                User other = isCreditor ? debt.getDebtor() : debt.getCreditor();
                Expense expense = debt.getExpense();
                //Si el user isCreditor el amount va a ser positivo, sino negativo.
                double signedAmount = isCreditor ? debt.getAmount() : -debt.getAmount();

                Bill bill = new Bill(
                        debt.getId(),
                        other,
                        expense.getDescription(),
                        signedAmount,
                        debt.getDueDate()
                );

                bills.add(bill.toDTO());
            }
        }
        //for (Payment payment: )
    }

    public double accountUpToDate(LocalDate date) {
        double total = 0;
        for (BillDTO bill : bills) {
            if (!bill.getDueDate().isAfter(date)) {
                total += bill.getAmount();
            }
        }
        return total;
    }


    public List<BillDTO> billsUpToDate(LocalDate fecha) {
        List<BillDTO> resultado = new ArrayList<>();
        for (BillDTO bill : bills) {
            if (!bill.getDueDate().isAfter(fecha)) {
                resultado.add(bill);
            }
        }
        return resultado;
    }


    public List<BillDTO> billsUpToDateByUser(LocalDate fecha, int userId) {
        List<BillDTO> resultado = new ArrayList<>();
        for (BillDTO bill : bills) {
            if (!bill.getDueDate().isAfter(fecha) && bill.getOtherUser() == userId) {
                resultado.add(bill);
            }
        }
        return resultado;
    }


    public ArrayList<BillDTO> getAllBills() {
        return new ArrayList<>(bills);
    }
}
