package org.example.service;

import org.example.cache.DebtCache;
import org.example.cache.ExpenseCache;
import org.example.cache.PaymentCache;
import org.example.cache.UserCache;
import org.example.dto.UserDTO;
import org.example.model.Debt;
import org.example.model.Expense;
import org.example.model.Payment;
import org.example.model.Response;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

public class BalanceService {
    private DebtCache debtCache;
    private ExpenseCache expenseCache;
    private PaymentCache paymentCache;
    private UserCache userCache;
    private UserDTO user;
    private LocalDate date;

    public BalanceService(UserDTO user, LocalDate date) {
        this.user = user;
        if(date != null) {
            this.date = date; // Si no
        }else{
            this.date = LocalDate.now(); // Si la fecha es nula, se usa la fecha actual
        }
         // Si la fecha es nula, se usa la fecha actual
        this.debtCache = DebtCache.getInstance(user);
        this.paymentCache = PaymentCache.getInstance(user);
        this.userCache = UserCache.getInstance();
        this.expenseCache = ExpenseCache.getInstance(user);
    }

    public List<String> getUserBalances(UserDTO user, LocalDate date) {
        List<String> balances = new ArrayList<>();
        for (UserDTO friend : userCache.getAllUsers().values()) {
            if (friend.getId() == user.getId()) {
                continue;
            }
            List<Payment> paymentsBetween = paymentCache.getOtherUserPayments(friend);// Obtiene los pagos entre el usuario actual y el amigo
            List<Debt> debtsBetween = debtCache.getOtherUserDebts(friend);// Obtiene las deudas entre el usuario actual y el amigo
            double balance = 0.0;// Calcular el balance total a la fecha
            for (Payment payment : paymentsBetween) {
                if(payment.getDate().isAfter(date)) {
                    continue; // Ignorar pagos futuros a la fecha.
                }
                if (payment.getPayer().getId() == user.getId()) {//Comprueba que el usuario actual es el pagador
                    balance += payment.getAmount(); // Sumar el pago del balance
                } else if (payment.getRecipient().getId() == user.getId()) {// Comprueba que el usuario actual es el receptor
                    balance -= payment.getAmount();// Restar el pago al balance
                }// Si el pago es entre otro usuario y el amigo, no se muestra. Igualmente no debería pasar.
            }
            for (Debt debt : debtsBetween) {
                if(debt.getDueDate().isAfter(date)) {
                    continue; // Ignorar deudas futuras a la fecha.
                }
                if (debt.getCreditor().getId() == user.getId()) {
                    balance += debt.getAmount(); // Sumar la deuda al balance
                } else if (debt.getDebtor().getId() == user.getId()) {
                    balance -= debt.getAmount(); // Restar la deuda del balance
                }
            }
            if (balance != 0) {
                balances.add(String.format("ID %d - %s: $%.2f",friend.getId(),friend.getName(),balance));
            }
        }
        return balances;

    }




    // Aquí puedes implementar la lógica relacionada con el balance de los usuarios
    // Por ejemplo, métodos para obtener el balance, actualizarlo, etc.

    // Ejemplo de método para obtener el balance de un usuario
    public double getBalance(int userId) {
        // Lógica para obtener el balance del usuario por su ID
        return 0.0; // Retorna un valor de ejemplo
    }

    // Ejemplo de método para actualizar el balance de un usuario
    public boolean updateBalance(int userId, double newBalance) {
        // Lógica para actualizar el balance del usuario por su ID
        return true; // Retorna true si la actualización fue exitosa
    }
}
