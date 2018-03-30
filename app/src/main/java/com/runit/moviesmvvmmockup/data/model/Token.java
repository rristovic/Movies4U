package com.runit.moviesmvvmmockup.data.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by Radovan Ristovic on 3/29/2018.
 * Quantox.com
 * radovanr995@gmail.com
 */

public class Token {
    @Expose
    @SerializedName("success")
    private boolean success;
    @Expose
    @SerializedName("expires_at")
    private String expires_at;
    @Expose
    @SerializedName("request_token")
    private String request_token;

    public Token(boolean success, String expires_at, String request_token) {
        this.success = success;
        this.expires_at = expires_at;
        this.request_token = request_token;
    }

    public boolean isSuccess() {
        return success;
    }

    public String getExpires_at() {
        return expires_at;
    }

    public String getRequest_token() {
        return request_token;
    }
}
