package org.example.service;

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

    public User validarCredenciales(List<User> usuarios, String email, String password) {
        for (User u : usuarios) {
            if (u.getEmail().equalsIgnoreCase(email) && u.getPassword().equals(password)) {
                return u;
            }
        }
        return null;
    }

}
