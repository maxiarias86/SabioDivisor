package org.example;


import org.example.cache.UserCache;
import org.example.model.User;

public class Main {
    public static void main(String[] args) {
        LoginJFrame login = new LoginJFrame();
        login.setVisible(true);
        UserCache userCache = UserCache.getInstance();
        //Poner un metodo que muestre por consola usuarios y credenciales

        for (User user : userCache.getAllUsers().values()) {
            System.out.println("\nUsuario: "+ user.getEmail()+"\nContraseña: "+ user.getPassword());
        }

    }
}
