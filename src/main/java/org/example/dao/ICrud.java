package org.example.dao;

import org.example.model.Response;

public interface ICrud<T> {

    Response<T> create(T entity);
    Response<T> read(int id);
    Response<T> update(T entity);
    Response<T> delete(int id);
    Response<T> readAll();
}
