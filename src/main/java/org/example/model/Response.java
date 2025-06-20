package org.example.model;

import java.util.List;

public class Response<T> {

    private boolean success;
    private String code;
    private String message;
    private List<T> data;
    private T obj;

    public Response(boolean success, String code, String message) {
        this.success = success;
        this.code = code;
        this.message = message;
    }

    public Response(boolean success, String code, String message, List<T> data) {
        this.success = success;
        this.code = code;
        this.message = message;
        this.data = data;
    }

    public Response(boolean success, String code, String message, T obj) {
        this.success = success;
        this.code = code;
        this.message = message;
        this.obj = obj;
    }

    public boolean isSuccess() {
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public List<T> getData() {
        return data;
    }

    public void setData(List<T> data) {
        this.data = data;
    }

    public T getObj() {
        return obj;
    }

    public void setObj(T obj) {
        this.obj = obj;
    }
}
