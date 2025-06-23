package org.example;

/*
        LoginJFrame login = new LoginJFrame();
        login.setVisible(true);
                UserCache userCache = UserCache.getInstance();
        for (User user : userCache.getAllUsers().values()) {
            System.out.println(user.getName());
        }

        */


import org.example.cache.BillCache;
import org.example.dao.DebtDAO;
import org.example.dao.PaymentDAO;
import org.example.dao.UserDAO;
import org.example.model.Debt;
import org.example.model.Payment;
import org.example.model.Response;
import org.example.model.User;

public class Main {
    public static void main(String[] args) {
        PaymentDAO paymentDAO = PaymentDAO.getInstance();
        DebtDAO debtDAO = DebtDAO.getInstance();
        UserDAO userDAO = UserDAO.getInstance();
        User user = userDAO.read(1).getObj();

        Response<Payment> paymentResponse = paymentDAO.readAllByUser(user);
        if (paymentResponse.isSuccess()) {
            for (Payment pay : paymentResponse.getData()) {
                System.out.println(pay);
            }
        } else {
            System.out.println("Error" + paymentResponse.getMessage());
        }

        Response<Debt> response = debtDAO.readAllByUser(user);
        if (response.isSuccess()) {
            for (Debt debt : response.getData()){
                System.out.println(debt);
            }
        } else {
            System.out.println("Error" + response.getMessage());
        }
        BillCache billCache = BillCache.getInstance();
       // billCache.createBillCache(user,response.getData(),paymentResponse.getData());



    }
}
