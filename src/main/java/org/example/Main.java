package org.example;

/*
        LoginJFrame login = new LoginJFrame();
        login.setVisible(true);
                UserCache userCache = UserCache.getInstance();
        for (User user : userCache.getAllUsers().values()) {
            System.out.println(user.getName());
        }

        */


import org.example.dao.DebtDAO;
import org.example.dao.UserDAO;
import org.example.model.Debt;
import org.example.model.Response;
import org.example.model.User;

public class Main {
    public static void main(String[] args) {
        DebtDAO debtDAO = DebtDAO.getInstance();
        UserDAO userDAO = UserDAO.getInstance();
        User user = userDAO.read(1).getObj();
        Response<Debt> response = debtDAO.readAllByUser(user);
        if (response.isSuccess()) {
            for (Debt debt : response.getData()){
                System.out.println(debt);
            }
        } else {
            System.out.println("Error" + response.getMessage());
        }


    }
}
