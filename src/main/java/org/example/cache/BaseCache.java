package org.example.cache;

import org.example.model.Response;

public abstract class BaseCache<T> {
    public abstract Response<T> findById(int id);
    public abstract Response<T> clearCache();

    }
