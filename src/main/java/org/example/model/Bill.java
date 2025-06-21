package org.example.model;

import org.example.dto.BillDTO;

import java.time.LocalDate;

public class Bill {
    private int id; // ID de la Debt
    private User otherUser; // El otro usuario (no el que la genera)
    private String description; // del Expense
    private double amount; // Positivo si soy acreedor, negativo si soy deudor
    private LocalDate dueDate;

    public Bill(int id, User otherUser, String description, double amount, LocalDate dueDate) {
        this.id = id;
        this.otherUser = otherUser;
        this.description = description;
        this.amount = amount;
        this.dueDate = dueDate;
    }

    public BillDTO toDTO() {
        return new BillDTO(id, otherUser.getName(), description, amount, dueDate);
    }

    // Getters
    public int getId() {
        return id;
    }

    public User getOtherUser() {
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
}
