package com.runit.moviesmvvmmockup.data.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by Radovan Ristovic on 3/29/2018.
 * Quantox.com
 * radovanr995@gmail.com
 */

public class Session {
    @Expose
    @SerializedName("success")
    private boolean success;
    @Expose
    @SerializedName("session_id")
    private String session_id;

    public boolean isSuccess() {
        return success;
    }

    public String session() {
        return session_id;
    }

    public Session(boolean success, String session_id) {
        this.success = success;
        this.session_id = session_id;
    }
}
