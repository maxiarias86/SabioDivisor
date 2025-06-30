package org.example.service;

import org.example.cache.DebtCache;
import org.example.cache.ExpenseCache;
import org.example.cache.PaymentCache;
import org.example.cache.UserCache;
import org.example.dto.UserDTO;
import org.example.model.Response;
import org.example.model.User;

import java.util.List;
import java.util.Scanner;

public class LoginService {

    public User login(List<User> usuarios) {
        Scanner scanner = new Scanner(System.in);
        User usuarioLogueado = null;

        while (usuarioLogueado == null) {
            System.out.print("Ingrese su email: ");
            String email = scanner.nextLine();

            System.out.print("Ingrese su contraseña: ");
            String password = scanner.nextLine();

            for (User u : usuarios) {
                if (u.getEmail().equalsIgnoreCase(email) && u.getPassword().equals(password)) {
                    usuarioLogueado = u;
                    break;
                }
            }

            if (usuarioLogueado == null) {
                System.out.println("Email o contraseña incorrectos. Intente nuevamente.\n");
            }
        }

        System.out.println("\nBienvenido, " + usuarioLogueado.getName() + "\n");
        return usuarioLogueado;
    }

    public UserDTO validarCredenciales(List<User> usuarios, String email, String password) {
        for (User u : usuarios) {
            if (u.getEmail().equalsIgnoreCase(email) && u.getPassword().equals(password)) {
                // Si las credenciales son correctas, retornamos el dto del usuario. Para evitar pasar el objeto User completo.
                UserService userService = new UserService();
                UserDTO userDTO = userService.convertToDTO(u);
                return userDTO;
            }
        }
        return null;
    }

    public Response logout(UserDTO user) {
        if (user == null) {
            return new Response(false, "400","No hay usuario logueado.");
        }
        UserCache userCache = UserCache.getInstance();
        userCache.reset();
        PaymentCache paymentCache = PaymentCache.getInstance(user);
        paymentCache.reset();
        DebtCache debtCache = DebtCache.getInstance(user);
        debtCache.reset();
        ExpenseCache expenseCache = ExpenseCache.getInstance(user);
        expenseCache.reset();
        return new Response(true, "200","Sesión cerrada correctamente.");
    }

}
