package org.example.dao;

import org.example.model.Response;

import java.sql.Connection;

public abstract class BaseDAO<T> implements ICrud<T> {

    protected Connection conn;

    public BaseDAO(Connection conn) {
        this.conn = conn;
    }

    protected BaseDAO() {
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
