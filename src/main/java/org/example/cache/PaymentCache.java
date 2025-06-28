package org.example.cache;

/*
Creo un cache de Payments para no tener que estar consultando la BBDD nuevamente para ver los que son a favor
o en con saldo negativo, tambien para no tener que volver a la BBDD cada vez que se cambia la fecha del balance.
 */

import org.example.dao.PaymentDAO;
import org.example.dto.UserDTO;
import org.example.model.Payment;
import org.example.model.Response;

import java.util.ArrayList;
import java.util.List;

public class PaymentCache {

    private static PaymentCache instance;
    private ArrayList<Payment> payments;

    // Aquí podrías tener una estructura para almacenar los pagos, por ejemplo, un Map
    // private final Map<Integer, Payment> paymentCache = new HashMap<>();

    private PaymentCache(UserDTO userDTO) {
        // Constructor privado para evitar instanciación externa
        this.payments = (ArrayList<Payment>) PaymentDAO.getInstance().readAllByUser(userDTO).getData();
    }

    public static PaymentCache getInstance(UserDTO userDTO) {
        if (instance == null) {
            instance = new PaymentCache(userDTO);
        }
        return instance;
    }

    public List<Payment> getOtherUserPayments(UserDTO friend) {
        List<Payment> otherUserPayments = new ArrayList<>();
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

    // Metodo para borrar cache al hacer el logout.
    public static void reset() {
        instance = null;
    }

}
