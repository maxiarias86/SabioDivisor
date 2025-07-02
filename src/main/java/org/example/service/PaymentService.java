package org.example.service;

import org.example.cache.PaymentCache;
import org.example.dto.PaymentDTO;
import org.example.dao.PaymentDAO;
import org.example.dao.UserDAO;
import org.example.dto.UserDTO;
import org.example.model.Payment;
import org.example.model.Response;
import org.example.model.User;

import java.time.LocalDate;

public class PaymentService {

    public Response<Payment> registerPayment(PaymentDTO dto) {//El response devuelve un Integer para pasar el ID del Payment creado o null.
        if (dto.getAmount() <= 0) {
            return new Response<>(false, "400", "El monto debe ser mayor a cero.");
        }
        if (dto.getDate() == null || dto.getDate().isAfter(LocalDate.now())) {
            return new Response<>(false, "400", "La fecha es inválida o futura.");
        }
        if (dto.getPayerId() == dto.getRecipientId()) {
            return new Response<>(false, "400", "El pagador no puede hacerse pagos a sí mismo.");
        }

        try{
            // Validación de existencia de usuarios
            Response<User> payerResp = UserDAO.getInstance().read(dto.getPayerId());
            Response<User> recipientResp = UserDAO.getInstance().read(dto.getRecipientId());
            if (!payerResp.isSuccess()) {
                return new Response<>(false, "404", "El pagador no existe en la base de datos.");
            }
            if (!recipientResp.isSuccess()) {
                return new Response<>(false, "404", "El receptor no existe en la base de datos.");
            }

            // Crear el pago. Sin id porque aún no lo sabe.
            Payment payment = new Payment(
                    0,
                    dto.getAmount(),
                    dto.getDate(),
                    payerResp.getObj(),
                    recipientResp.getObj()
            );

            Response<Payment> response = PaymentDAO.getInstance().create(payment);//Le paso el Payment sin ID, porque aún no lo tiene asignado.
            if (response.isSuccess()) {
                return new Response<>(true, "201", "Pago registrado exitosamente.", response.getObj());//Devuelve el objeto Payment creado con su ID asignado.
            } else {
                return new Response<>(false, response.getCode(), response.getMessage());
            }
        } catch (Exception e) {
            return new Response<>(false, "500", "Error en el servidor: " + e.getMessage());
        }

    }

    public Response loadToCache(Payment newPayment, UserDTO userDTO) {
        try {
            PaymentCache cache = PaymentCache.getInstance(userDTO);
            cache.addPayment(newPayment);
            return new Response<>(true, "200", "Pago cargado en el cache exitosamente.");
        }catch(Exception e) {
            return new Response<>(false, "500", "Error al cargar el pago en el cache: " + e.getMessage());
        }

    }

    public Response editPayment(PaymentDTO dto) {
        if (dto.getId() <= 0) {// Validación del ID del pago
            return new Response(false, "400", "El ID del pago no es válido.");
        }
        if (dto.getAmount() <= 0) {// Validación del monto
            return new Response(false, "400", "El monto debe ser mayor a cero.");
        }
        if (dto.getDate() == null || dto.getDate().isAfter(LocalDate.now())) {// Validación de la fecha
            return new Response(false, "400", "La fecha es inválida o futura.");
        }
        if (dto.getPayerId() == dto.getRecipientId()) {// Validación de que el pagador no sea el mismo que el receptor
            return new Response(false, "400", "El pagador no puede ser el mismo que el receptor.");
        }
        try {// Validación de que el ID del pagador y receptor existan
            Response<Payment> paymentResponse = PaymentDAO.getInstance().read(dto.getId());// Buscar el pago original por ID
            if (!paymentResponse.isSuccess()) {// Si no se encuentra el pago, devolver error
                return new Response(false, "404", "El pago no existe en la base de datos.");
            }

            // Validar existencia de payer y recipient
            Response<User> payerResp = UserDAO.getInstance().read(dto.getPayerId());// Buscar el pagador por ID
            Response<User> recipientResp = UserDAO.getInstance().read(dto.getRecipientId());// Buscar el receptor por ID
            if (!payerResp.isSuccess()) {// Si no se encuentra el pagador devuelve error
                return new Response(false, "404", "El pagador no existe en la base de datos.");
            }
            if (!recipientResp.isSuccess()) {// Si no se encuentra el receptor devuelve error
                return new Response(false, "404", "El receptor no existe en la base de datos.");
            }

            // Construye un pago con los datos nuevos y el ID del original
            Payment updated = new Payment(dto.getId(),dto.getAmount(),dto.getDate(),payerResp.getObj(),recipientResp.getObj());

            // Ejecuta la actualización en la base de datos con el DAO
            Response updateResponse = PaymentDAO.getInstance().update(updated);//Le paso el actualizado.
            if (!updateResponse.isSuccess()) {// Si la actualización falla, devolver error
                return new Response(false, updateResponse.getCode(), updateResponse.getMessage());//Me devuelve el mismo response que su metodo update.
            }
            // Si sale bien, devuelve el Payment actualizado
            return new Response(true, "200", "Payment actualizado en la base de datos", updated);//Devuelve true y el Payment.

        } catch (Exception e) {
            return new Response(false, "500", "Error inesperado al editar el pago: " + e.getMessage());
        }
    }

    public Response deletePayment(int id) {
        if (id <= 0) {
            return new Response<>(false, "400", "El ID del pago no es válido.");
        }
        try{
            Response<Payment> response = PaymentDAO.getInstance().delete(id);
            if (response.isSuccess()) {
                return new Response(true,"200", "Pago eliminado exitosamente.");
            } else {
                return new Response<>(false, "500", "No se pudo eliminar el pago");
            }
        }catch(Exception e){
            return new Response(false,"500", "Error inesperado al eliminar el pago: " + e.getMessage());
        }
    }
}