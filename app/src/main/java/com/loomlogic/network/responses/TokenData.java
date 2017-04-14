package com.loomlogic.network.responses;

import com.google.gson.annotations.SerializedName;

/**
 * Created on 30.09.2016.
 */
public class TokenData
{
    @SerializedName("access_token")
    private String accessToken;

    @SerializedName("expires_in")
    private String expiresIn;

    @SerializedName("refresh_token")
    private String refreshToken;


    public String getAccessToken()
    {
        return accessToken;
    }

    public String getExpiresIn()
    {
        return expiresIn;
    }

    public String getRefreshToken()
    {
        return refreshToken;
    }

    public void applyTokenData(TokenData other){
        this.accessToken = other.accessToken;
        this.expiresIn = other.expiresIn;
        this.refreshToken = other.refreshToken;
    }

    public void setAccessToken(String accessToken) {
        this.accessToken = accessToken;
    }

    @Override
    public String toString()
    {
        return "TokenData{" +
                "accessToken='" + accessToken + '\'' +
                ", expiresIn='" + expiresIn + '\'' +
                ", refreshToken='" + refreshToken + '\'' +
                '}';
    }
}
