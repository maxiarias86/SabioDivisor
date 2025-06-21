package org.example.cache;

import org.example.dto.BillDTO;
import org.example.model.Bill;
import org.example.model.Debt;
import org.example.model.Expense;
import org.example.model.User;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class BillCache {
/*Lo armo como un Singleton porque siempre va a haber un solo BillCache. Cuando se haga el login se inicia
y con el logout se limpia.
 */

    private static BillCache instance;
    private ArrayList<BillDTO> bills;

    private BillCache() {
        bills = new ArrayList<>();
    }

    public static BillCache getInstance() {
        if (instance == null) {
            instance = new BillCache();
        }
        return instance;
    }

    public void clearCache() {
        bills.clear();
        instance = null; // elimina la instancia para forzar recreación
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


    public List<BillDTO> billsUpToDateByUser(LocalDate fecha, String userName) {
        List<BillDTO> resultado = new ArrayList<>();
        for (BillDTO bill : bills) {
            if (!bill.getDueDate().isAfter(fecha) && bill.getOtherUser().equalsIgnoreCase(userName)) {
                resultado.add(bill);
            }
        }
        return resultado;
    }


    public ArrayList<BillDTO> getAllBills() {
        return new ArrayList<>(bills);
    }
}
