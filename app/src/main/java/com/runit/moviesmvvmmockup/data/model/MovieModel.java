package com.runit.moviesmvvmmockup.data.model;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.text.TextUtils;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by Radovan Ristovic on 3/30/2018.
 * Quantox.com
 * radovanr995@gmail.com
 */

public class MovieModel {
    @Expose
    @SerializedName("poster_path")
    private String thumbnailUrl;
    @Expose
    @SerializedName("title")
    private String title;

    /**
     * Return the movie thumbnail url.
     *
     * @return valid url or NULL otherwise.
     */
    public @Nullable
    String getThumbnailUrl() {
        return TextUtils.isEmpty(thumbnailUrl) ? null : "https://image.tmdb.org/t/p/w500" + thumbnailUrl;
    }

    public void setThumbnailUrl(String thumbnailUrl) {
        this.thumbnailUrl = thumbnailUrl;
    }


    public @NonNull
    String getTitle() {
        return title == null ? "" : title;
    }

    public void setTitle(String title) {
        this.title = title;
    }
}
