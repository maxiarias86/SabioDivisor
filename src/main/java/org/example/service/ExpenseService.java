package org.example.service;

import org.example.cache.UserCache;
import org.example.dao.ExpenseDAO;
import org.example.dto.ExpenseDTO;
import org.example.model.Debt;
import org.example.model.User;
import org.example.model.Response;

import java.time.LocalDate;
import java.util.*;

public class ExpenseService {

    public Response<Integer> registerExpense(ExpenseDTO dto) {

        if (dto.getAmount() <= 0 || dto.getPayers().isEmpty() || dto.getDebtors().isEmpty()) {
            return new Response<>(false, "400", "Datos insuficientes para registrar el gasto.");
        }

        double totalPayed = 0;
        for (Double amount : dto.getPayers().values()) {//Obtiene todos los valores pagados.
            totalPayed += amount;
        }

        if (Math.abs(totalPayed - dto.getAmount()) > 0.01) {//Si la diferencia entre ambos es mayor a 0.01 tira error. Se deja 0.01 por errores de redondeo.
            return new Response<>(false, "404", "La suma de los pagos no coincide con el monto total.");
        }

        //Calcula balances individuales
        Map<Integer, Double> balanceMap = new HashMap<>();

        for (Map.Entry<Integer, Double> entry : dto.getPayers().entrySet()) {
            int userId = entry.getKey();
            double pagado = entry.getValue();
            balanceMap.put(userId, pagado);
        }

        for (Map.Entry<Integer, Double> entry : dto.getDebtors().entrySet()) {
            int userId = entry.getKey();
            double deuda = entry.getValue();
            double balanceActual = balanceMap.containsKey(userId) ? balanceMap.get(userId) : 0.0;
            double nuevoBalance = balanceActual - deuda;
            balanceMap.put(userId, nuevoBalance);//Reemplaza el valor previo
        }

        //Listas separadas
        List<User> creditors = new ArrayList<>();
        List<User> debtors = new ArrayList<>();
        UserCache cache = UserCache.getInstance();

        for (Map.Entry<Integer, Double> entry : balanceMap.entrySet()) {
            int userId = entry.getKey();
            double balance = entry.getValue();
            User user = cache.getById(userId);
            if (user == null) {
                return new Response<>(false, "404", "El usuario con ID " + userId + " no existe.");
            }
            if (balance == 0) continue;//Si pago justo lo saltea
            if (balance > 0) creditors.add(user);// Lo pone en una lista u otra de acuerdo a si debe o no
            else debtors.add(user);
        }

        List<Debt> debts = new ArrayList<>();
        int installments = dto.getInstallments();
        if (installments <= 0) {
            return new Response<>(false, "400", "El número de cuotas debe ser mayor o igual a 1.");
        }


        //Generar deudas
        for (User debtor : debtors) {//Primero un deudor...
            int debtorId = debtor.getId();
            double pendiente = -balanceMap.get(debtorId);//Como el saldo es negativo lo paso a positivo

            for (User creditor : creditors) {//Tomo el primero de los acreedores
                int creditorId = creditor.getId();
                double disponible = balanceMap.get(creditorId);//Saldo que le deben

                if (pendiente <= 0) break; // El deudor ya no debe nada, corto este for para que tome a otro deudor
                if (disponible <= 0) continue; // este acreedor ya cobró, paso a la proxima iteracion de este for
                //Dicen <=0 por un minimo error de redondeo que pueda llegar a haber.

                double monto = Math.min(pendiente, disponible);//El mínimo entre lo que el deudor aún debe y lo que el acreedor aún debe recibir

                double cuota = monto / installments;//Se divide por la cantidad de cuotas

                for (int i = 1; i <= installments; i++) {
                    debts.add(new Debt(0, cuota, creditor, debtor, 0, dto.getDate().plusMonths(i), i));
                    //El expenseId es 0 porque todavia no existe el Expense, se va a cargar en la capa DAO, en ExpenseDAO.save().
                    //El id de la Debt se genera automaticamente en la BBDD.
                }

                balanceMap.put(creditorId, disponible - monto);//Actualiza el balanceMap, pasa el ID y al disponible le resta el monto.
                pendiente -= monto;
            }
        }

        return ExpenseDAO.getInstance().save(dto, debts);
    }
}
