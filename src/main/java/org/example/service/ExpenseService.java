package org.example.service;

import org.example.cache.UserCache;
import org.example.dao.ExpenseDAO;
import org.example.dto.ExpenseDTO;
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
            return new Response<>(false, "404", "La suma de los pagos no coincide con el monto total.");
        }

        UserCache cache = UserCache.getInstance();
        Map<Integer, Double> payerMap = dto.getPayers();
        Map<Integer, Double> debtorMap = dto.getDebtors();

        List<Debt> debts = new ArrayList<>();
        int installments = Math.max(1, dto.getInstallments());

        for (Map.Entry<Integer, Double> payerEntry : payerMap.entrySet()) {
            int payerId = payerEntry.getKey();
            double pagado = payerEntry.getValue();
            User payer = cache.getById(payerId);
            if (payer == null) continue;

            for (Map.Entry<Integer, Double> debtorEntry : debtorMap.entrySet()) {
                int debtorId = debtorEntry.getKey();
                double deudaTotal = debtorEntry.getValue();
                User debtor = cache.getById(debtorId);
                if (debtor == null || debtorId == payerId) continue;

                double proporcion = pagado / totalPagado;
                double montoTotal = deudaTotal * proporcion;
                double montoCuota = montoTotal / installments;

                for (int i = 1; i <= installments; i++) {
                    LocalDate dueDate = dto.getDate().plusMonths(i);
                    Debt deuda = new Debt(0, montoCuota, payer, debtor, null, dueDate, i);
                    debts.add(deuda);
                }
            }
        }

        return ExpenseDAO.getInstance().save(dto, debts);
    }
}
