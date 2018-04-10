package com.runit.moviesmvvmmockup.data.model.request;

import com.google.gson.annotations.SerializedName;

/**
 * Created by Radovan Ristovic on 4/5/2018.
 * Quantox.com
 * radovanr995@gmail.com
 */

public class MovieBookmarkRequest {
    @SerializedName("media_type")
    private String mediaType = "movie";
    @SerializedName("media_id")
    private long movieId;
    @SerializedName("watchlist")
    private boolean watchlist = true;

    public MovieBookmarkRequest(long movieId) {
        this.movieId = movieId;
    }
}
