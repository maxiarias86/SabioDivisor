package org.example;

/*
        LoginJFrame login = new LoginJFrame();
        login.setVisible(true);
                UserCache userCache = UserCache.getInstance();
        for (User user : userCache.getAllUsers().values()) {
            System.out.println(user.getName());
        }

        */


import org.example.dao.PaymentDAO;
import org.example.dto.PaymentDTO;
import org.example.model.Payment;
import org.example.model.Response;
import org.example.service.PaymentService;

import java.time.LocalDate;

public class Test {
    public static void main(String[] args) {
        PaymentService service = new PaymentService();

        // Paso 1: Crear un PaymentDTO (asumimos que los usuarios con ID 1 y 2 existen)
        PaymentDTO dto = new PaymentDTO(LocalDate.now(), 1, 2, 100.0);

        // Paso 2: Registrar el pago
        Response<Integer> creationResponse = service.registerPayment(dto);
        if (!creationResponse.isSuccess()) {
            System.out.println("❌ Error al registrar el pago: " + creationResponse.getMessage());
            return;
        }
        int paymentId = creationResponse.getObj();
        System.out.println("✅ Pago creado con ID: " + paymentId);

        // Paso 3: Leer y mostrar el pago
        Response<Payment> readResponse = PaymentDAO.getInstance().read(paymentId);
        if (readResponse.isSuccess()) {
            System.out.println("📄 Detalle del pago creado:");
            System.out.println(readResponse.getObj());
        } else {
            System.out.println("❌ No se pudo leer el pago: " + readResponse.getMessage());
        }

        // Paso 4: Eliminar el pago
        Response deleteResponse = service.deletePayment(paymentId);
        if (deleteResponse.isSuccess()) {
            System.out.println("🗑️ Pago eliminado correctamente.");
        } else {
            System.out.println("❌ Error al eliminar el pago: " + deleteResponse.getMessage());
        }

        // Paso 5: Intentar leerlo nuevamente
        Response<Payment> postDeleteRead = PaymentDAO.getInstance().read(paymentId);
        if (!postDeleteRead.isSuccess()) {
            System.out.println("✔️ Confirmación: el pago ya no existe.");
        } else {
            System.out.println("⚠️ El pago todavía existe (algo falló): " + postDeleteRead.getObj());
        }
    }
}
