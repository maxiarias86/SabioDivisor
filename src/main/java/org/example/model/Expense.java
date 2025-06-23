package org.example.model;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;
import java.util.Objects;

public class Expense extends Transaction {
    private int installments;
    private List<Debt> debts;
    private String description;

    public Expense() {
    }

    public Expense(int id, double amount, LocalDate date, int installments,
                   List<Debt> debts, String description) {
        super(id, amount, date);
        setInstallments(installments);
        setDebts(debts);
        setDescription(description);
    }

    // Getters públicos
    public int getInstallments() {
        return installments;
    }

    public List<Debt> getDebts() {
        return debts;
    }

    public String getDescription() {
        return description;
    }

    // Setters públicos con validación
    public void setInstallments(int installments) {
        try {
            if (installments < 1) {
                throw new IllegalArgumentException("La cantidad de cuotas debe ser al menos 1");
            }
            this.installments = installments;
        } catch (IllegalArgumentException e) {
            System.out.println("Error en 'installments': " + e.getMessage());
        }
    }


    public void setDebts(List<Debt> debts) {
        this.debts = debts;
    }

    public void setDescription(String description) {
        try {
            if (description == null || description.isBlank()) {
                throw new IllegalArgumentException("La descripción no puede estar vacía");
            }
            this.description = description;
        } catch (IllegalArgumentException e) {
            System.out.println("Error en 'description': " + e.getMessage());
        }
    }

    // equals y hashCode basados en id
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Expense)) return false;
        Expense expense = (Expense) o;
        return this.getId() == expense.getId();
    }

    @Override
    public int hashCode() {
        return Objects.hash(getId());
    }

    @Override
    public String toString() {
        return "Expense{" +
                "id=" + getId() +
                ", amount=" + super.getAmount() +
                ", date=" + super.getDate() +
                ", installments=" + installments +
                ", description='" + description + '\'' +
                '}';
    }
}
