package org.example.model;

import java.time.LocalDate;

public abstract class Transaction {
    protected int id;
    protected double amount;
    protected LocalDate date;

    public Transaction() {
    }

    public Transaction(int id, double amount, LocalDate date) {
        this.id = id;
        setAmount(amount);
        this.date = date;
    }

    // Getters
    public int getId() {
        return id;
    }

    public double getAmount() {
        return amount;
    }

    public LocalDate getDate() {
        return date;
    }

    // Setters públicos con validación
    public void setId(int id) {
        this.id = id;
    }

    public void setAmount(double amount) {
        try {
            if (amount <= 0) {
                throw new IllegalArgumentException("El monto debe ser mayor a cero");
            }
            this.amount = amount;
        } catch (IllegalArgumentException e) {
            System.out.println("Error en 'amount': " + e.getMessage());
        }
    }

    public void setDate(LocalDate date) {
        try {
            if (date == null) {
                throw new IllegalArgumentException("La fecha no puede ser nula");
            }
            if (date.isAfter(LocalDate.now())) {
                throw new IllegalArgumentException("La fecha no puede ser futura");
            }
            this.date = date;
        } catch (IllegalArgumentException e) {
            System.out.println("Error en 'date': " + e.getMessage());
        }
    }

}
