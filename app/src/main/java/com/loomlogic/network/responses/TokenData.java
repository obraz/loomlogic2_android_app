package com.loomlogic.network.responses;

public class TokenData {
    private String accessToken;
    private String client;
    private String uid;

    public String getAccessToken() {
        return accessToken;
    }

    public void setAccessToken(String accessToken) {
        this.accessToken = accessToken;
    }

    public String getClient() {
        return client;
    }

    public void setClient(String client) {
        this.client = client;
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    @Override
    public String toString() {
        return "TokenData{" +
                "accessToken='" + accessToken + '\'' +
                ", client='" + client + '\'' +
                ", uid='" + uid + '\'' +
                '}';
    }
}
