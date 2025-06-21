package org.example;

import org.example.cache.BillCache;
import org.example.dto.BillDTO;
import org.example.model.Debt;
import org.example.model.Expense;
import org.example.model.User;
import org.example.service.LoginService;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        User maxi = new User("Maxi", "maxi@email.com", "1234");
        maxi.setId(1);
        User juan = new User("Juan", "juan@email.com", "1234");
        juan.setId(2);
        User lucia = new User("Lucia", "lucia@email.com", "abcd");
        lucia.setId(3);
        User pedro = new User("Pedro", "pedro@email.com", "pass");
        pedro.setId(4);

        System.out.println("Usuarios disponibles para login:");
        System.out.println("- Maxi | maxi@email.com | 1234");
        System.out.println("- Juan | juan@email.com | 1234");
        System.out.println("- Lucia | lucia@email.com | abcd");
        System.out.println("- Pedro | pedro@email.com | pass\n");

        List<User> usuarios = new ArrayList<>();
        usuarios.add(maxi);
        usuarios.add(juan);
        usuarios.add(lucia);
        usuarios.add(pedro);

        Scanner scanner = new Scanner(System.in);
        LoginService loginService = new LoginService();

        boolean continuar = true;
        while (continuar) {
            User usuarioLogueado = loginService.login(usuarios);

            Expense almuerzo = new Expense();
            almuerzo.setId(100);
            almuerzo.setDescription("Almuerzo en el parque");

            List<Debt> allDebts = new ArrayList<>();
            allDebts.add(new Debt(1, 1000, maxi, juan, almuerzo, LocalDate.of(2025, 6, 15), 1));
            allDebts.add(new Debt(2, 1500, juan, maxi, almuerzo, LocalDate.of(2025, 6, 25), 1));
            allDebts.add(new Debt(3, 500, maxi, juan, almuerzo, LocalDate.of(2025, 6, 20), 1));

            BillCache.getInstance().createBillCache(usuarioLogueado, allDebts);

            boolean sesionActiva = true;
            while (sesionActiva) {
                System.out.println("\n===== MENÚ =====");
                System.out.println("1. Ver todas las Bills");
                System.out.println("2. Ver estado de cuenta a hoy");
                System.out.println("3. Ver deudas vencidas a hoy");
                System.out.println("4. Ver deudas con Juan vencidas a hoy");
                System.out.println("5. Registrar gasto simulado");
                System.out.println("6. Cerrar sesión");
                System.out.println("7. Salir del programa");
                System.out.print("Seleccione una opción: ");
                String opcion = scanner.nextLine().trim();

                switch (opcion) {
                    case "1":
                        System.out.println("\n🔎 Todas las Bills de " + usuarioLogueado.getName() + ":");
                        for (BillDTO bill : BillCache.getInstance().getAllBills()) {
                            System.out.printf("- [%d] %s | %s | $%.2f | Vence: %s%n",
                                    bill.getId(), bill.getOtherUser(), bill.getDescription(),
                                    bill.getAmount(), bill.getDueDate()
                            );
                        }
                        break;
                    case "2":
                        LocalDate hoy = LocalDate.now();
                        double saldo = BillCache.getInstance().accountUpToDate(hoy);
                        System.out.println("\n💰 Estado de cuenta a " + hoy + ": $" + saldo);
                        break;
                    case "3":
                        hoy = LocalDate.now();
                        System.out.println("\n📅 Deudas vencidas hasta " + hoy + ":");
                        for (BillDTO bill : BillCache.getInstance().billsUpToDate(hoy)) {
                            System.out.println("- " + bill.getOtherUser() + ": $" + bill.getAmount());
                        }
                        break;
                    case "4":
                        hoy = LocalDate.now();
                        System.out.println("\n📌 Deudas con Juan vencidas hasta " + hoy + ":");
                        for (BillDTO bill : BillCache.getInstance().billsUpToDateByUser(hoy, "Juan")) {
                            System.out.println("- " + bill.getDescription() + ": $" + bill.getAmount());
                        }
                        break;
                    case "5":
                        System.out.println("\n➕ Registrar nuevo gasto simulado");
                        System.out.print("Descripción del gasto: ");
                        String desc = scanner.nextLine();
                        System.out.print("Monto total: ");
                        double montoTotal = Double.parseDouble(scanner.nextLine());
                        System.out.print("Cantidad de cuotas: ");
                        int cuotas = Integer.parseInt(scanner.nextLine());

                        // Elegir pagadores
                        List<User> pagadores = new ArrayList<>();
                        System.out.println("Seleccione quién(es) pagó/pagaron (ingrese IDs separados por coma):");
                        for (User u : usuarios) {
                            System.out.println("- " + u.getId() + ": " + u.getName());
                        }
                        String[] idsPagadores = scanner.nextLine().split(",");
                        for (String id : idsPagadores) {
                            int pid = Integer.parseInt(id.trim());
                            for (User u : usuarios) {
                                if (u.getId() == pid) pagadores.add(u);
                            }
                        }

                        // Elegir deudores
                        List<User> deudores = new ArrayList<>();
                        System.out.println("Seleccione quién(es) deben pagar el gasto (ingrese IDs separados por coma):");
                        for (User u : usuarios) {
                            System.out.println("- " + u.getId() + ": " + u.getName());
                        }
                        String[] idsDeudores = scanner.nextLine().split(",");
                        for (String id : idsDeudores) {
                            int did = Integer.parseInt(id.trim());
                            for (User u : usuarios) {
                                if (u.getId() == did) deudores.add(u);
                            }
                        }

                        double montoPorDeudor = montoTotal / deudores.size();
                        double montoPorCuota = montoPorDeudor / cuotas;
                        LocalDate fechaBase = LocalDate.now();

                        Expense nuevo = new Expense();
                        nuevo.setId(999); // ID ficticio
                        nuevo.setDescription(desc);

                        int nextDebtId = 1000;
                        for (User deudor : deudores) {
                            for (User pagador : pagadores) {
                                for (int i = 1; i <= cuotas; i++) {
                                    LocalDate vencimiento = fechaBase.plusMonths(i - 1);
                                    allDebts.add(new Debt(nextDebtId++, montoPorCuota, deudor, pagador, nuevo, vencimiento, i));
                                }
                            }
                        }

                        BillCache.getInstance().createBillCache(usuarioLogueado, allDebts);
                        System.out.println("✅ Gasto registrado correctamente.");
                        break;
                    case "6":
                        sesionActiva = false;
                        BillCache.getInstance().clearCache();
                        System.out.println("\n🔒 Sesión cerrada.\n");
                        break;
                    case "7":
                        sesionActiva = false;
                        continuar = false;
                        BillCache.getInstance().clearCache();
                        System.out.println("\n👋 Sesión finalizada. Hasta luego.");
                        break;
                    default:
                        System.out.println("❌ Opción no válida. Intente nuevamente.");
                }
            }
        }
    }
}
