package com.kt.http.base;

public interface IResponseItem {

    void initWithJSON(String theJSONString);

    void initWithXML(String theJSONString);

    void initWithText(String theJSONString);
}
