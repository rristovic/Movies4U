package com.runit.moviesmvvmmockup.data.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by Radovan Ristovic on 3/29/2018.
 * Quantox.com
 * radovanr995@gmail.com
 */

public class Account {
    @Expose
    @SerializedName("id")
    private long id = -1;
    @Expose
    @SerializedName("name")
    private String name;
    @Expose
    @SerializedName("username")
    private String username;

    public long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getUsername() {
        return username;
    }

    public Account(long id, String name, String username) {
        this.id = id;
        this.name = name;
        this.username = username;
    }
}
