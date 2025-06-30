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
        this.amount = amount;
        this.date = date;
    }
    // Getters
    public int getId() {return id;}

    public double getAmount() {return amount;}

    public LocalDate getDate() {return date;}

    // Setters públicos con validación
    public void setId(int id) {this.id = id;}

    public void setAmount(double amount) {this.amount = amount;}

    public void setDate(LocalDate date) {this.date = date;}


}
