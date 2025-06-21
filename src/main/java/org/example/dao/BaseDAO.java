package org.example.dao;

import org.example.model.Response;

import java.sql.Connection;
import java.sql.SQLException;

public abstract class BaseDAO<T> implements ICrud<T> {

    protected Connection conn;

    public BaseDAO(){
        try {
            this.conn = DBConn.getInstance().getConnection();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public abstract Response<T> create(T entity);

    @Override
    public abstract Response<T> read(int id);
    //En todos los read() debo recuperarlos con un getObj() para un solo objeto

    @Override
    public abstract Response<T> update(T entity);

    @Override
    public abstract Response<T> delete(int id);

    @Override
    public abstract Response<T> readAll();
    //En todos los readAll() debo recuperarlos con un getData() para listas
}
