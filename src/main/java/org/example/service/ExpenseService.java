package org.example.service;

import org.example.cache.UserCache;
import org.example.dao.ExpenseDAO;
import org.example.controller.ExpenseDTO;
import org.example.model.Debt;
import org.example.model.User;
import org.example.model.Response;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class ExpenseService {

    public Response<Integer> registerExpense(ExpenseDTO dto) {
        // Validación básica
        if (dto.getAmount() <= 0 || dto.getPayers().isEmpty() || dto.getDebtors().isEmpty()) {
            return new Response<>(false, "400", "Datos insuficientes para registrar el gasto.");
        }

        double totalPagado = dto.getPayers().values().stream().mapToDouble(Double::doubleValue).sum();
        if (Math.abs(totalPagado - dto.getAmount()) > 0.01) {
            return new Response<>(false, "422", "La suma de los pagos no coincide con el monto total.");
        }

        // Resolver UserCache
        UserCache cache = UserCache.getInstance();
        Map<Integer, Double> payerMap = dto.getPayers();
        Map<Integer, Double> debtorMap = dto.getDebtors();

        List<Debt> debts = new ArrayList<>();

        for (Map.Entry<Integer, Double> debtorEntry : debtorMap.entrySet()) {
            int debtorId = debtorEntry.getKey();
            double deudaTotal = debtorEntry.getValue();
            User debtor = cache.getById(debtorId);
            if (debtor == null) continue;

            for (Map.Entry<Integer, Double> payerEntry : payerMap.entrySet()) {
                int payerId = payerEntry.getKey();
                double pagado = payerEntry.getValue();
                User payer = cache.getById(payerId);
                if (payer == null || payerId == debtorId) continue;

                double proporcion = pagado / totalPagado;
                double monto = deudaTotal * proporcion;

                try {
                    Debt deuda = new Debt(0, monto, debtor, payer, dto.getDate());
                    debts.add(deuda);
                } catch (IllegalArgumentException e) {
                    System.out.println("⚠️ No se pudo crear una deuda: " + e.getMessage());
                    // Opcional: podrías devolver un error general si no querés registrar parcialmente
                    return new Response<>(false, "400", "Error al generar deudas: " + e.getMessage());
                }
            }
        }

        if (debts.isEmpty()) {
            return new Response<>(false, "400", "No se generaron deudas válidas.");
        }

        return ExpenseDAO.getInstance().save(dto, debts);
    }
}
