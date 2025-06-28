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

            Response<Payment> response = PaymentDAO.getInstance().create(payment);
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

    public Response<Void> editPayment(PaymentDTO dto) {
        if (dto.getId() <= 0) {
            return new Response<>(false, "400", "El ID del pago no es válido.");
        }
        if (dto.getAmount() <= 0) {
            return new Response<>(false, "400", "El monto debe ser mayor a cero.");
        }
        if (dto.getDate() == null || dto.getDate().isAfter(LocalDate.now())) {
            return new Response<>(false, "400", "La fecha es inválida o futura.");
        }
        if (dto.getPayerId() == dto.getRecipientId()) {
            return new Response<>(false, "400", "El pagador no puede ser el mismo que el receptor.");
        }
        try {
            // Buscar el pago original por ID
            Response<Payment> paymentResponse = PaymentDAO.getInstance().read(dto.getId());
            if (!paymentResponse.isSuccess()) {
                return new Response<>(false, "404", "El pago no existe en la base de datos.");
            }

            // Validar existencia de payer y recipient
            Response<User> payerResp = UserDAO.getInstance().read(dto.getPayerId());
            Response<User> recipientResp = UserDAO.getInstance().read(dto.getRecipientId());
            if (!payerResp.isSuccess()) {
                return new Response<>(false, "404", "El pagador no existe en la base de datos.");
            }
            if (!recipientResp.isSuccess()) {
                return new Response<>(false, "404", "El receptor no existe en la base de datos.");
            }

            // Construir el objeto actualizado
            Payment updated = new Payment(
                    dto.getId(),
                    dto.getAmount(),
                    dto.getDate(),
                    payerResp.getObj(),
                    recipientResp.getObj()
            );

            // Ejecutar la actualización en la base de datos con el DAO
            Response<Payment> updateResponse = PaymentDAO.getInstance().update(updated);//Le paso el actualizado
            return new Response<>(updateResponse.isSuccess(), updateResponse.getCode(), updateResponse.getMessage());//Sube las burbujas del DAO. Me devuelve el mismo response que su metodo update.

        } catch (Exception e) {
            return new Response<>(false, "500", "Error inesperado al editar el pago: " + e.getMessage());
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
