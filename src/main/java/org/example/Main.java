/*
        LoginJFrame login = new LoginJFrame();
        login.setVisible(true);
                UserCache userCache = UserCache.getInstance();
        for (User user : userCache.getAllUsers().values()) {
            System.out.println(user.getName());
        }

        */

package org.example;
import org.example.cache.BillCache;
import org.example.cache.UserCache;
import org.example.dao.DebtDAO;
import org.example.dto.BillDTO;
import org.example.model.Debt;
import org.example.model.Response;
import org.example.model.User;

import java.time.LocalDate;
import java.util.List;

public class Main {
    public static void main(String[] args) {
        UserCache userCache = UserCache.getInstance();
        User ana = userCache.getById(28);

        if (ana == null) {
            System.out.println("❌ No se encontró a Ana Torres");
            return;
        }

        Response<Debt> debtResp = DebtDAO.getInstance().readAll();
        if (!debtResp.isSuccess()) {
            System.out.println("❌ No se pudieron leer deudas: " + debtResp.getMessage());
            return;
        }

        BillCache billCache = BillCache.getInstance();
        billCache.createBillCache(ana, debtResp.getData());

        System.out.println("📋 Estado de cuenta de " + ana.getName() + ":");
        for (BillDTO bill : billCache.getAllBills()) {
            User otroUsuario = userCache.getById(bill.getOtherUser());
            String nombreOtro = otroUsuario != null ? otroUsuario.getName() : "Usuario #" + bill.getOtherUser();
            String direccion = bill.getAmount() > 0 ? "te debe" : "le debés a";

            System.out.printf("🧾 %s | %s | %s %s: $%.2f\n",
                    bill.getDueDate(),
                    bill.getDescription(),
                    direccion,
                    nombreOtro,
                    Math.abs(bill.getAmount())
            );
        }

        double saldo = billCache.accountUpToDate(LocalDate.now());
        System.out.printf("\n💰 Balance actual hasta hoy: %s%.2f\n", saldo >= 0 ? "+" : "", saldo);
    }
}
