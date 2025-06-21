package org.example;

import org.example.controller.ExpenseDTO;
import org.example.dao.DebtDAO;
import org.example.model.Debt;
import org.example.model.Response;
import org.example.service.ExpenseService;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Main {
    public static void main(String[] args) {
        System.out.println("🔍 Probando método ExpenseService.registerExpense con múltiples pagadores y deudores en cuotas");

        // Armar el DTO del gasto
        ExpenseDTO dto = new ExpenseDTO();
        dto.setAmount(600.0); // Monto total del gasto
        dto.setDate(LocalDate.of(2025, 7, 21));
        dto.setInstallments(3);
        dto.setDescription("Gasto compartido en cuotas con múltiples usuarios");

        // Pagadores (por ID del usuario en caché): Maxi (1) paga 400, Juan (2) paga 200
        Map<Integer, Double> payers = new HashMap<>();
        payers.put(1, 400.0);  // Maxi
        payers.put(2, 200.0);  // Juan
        dto.setPayers(payers);

        // Deudores: Carlos (21), Sofi (22), y Juan (2), se reparten el gasto
        Map<Integer, Double> debtors = new HashMap<>();
        debtors.put(21, 200.0); // Carlos
        debtors.put(22, 200.0); // Sofi
        debtors.put(2, 200.0);  // Juan
        dto.setDebtors(debtors);

        // Ejecutar el servicio
        ExpenseService service = new ExpenseService();
        Response<Integer> response = service.registerExpense(dto);

        // Mostrar resultados
        if (response.isSuccess()) {
            Integer expenseId = response.getObj();
            System.out.println("✅ [TEST 8] Gasto registrado con ID: " + expenseId);

            List<Debt> debts = DebtDAO.getInstance().getByExpenseId(expenseId);
            System.out.println("📋 Deudas registradas para el gasto ID " + expenseId + " (" + debts.size() + " en total):");
            for (Debt d : debts) {
                System.out.println(d);
            }
        } else {
            System.out.println("❌ [TEST 8] Error " + response.getCode() + ": " + response.getMessage());
        }
    }
}
