package org.example.dao;

import org.example.model.Response;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public abstract class BaseDAO<T> implements ICrud<T> {

    protected Connection conn;

    public BaseDAO() {
        try {
            conn = DriverManager.getConnection(
                    "jdbc:mysql://localhost:3306/sabio_divisor", "root", "");
        } catch (SQLException e) {
            throw new RuntimeException("No se pudo conectar a la base de datos", e);        }
    }

    @Override
    public abstract Response<T> create(T entity);

    @Override
    public abstract Response<T> read(int id);

    @Override
    public abstract Response<T> update(T entity);

    @Override
    public abstract Response<T> delete(int id);

    @Override
    public abstract Response<T> readAll();
}
