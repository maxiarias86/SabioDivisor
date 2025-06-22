package org.example.dto;

import java.time.LocalDate;

public class BillDTO {
    private int id; // ID de la Debt que le dio origen
    private int otherUser; // ID del otro usuario (deudor o acreedor)
    private String description; // Descripción del Expense
    private double amount; // Monto de la Debt (positivo o negativo según el rol)
    private LocalDate dueDate; // Fecha de vencimiento

    public BillDTO(int id, int otherUser, String description, double amount, LocalDate dueDate) {
        this.id = id;
        this.otherUser = otherUser;
        this.description = description;
        this.amount = amount;
        this.dueDate = dueDate;
    }


    // Getters
    public int getId() {
        return id;
    }

    public int getOtherUser() {
        return otherUser;
    }

    public String getDescription() {
        return description;
    }

    public double getAmount() {
        return amount;
    }

    public LocalDate getDueDate() {
        return dueDate;
    }

    // Setters (opcional, solo si pensás modificarlos después)
    public void setId(int id) {
        this.id = id;
    }

    public void setOtherUser(int otherUser) {
        this.otherUser = otherUser;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setAmount(double amount) {
        this.amount = amount;
    }

    public void setDueDate(LocalDate dueDate) {
        this.dueDate = dueDate;
    }
}
