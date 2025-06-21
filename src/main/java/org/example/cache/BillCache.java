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
                User other = isCreditor ? debt.getDebtor() : debt.getCreditor();
                Expense expense = debt.getExpense();
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

    public double verEstadoDeCuentaAFecha(LocalDate fecha) {
        return bills.stream()
                .filter(b -> !b.getDueDate().isAfter(fecha))
                .mapToDouble(BillDTO::getAmount)
                .sum();
    }

    public List<BillDTO> verDeudasAFecha(LocalDate fecha) {
        return bills.stream()
                .filter(b -> !b.getDueDate().isAfter(fecha))
                .collect(Collectors.toList());
    }

    public List<BillDTO> verDeudasAFechaPorUsuario(LocalDate fecha, String userName) {
        return bills.stream()
                .filter(b -> !b.getDueDate().isAfter(fecha) && b.getOtherUser().equalsIgnoreCase(userName))
                .collect(Collectors.toList());
    }

    public ArrayList<BillDTO> getAllBills() {
        return new ArrayList<>(bills);
    }
}
