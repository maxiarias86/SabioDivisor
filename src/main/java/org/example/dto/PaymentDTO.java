package org.example.dto;

import java.time.LocalDate;

public class PaymentDTO {
    private int id;//Puede ser 0 cuando venga del front
    private LocalDate date;
    private int payerId;
    private int recipientId;
    private double amount;

    public PaymentDTO(){

    }

    public PaymentDTO(LocalDate date, int payerId, int recipientId, double amount) {
        id = 0;
        this.setDate(date);
        this.payerId = payerId;
        this.recipientId = recipientId;
        this.setAmount(amount);
    }

    public PaymentDTO(int id, LocalDate date, int payerId, int recipientId, double amount) {
        this.id = id;
        this.setDate(date);
        this.payerId = payerId;
        this.recipientId = recipientId;
        this.setAmount(amount);
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public LocalDate getDate() {
        return date;
    }

    public void setDate(LocalDate date) {
        if (date == null || date.isAfter(LocalDate.now())) {
            throw new IllegalArgumentException("La fecha es inválida, no puede ser nula o futura.");
        }
        this.date = date;
    }

    public int getPayerId() {
        return payerId;
    }

    public void setPayerId(int payerId) {
        this.payerId = payerId;
    }

    public int getRecipientId() {
        return recipientId;
    }

    public void setRecipientId(int recipientId) {
        this.recipientId = recipientId;
    }

    public double getAmount() {
        return amount;
    }

    public void setAmount(double amount) {
        if (amount <= 0) {
            throw new IllegalArgumentException("El monto debe ser mayor a cero");
        }
        this.amount = amount;
    }
}
