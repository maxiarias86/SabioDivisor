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
    }
}
