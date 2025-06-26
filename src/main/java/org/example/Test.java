package org.example;

import org.example.dao.UserDAO;
import org.example.model.Response;
import org.example.model.User;
import org.example.service.UserService;

import java.util.ArrayList;
import java.util.List;

public class Test {
    public static void main(String[] args) {
        System.out.println("=== PRUEBA: UserService.getAllUsersButOne(1) ===");

        int idAExcluir = 1;
        UserService userService = new UserService();
        Response response = userService.getAllUsersButOne(idAExcluir);

        if (response.isSuccess()) {
            List<User> lista = response.getData();
            if (lista == null || lista.isEmpty()) {
                System.out.println("No hay otros usuarios.");
            } else {
                for (User user : lista) {
                    System.out.println("ID: " + user.getId() + " | Nombre: " + user.getName() + " | Email: " + user.getEmail());
                }
            }
        } else {
            System.out.println("Error: " + response.getCode() + " - " + response.getMessage());
        }
    }
}
