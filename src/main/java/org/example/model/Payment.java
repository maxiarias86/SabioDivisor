package org.example.model;

import java.time.LocalDate;
import java.util.Objects;

public class Payment extends Transaction {
    private User payer;
    private User recipient;

    public Payment() {
    }

    public Payment(double amount, LocalDate date, User payer, User recipient) {
        super(0, amount, date); // El ID lo pone la base de datos
        setPayer(payer);
        setRecipient(recipient);
    }


    public Payment(int id, double amount, LocalDate date, User payer, User recipient) {
        super(id, amount, date);
        //Se agregan con los setters para utilizar las validaciones
        setPayer(payer);
        setRecipient(recipient);
    }

    // Getters
    public User getPayer() {
        return payer;
    }

    public User getRecipient() {
        return recipient;
    }

    // Setters con validación
    public void setPayer(User payer) {
        try {
            if (payer == null) {
                throw new IllegalArgumentException("El pagador no puede ser nulo");
            }
            this.payer = payer;
        } catch (IllegalArgumentException e) {
            System.out.println("Error en 'payer': " + e.getMessage());
        }
    }

    public void setRecipient(User recipient) {
        try {
            if (recipient == null) {
                throw new IllegalArgumentException("El destinatario no puede ser nulo");
            }
            if (this.payer != null && payer.equals(recipient)) {
                throw new IllegalArgumentException("El pagador no puede ser el mismo que el destinatario");
            }
            this.recipient = recipient;
        } catch (IllegalArgumentException e) {
            System.out.println("Error en 'recipient': " + e.getMessage());
        }
    }

    // equals y hashCode por id
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Payment)) return false;
        Payment payment = (Payment) o;
        return getId() == payment.getId();
    }

    @Override
    public int hashCode() {
        return Objects.hash(getId());
    }

    // toString
    @Override
    public String toString() {
        return "Payment{" +
                "id=" + getId() +
                ", amount=" + getAmount() +
                ", date=" + getDate() +
                ", payer=" + (payer != null ? payer.getName() : "null") +
                ", recipient=" + (recipient != null ? recipient.getName() : "null") +
                '}';
    }
}
