package org.example.service;

import org.example.cache.DebtCache;
import org.example.cache.ExpenseCache;
import org.example.cache.PaymentCache;
import org.example.cache.UserCache;
import org.example.dto.UserDTO;
import org.example.model.Response;
import org.example.model.User;

import java.util.List;

public class LoginService {


    public UserDTO validateCredentials(List<User> usuarios, String email, String password) {
        //String hashedPassword = BCrypt.withDefaults().hashToString(10, password.toCharArray());

        for (User u : usuarios) {
            if (u.getEmail().equalsIgnoreCase(email) && u.getPassword().equals(password)) {
// Cambiarlo para el final: if (u.getEmail().equalsIgnoreCase(email) && u.getPassword().equals(hashedPassword)) {
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
