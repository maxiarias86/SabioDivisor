package org.example.model;

import java.time.LocalDate;
import java.util.Objects;

public class Debt {
    private int id;
    private double amount;
    private User creditor;
    private User debtor;
    private Expense expense;
    private LocalDate dueDate;

    public Debt() {}

    public Debt(int id, double amount, User creditor, User debtor, Expense expense, LocalDate dueDate) {
        setId(id);
        setAmount(amount);
        setCreditor(creditor);
        setDebtor(debtor);
        setExpense(expense);
        setDueDate(dueDate);
    }

    public Debt(int id, double amount, User debtor, User creditor, LocalDate dueDate) {
        this(id, amount, creditor, debtor, null, dueDate);
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

    public Expense getExpense() {
        return expense;
    }

    public LocalDate getDueDate() {
        return dueDate;
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

    public void setExpense(Expense expense) {
        this.expense = expense;
    }

    public void setDueDate(LocalDate dueDate) {
        if (dueDate == null) {
            throw new IllegalArgumentException("La fecha de vencimiento no puede ser nula");
        }
        this.dueDate = dueDate;
    }

    // equals y hashCode basados en id
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Debt)) return false;
        Debt debt = (Debt) o;
        return id == debt.id;
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

    // toString para depuración
    @Override
    public String toString() {
        return "Debt{" +
                "id=" + id +
                ", amount=" + amount +
                ", creditor=" + (creditor != null ? creditor.getName() : "null") +
                ", debtor=" + (debtor != null ? debtor.getName() : "null") +
                ", expenseId=" + (expense != null ? expense.getId() : "null") +
                ", dueDate=" + dueDate +
                '}';
    }
}
