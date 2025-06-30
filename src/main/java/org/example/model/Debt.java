package org.example.model;

import java.time.LocalDate;
import java.util.Objects;

public class Debt {
    private int id;
    private double amount;
    private User creditor;
    private User debtor;
    private int expenseId;
    private LocalDate dueDate;
    private int installmentNumber;


    public Debt() {}

    public Debt(int id, double amount, User creditor, User debtor, int expenseId, LocalDate dueDate, int installmentNumber) {
        setId(id);
        setAmount(amount);
        setCreditor(creditor);
        setDebtor(debtor);
        setExpenseId(expenseId);
        setDueDate(dueDate);
        setInstallmentNumber(installmentNumber);
    }

    public Debt(int id, double amount, User debtor, User creditor, LocalDate dueDate) {
        this(id, amount, creditor, debtor, 0, dueDate,1);
    }

    // Getters
    public int getId() {
        return id;
    }

    public double getAmount() {
        return amount;
    }

    public User getCreditor() {
        return creditor;
    }

    public User getDebtor() {
        return debtor;
    }

    public int getExpenseId() {
        return expenseId;
    }

    public LocalDate getDueDate() {
        return dueDate;
    }

    public int getInstallmentNumber() {
        return installmentNumber;
    }


    // Setters con validación
    public void setId(int id) {
        this.id = id;
    }

    public void setAmount(double amount) {
        if (amount <= 0) {
            throw new IllegalArgumentException("El monto de la deuda debe ser mayor a cero");
        }
        this.amount = amount;
    }

    public void setCreditor(User creditor) {
        if (creditor == null) {
            throw new IllegalArgumentException("El acreedor no puede ser nulo");
        }
        this.creditor = creditor;
    }

    public void setDebtor(User debtor) {
        if (debtor == null) {
            throw new IllegalArgumentException("El deudor no puede ser nulo");
        }
        if (this.creditor != null && this.creditor.equals(debtor)) {
            throw new IllegalArgumentException("El deudor no puede ser el mismo que el acreedor");
        }
        this.debtor = debtor;
    }

    public void setExpenseId(int expenseId) {
        this.expenseId = expenseId;
    }

    public void setDueDate(LocalDate dueDate) {
        if (dueDate == null) {
            throw new IllegalArgumentException("La fecha de vencimiento no puede ser nula");
        }
        this.dueDate = dueDate;
    }

    public void setInstallmentNumber(int installmentNumber) {
        if (installmentNumber <= 0) {
            throw new IllegalArgumentException("El número de cuotas debe ser mayor o igual a 1");
        }
        this.installmentNumber = installmentNumber;
    }
}
