package com.kt.http.base;


public interface IPostableItem {

    public String toJsonString();

    public String toXMLString();

    public String toPlainText();

    public String toUrlEncodedText();
}
