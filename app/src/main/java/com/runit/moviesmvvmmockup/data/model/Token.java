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
    public boolean success;
    @Expose
    @SerializedName("expires_at")
    public String expires_at;
    @Expose
    @SerializedName("request_token")
    public String request_token;
}
