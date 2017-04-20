package com.loomlogic.network.responses;

public class ResponseDataWrapper<T> {
    public T data;
    public String alert;
    public String[] errors;
    public boolean success;
}
