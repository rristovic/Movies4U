package com.runit.moviesmvvmmockup.data.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by Radovan Ristovic on 3/29/2018.
 * Quantox.com
 * radovanr995@gmail.com
 */

public class ServerResponse<T> {
    @Expose
    @SerializedName("results")
    public List<T> results;
    @Expose
    @SerializedName("page")
    public int page;
    @Expose
    @SerializedName("total_pages")
    public int total_pages;
    @Expose
    @SerializedName("total_results")
    public int total_results;
}
