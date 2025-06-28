package org.example;

import org.example.cache.DebtCache;
import org.example.dto.UserDTO;
import org.example.model.Debt;

public class Test {
    public static void main(String[] args) {
        // Supongamos que el usuario con ID 1 está logueado
        UserDTO user = new UserDTO(1, "Juan", "juan@email.com");

        // Inicializamos el DebtCache para ese usuario
        DebtCache.reset(); // Limpia por si quedó otra sesión
        DebtCache cache = DebtCache.getInstance(user);

        // Recorremos e imprimimos las deudas
        System.out.println("📄 DEUDAS DEL USUARIO " + user.getName() + ":");
        for (Debt d : cache.getDebts()) {
            System.out.println("- ID: " + d.getId() +
                    ", Monto: " + d.getAmount() +
                    ", Cuota: " + d.getInstallmentNumber() +
                    ", Fecha: " + d.getDueDate() +
                    ", Acreedor: " + d.getCreditor().getName() +
                    ", Deudor: " + d.getDebtor().getName() +
                    ", ExpenseID: " + d.getExpenseId());
        }
    }
}
