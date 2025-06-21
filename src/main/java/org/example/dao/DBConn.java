package org.example.dao;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DBConn {
    //Agregué ?useSSL=false&serverTimezone=UTC
    private final String url = "jdbc:mysql://localhost:3306/sabio_divisor?useSSL=false&serverTimezone=UTC";
    private final String user = "root";
    private final String password = "";

    private Connection conn;

    private static DBConn instance;

    private DBConn() throws SQLException {
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            this.conn = DriverManager.getConnection(url, user, password);

        }catch(ClassNotFoundException e){
            System.out.println("Error al cargar el driver de MYSQL: " + e.getMessage());
        }catch(SQLException e){
            System.out.println("Error al intentar generar la conexion: " + e.getMessage());
        }catch(Exception e){
            System.out.println("Error : " + e.getMessage());
        }
    }

    public Connection getConnection() {
        return this.conn;
    }

    public static DBConn getInstance() throws SQLException {
        if (instance == null || instance.getConnection().isClosed()) {
            instance = new DBConn();
        }
        return instance;
    }
}
