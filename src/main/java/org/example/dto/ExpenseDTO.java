package org.example.dto;

import java.time.LocalDate;
import java.util.Map;

public class ExpenseDTO {
    private Integer id;
    private double amount;
    private LocalDate date;
    private int installments;
    private String description;

    // userId → monto
    private Map<Integer, Double> payers;
    private Map<Integer, Double> debtors;

    public ExpenseDTO() {}

    public ExpenseDTO(double amount, LocalDate date, int installments, String description,
                      Map<Integer, Double> payers, Map<Integer, Double> debtors) {
        this.amount = amount;
        this.date = date;
        this.installments = installments;
        this.description = description;
        this.payers = payers;
        this.debtors = debtors;
        this.id = null;
    }
    public ExpenseDTO(int id, double amount, LocalDate date, int installments, String description,
                      Map<Integer, Double> payers, Map<Integer, Double> debtors) {
        this.amount = amount;
        this.date = date;
        this.installments = installments;
        this.description = description;
        this.payers = payers;
        this.debtors = debtors;
        this.id = id;
    }

    // Getters y setters

    public Integer getId() {
        return id;
    }
    public void setId(Integer id) {
        this.id = id;
    }

    public double getAmount() {
        return amount;
    }

    public void setAmount(double amount) {
        this.amount = amount;
    }

    public LocalDate getDate() {
        return date;
    }

    public void setDate(LocalDate date) {
        this.date = date;
    }

    public int getInstallments() {
        return installments;
    }

    public void setInstallments(int installments) {
        this.installments = installments;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Map<Integer, Double> getPayers() {
        return payers;
    }

    public void setPayers(Map<Integer, Double> payers) {
        this.payers = payers;
    }

    public Map<Integer, Double> getDebtors() {
        return debtors;
    }

    public void setDebtors(Map<Integer, Double> debtors) {
        this.debtors = debtors;
    }
}
