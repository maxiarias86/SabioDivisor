package org.example.cache;

import org.example.dao.PaymentDAO;
import org.example.dto.UserDTO;
import org.example.model.Payment;
import org.example.model.Response;

import java.util.ArrayList;
import java.util.List;

public class PaymentCache {

    private static PaymentCache instance;
    private ArrayList<Payment> payments;

    private PaymentCache(UserDTO userDTO) {
        this.payments = (ArrayList<Payment>) PaymentDAO.getInstance().readAllByUser(userDTO).getData();//Inicializa la lista de pagos con los datos del DAO
    }

    public static PaymentCache getInstance(UserDTO userDTO) {
        if (instance == null) {
            instance = new PaymentCache(userDTO);
        }
        return instance;
    }

    public List<Payment> getOtherUserPayments(UserDTO friend) {// Obtiene los pagos entre el usuario actual y un amigo
        List<Payment> otherUserPayments = new ArrayList<>();// Inicializa una lista para almacenar los pagos entre el usuario actual y el amigo
         // Itera sobre la lista de pagos y agrega aquellos que son entre el usuario actual y el amigo
        for (Payment payment : payments) {
            if (payment.getPayer().getId() == friend.getId() || payment.getRecipient().getId() == friend.getId()) {// Verifica si el pago es entre el usuario actual y el amigo
                    otherUserPayments.add(payment);
            }
        }
        return otherUserPayments;
    }

    public ArrayList<Payment> getPayments() {
        return payments;
    }

    public void addPayment(Payment payment) {
        this.payments.add(payment);
    }

    public Response<Payment> getPaymentById(int paymentId) {
        for (Payment payment : payments) {
            if (payment.getId() == paymentId) {
                return new Response(true,"200", "Pago encontrado", payment);
            }
        }
        return new Response(false, "404", "Pago no encontrado");
    }

    public Response<Payment> editPayment(Payment payment) {
        for (int i = 0; i < payments.size(); i++) {
            if (payments.get(i).getId() == payment.getId()) {
                payments.set(i, payment);
                return new Response<>(true, "200", "Pago editado exitosamente", payment);
            }
        }
        return new Response<>(false, "404", "Pago no encontrado");
    }

    // Metodo para borrar cache al hacer el logout.
    public static void reset() {
        instance = null;
    }
}