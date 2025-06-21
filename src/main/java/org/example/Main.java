package org.example;

import org.example.controller.PaymentDTO;
import org.example.model.Response;
import org.example.service.PaymentService;

import java.time.LocalDate;

public class Main {
    public static void main(String[] args) {
        PaymentService paymentService = new PaymentService();

        System.out.println("🔍 [TEST 1] Pago válido");
        PaymentDTO pago1 = new PaymentDTO(LocalDate.now().minusDays(2), 1, 2, 150.0);
        Response<Integer> r1 = paymentService.registerPayment(pago1);
        mostrar(r1);

        System.out.println("🔍 [TEST 2] Mismo usuario");
        PaymentDTO pago2 = new PaymentDTO(LocalDate.now(), 1, 1, 100.0);
        Response<Integer> r2 = paymentService.registerPayment(pago2);
        mostrar(r2);

        System.out.println("🔍 [TEST 3] Usuario inexistente");
        PaymentDTO pago3 = new PaymentDTO(LocalDate.now(), 1, 999, 100.0);
        Response<Integer> r3 = paymentService.registerPayment(pago3);
        mostrar(r3);

        System.out.println("🔍 [TEST 4] Monto inválido");
        PaymentDTO pago4 = new PaymentDTO(LocalDate.now(), 1, 2, -50.0);
        Response<Integer> r4 = paymentService.registerPayment(pago4);
        mostrar(r4);

        System.out.println("🔍 [TEST 5] Fecha futura");
        PaymentDTO pago5 = new PaymentDTO(LocalDate.now().plusDays(1), 1, 2, 100.0);
        Response<Integer> r5 = paymentService.registerPayment(pago5);
        mostrar(r5);
    }

    private static void mostrar(Response<Integer> response) {
        if (response.isSuccess()) {
            System.out.println("✅ Pago registrado con ID: " + response.getObj());
        } else {
            System.out.println("❌ Error " + response.getCode() + ": " + response.getMessage());
        }
    }
}
