package org.example;

import java.sql.Connection;
import java.sql.DriverManager;

public class Main {
    public static void main(String[] args) {
        System.out.println("🚀 Iniciando prueba de conexión a MySQL...");

        // Parámetros de conexión — modificá según tu configuración
        String url = "jdbc:mysql://localhost:3306/sabio_divisor"; // nombre de tu base
        String user = "root";
        String password = "";

        try {
            // Opcional en JDBC 4.0+, pero útil si querés estar seguro
            Class.forName("com.mysql.cj.jdbc.Driver");

            Connection conn = DriverManager.getConnection(url, user, password);
            System.out.println("✅ Conexión exitosa a la base de datos.");
            conn.close();
        } catch (Exception e) {
            System.out.println("❌ Error al conectar:");
            e.printStackTrace();
        }
    }
}
