package com.runit.moviesmvvmmockup.data.model;


import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class MovieAccountState {
    @SerializedName("id")
    @Expose
    private Long id;
    @SerializedName("favorite")
    @Expose
    private Boolean favorite;
    @SerializedName("rated")
    @Expose
    private Object rated;
    @SerializedName("watchlist")
    @Expose
    private Boolean watchlist;

    public MovieAccountState(Long id, Boolean favorite, Object rated, Boolean watchlist) {
        this.id = id;
        this.favorite = favorite;
        this.rated = rated;
        this.watchlist = watchlist;
    }
}
