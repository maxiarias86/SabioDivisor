package org.example.service;

import org.example.dto.PaymentDTO;
import org.example.dao.PaymentDAO;
import org.example.dao.UserDAO;
import org.example.model.Payment;
import org.example.model.Response;
import org.example.model.User;

import java.time.LocalDate;

public class PaymentService {

    public Response<Integer> registerPayment(PaymentDTO dto) {
        // Validación de monto
        if (dto.getAmount() <= 0) {
            return new Response<>(false, "400", "El monto debe ser mayor a cero.");
        }

        // Validación de fecha
        if (dto.getDate() == null || dto.getDate().isAfter(LocalDate.now())) {
            return new Response<>(false, "400", "La fecha es inválida o está en el futuro.");
        }

        // Validación de usuarios distintos
        if (dto.getPayerId() == dto.getPayeeId()) {
            return new Response<>(false, "400", "El pagador y el receptor deben ser distintos.");
        }

        // Validación de existencia de usuarios
        Response<User> payerResp = UserDAO.getInstance().read(dto.getPayerId());
        Response<User> payeeResp = UserDAO.getInstance().read(dto.getPayeeId());

        if (!payerResp.isSuccess()) {
            return new Response<>(false, "404", "El usuario pagador no existe.");
        }
        if (!payeeResp.isSuccess()) {
            return new Response<>(false, "404", "El usuario receptor no existe.");
        }

        // Crear y guardar el pago
        Payment payment = new Payment(
                0,
                dto.getAmount(),
                dto.getDate(),
                payerResp.getObj(),
                payeeResp.getObj()
        );

        Response<Payment> response = PaymentDAO.getInstance().create(payment);

        if (response.isSuccess()) {
            return new Response<>(true, "201", "Pago registrado exitosamente.", response.getObj().getId());
        } else {
            return new Response<>(false, response.getCode(), response.getMessage());
        }
    }
}
