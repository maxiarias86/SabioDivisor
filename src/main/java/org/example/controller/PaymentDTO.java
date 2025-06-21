package org.example.controller;

import java.time.LocalDate;

public class PaymentDTO {
    private LocalDate date;
    private int payerId;
    private int payeeId;
    private double amount;

    public PaymentDTO(LocalDate date, int payerId, int payeeId, double amount) {
        this.date = date;
        this.payerId = payerId;
        this.payeeId = payeeId;
        this.amount = amount;
    }

    public LocalDate getDate() {
        return date;
    }

    public void setDate(LocalDate date) {
        this.date = date;
    }

    public int getPayerId() {
        return payerId;
    }

    public void setPayerId(int payerId) {
        this.payerId = payerId;
    }

    public int getPayeeId() {
        return payeeId;
    }

    public void setPayeeId(int payeeId) {
        this.payeeId = payeeId;
    }

    public double getAmount() {
        return amount;
    }

    public void setAmount(double amount) {
        this.amount = amount;
    }
}
