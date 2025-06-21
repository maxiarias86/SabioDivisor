package org.example;

import org.example.cache.BillCache;
import org.example.dto.BillDTO;
import org.example.model.Debt;
import org.example.model.Expense;
import org.example.model.User;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class Main {
    public static void main(String[] args) {
        // Crear usuarios con constructor compatible
        User maxi = new User("Maxi", "maxi@email.com", "1234");
        maxi.setId(1);

        User juan = new User("Juan", "juan@email.com", "1234");
        juan.setId(2);

        // Crear expense
        Expense almuerzo = new Expense();
        almuerzo.setId(100);
        almuerzo.setDescription("Almuerzo en el parque");

        // Crear deudas usando el constructor completo disponible
        List<Debt> allDebts = new ArrayList<>();

        allDebts.add(new Debt(1, 1000, maxi, juan, almuerzo, LocalDate.of(2025, 6, 15), 1)); // Juan debe a Maxi
        allDebts.add(new Debt(2, 1500, juan, maxi, almuerzo, LocalDate.of(2025, 6, 25), 1)); // Maxi debe a Juan
        allDebts.add(new Debt(3, 500, maxi, juan, almuerzo, LocalDate.of(2025, 6, 20), 1));  // Juan debe a Maxi

        // Crear el BillCache para Maxi
        BillCache.getInstance().createBillCache(maxi, allDebts);

        // Mostrar todas las BillDTO
        System.out.println("\uD83D\uDD0E Todas las Bills de Maxi:");
        for (BillDTO bill : BillCache.getInstance().getAllBills()) {
            System.out.printf("- [%d] %s | %s | $%.2f | Vence: %s%n",
                    bill.getId(), bill.getOtherUser(), bill.getDescription(),
                    bill.getAmount(), bill.getDueDate()
            );
        }

        // Ver estado de cuenta a hoy
        LocalDate hoy = LocalDate.of(2025, 6, 21);
        double saldo = BillCache.getInstance().verEstadoDeCuentaAFecha(hoy);
        System.out.println("\n\uD83D\uDCB0 Estado de cuenta a " + hoy + ": $" + saldo);

        // Ver deudas vencidas
        System.out.println("\n\uD83D\uDCC5 Deudas vencidas hasta " + hoy + ":");
        for (BillDTO bill : BillCache.getInstance().verDeudasAFecha(hoy)) {
            System.out.println("- " + bill.getOtherUser() + ": $" + bill.getAmount());
        }

        // Ver deudas vencidas con Juan
        System.out.println("\n\uD83D\uDCCC Deudas con Juan vencidas hasta " + hoy + ":");
        for (BillDTO bill : BillCache.getInstance().verDeudasAFechaPorUsuario(hoy, "Juan")) {
            System.out.println("- " + bill.getDescription() + ": $" + bill.getAmount());
        }

        // Limpiar la cache
        BillCache.getInstance().clearCache();
    }
}
