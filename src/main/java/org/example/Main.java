package org.example;

import org.example.controller.ExpenseDTO;
import org.example.dao.DebtDAO;
import org.example.model.Debt;
import org.example.model.Response;
import org.example.service.ExpenseService;
import org.example.cache.UserCache;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class Main {
    public static void main(String[] args) {
        ExpenseService service = new ExpenseService();

        // 🧾 Mostrar usuarios disponibles en caché
        System.out.println("🔍 Usuarios disponibles en caché: " + UserCache.getInstance().getAllUsers().keySet());

        // ❌ TEST 1: Gasto mal formado
        Map<Integer, Double> payers1 = new HashMap<>();
        payers1.put(1, 50.0);

        Map<Integer, Double> debtors1 = new HashMap<>();
        debtors1.put(2, 60.0);

        ExpenseDTO dto1 = new ExpenseDTO(
                -100.0,
                LocalDate.now(),
                1,
                "Error de prueba",
                payers1,
                debtors1
        );

        Response<Integer> result1 = service.registerExpense(dto1);
        if (result1.isSuccess()) {
            System.out.println("✅ [TEST 1] Gasto registrado con ID: " + result1.getObj());
        } else {
            System.out.println("❌ [TEST 1] Error " + result1.getCode() + ": " + result1.getMessage());
        }

        // ✅ TEST 2: Gasto válido (solo IDs existentes: 1, 2, 21)
        Map<Integer, Double> payers2 = new HashMap<>();
        payers2.put(1, 100.0);

        Map<Integer, Double> debtors2 = new HashMap<>();
        debtors2.put(2, 60.0);
        debtors2.put(21, 40.0);

        ExpenseDTO dto2 = new ExpenseDTO(
                100.0,
                LocalDate.now(),
                1,
                "Cena compartida",
                payers2,
                debtors2
        );

        Response<Integer> result2 = service.registerExpense(dto2);
        if (result2.isSuccess()) {
            System.out.println("✅ [TEST 2] Gasto registrado con ID: " + result2.getObj());

            int expenseId = result2.getObj();
            List<Debt> allDebts = DebtDAO.getInstance().readAll().getData();
            List<Debt> debtsForExpense = allDebts.stream()
                    .filter(debt -> debt.getExpense() != null && debt.getExpense().getId() == expenseId)
                    .collect(Collectors.toList());

            System.out.println("📋 Deudas registradas para el gasto ID " + expenseId + " (" + debtsForExpense.size() + " en total):");
            debtsForExpense.forEach(System.out::println);

        } else {
            System.out.println("❌ [TEST 2] Error " + result2.getCode() + ": " + result2.getMessage());
        }

        // ✅ TEST 3: Gasto en cuotas (IDs existentes: 1, 2)
        Map<Integer, Double> payers3 = new HashMap<>();
        payers3.put(1, 300.0);

        Map<Integer, Double> debtors3 = new HashMap<>();
        debtors3.put(2, 300.0);

        ExpenseDTO dto3 = new ExpenseDTO(
                300.0,
                LocalDate.now(),
                3,
                "Compra en 3 cuotas",
                payers3,
                debtors3
        );

        Response<Integer> result3 = service.registerExpense(dto3);
        if (result3.isSuccess()) {
            System.out.println("✅ [TEST 3] Gasto en cuotas registrado con ID: " + result3.getObj());

            int expenseId = result3.getObj();
            List<Debt> allDebts = DebtDAO.getInstance().readAll().getData();
            List<Debt> debtsForExpense = allDebts.stream()
                    .filter(debt -> debt.getExpense() != null && debt.getExpense().getId() == expenseId)
                    .collect(Collectors.toList());

            System.out.println("📋 Deudas registradas para el gasto ID " + expenseId + " (" + debtsForExpense.size() + " en total):");
            debtsForExpense.forEach(System.out::println);

        } else {
            System.out.println("❌ [TEST 3] Error " + result3.getCode() + ": " + result3.getMessage());
        }

        // ✅ TEST 4: Gasto en 4 cuotas entre varios usuarios
        Map<Integer, Double> payers4 = new HashMap<>();
        payers4.put(1, 500.0);
        payers4.put(2, 500.0);

        Map<Integer, Double> debtors4 = new HashMap<>();
        debtors4.put(21, 600.0);
        debtors4.put(22, 400.0);

        ExpenseDTO dto4 = new ExpenseDTO(
                1000.0,
                LocalDate.now(),
                4,
                "Electrodomésticos en 4 cuotas",
                payers4,
                debtors4
        );

        Response<Integer> result4 = service.registerExpense(dto4);
        if (result4.isSuccess()) {
            System.out.println("✅ [TEST 4] Gasto en 4 cuotas registrado con ID: " + result4.getObj());

            int expenseId = result4.getObj();
            List<Debt> allDebts = DebtDAO.getInstance().readAll().getData();
            List<Debt> debtsForExpense = allDebts.stream()
                    .filter(debt -> debt.getExpense() != null && debt.getExpense().getId() == expenseId)
                    .collect(Collectors.toList());

            System.out.println("📋 Deudas registradas para el gasto ID " + expenseId + " (" + debtsForExpense.size() + " en total):");
            debtsForExpense.forEach(System.out::println);

        } else {
            System.out.println("❌ [TEST 4] Error " + result4.getCode() + ": " + result4.getMessage());
        }

        // ✅ TEST 5: Gasto en 3 cuotas con 2 deudores y 1 pagador (IDs: Sofi=22, Maxi=1, Juan=2)
        Map<Integer, Double> payers5 = new HashMap<>();
        payers5.put(22, 300.0); // Sofi paga todo

        Map<Integer, Double> debtors5 = new HashMap<>();
        debtors5.put(1, 150.0); // Maxi debe la mitad
        debtors5.put(2, 150.0); // Juan la otra mitad

        ExpenseDTO dto5 = new ExpenseDTO(
                300.0,
                LocalDate.now(),
                3,
                "Gasto TEST 5: Viaje compartido",
                payers5,
                debtors5
        );

        Response<Integer> result5 = service.registerExpense(dto5);
        if (result5.isSuccess()) {
            System.out.println("✅ [TEST 5] Gasto en 3 cuotas registrado con ID: " + result5.getObj());

            int expenseId = result5.getObj();
            List<Debt> allDebts = DebtDAO.getInstance().readAll().getData();
            List<Debt> debtsForExpense = allDebts.stream()
                    .filter(debt -> debt.getExpense() != null && debt.getExpense().getId() == expenseId)
                    .collect(Collectors.toList());

            System.out.println("📋 Deudas registradas para el gasto ID " + expenseId + " (" + debtsForExpense.size() + " en total):");
            debtsForExpense.forEach(System.out::println);

        } else {
            System.out.println("❌ [TEST 5] Error " + result5.getCode() + ": " + result5.getMessage());
        }

        //TEST6
        ExpenseDTO dto6 = new ExpenseDTO();
        dto6.setAmount(900);
        dto6.setDate(LocalDate.of(2025, 7, 21));
        dto6.setInstallments(3);
        dto6.setDescription("Salida grupal con gastos compartidos");

// Pagadores
        Map<Integer, Double> payers6 = new HashMap<>();
        payers6.put(1, 600.0); // Maxi
        payers6.put(2, 300.0); // Juan
        dto6.setPayers(payers6);

// Deudores
        Map<Integer, Double> debtors6 = new HashMap<>();
        debtors6.put(21, 300.0); // Sofi
        debtors6.put(22, 300.0); // Carlos
        debtors6.put(2,  300.0); // Juan
        dto6.setDebtors(debtors6);

// Registrar gasto
        Response<Integer> response6 = service.registerExpense(dto6);
        if (response6.isSuccess()) {
            System.out.println("✅ [TEST 6] Gasto registrado con ID: " + response6.getData());
            List<Debt> debts6 = DebtDAO.getInstance().getByExpenseId(response6.getObj());
            System.out.println("📋 Deudas registradas para el gasto ID " + response6.getData() + " (" + debts6.size() + " en total):");
            for (Debt d : debts6) System.out.println(d);
        } else {
            System.out.println("❌ [TEST 6] " + response6.getCode() + ": " + response6.getMessage());
        }







    }
}
