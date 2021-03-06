package com.runit.moviesmvvmmockup.data.model;

import android.arch.persistence.room.Entity;
import android.arch.persistence.room.Ignore;
import android.arch.persistence.room.PrimaryKey;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.text.TextUtils;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;


@Entity
public class MovieModel {
    @SerializedName("adult")
    @Expose
    private Boolean adult;
    @SerializedName("backdrop_path")
    @Expose
    private String backdropPath;
    @SerializedName("belongs_to_collection")
    @Expose
    @Ignore
    private Object belongsToCollection;
    @SerializedName("budget")
    @Expose
    private Integer budget;
    @SerializedName("genres")
    @Expose
    @Ignore
    private List<Genre> genres = null;
    @SerializedName("homepage")
    @Expose
    private String homepage;
    @SerializedName("id")
    @Expose
    @PrimaryKey
    private Long id;
    @SerializedName("imdb_id")
    @Expose
    private String imdbId;
    @SerializedName("original_language")
    @Expose
    private String originalLanguage;
    @SerializedName("original_title")
    @Expose
    private String originalTitle;
    @SerializedName("overview")
    @Expose
    private String overview;
    @SerializedName("popularity")
    @Expose
    private Double popularity;
    @SerializedName("poster_path")
    @Expose
    private String posterPath;
    @SerializedName("release_date")
    @Expose
    private String releaseDate;
    @SerializedName("revenue")
    @Expose
    private Long revenue;
    @SerializedName("runtime")
    @Expose
    private Integer runtime;
    @SerializedName("status")
    @Expose
    private String status;
    @SerializedName("tagline")
    @Expose
    private String tagline;
    @SerializedName("title")
    @Expose
    private String title;
    @SerializedName("video")
    @Expose
    private Boolean video;
    @SerializedName("vote_average")
    @Expose
    private Float voteAverage;
    @SerializedName("vote_count")
    @Expose
    private Integer voteCount;

    public MovieModel(Boolean adult, String backdropPath, Integer budget, String homepage, Long id, String imdbId, String originalLanguage, String originalTitle, String overview, Double popularity, String posterPath, String releaseDate, Long revenue, Integer runtime, String status, String tagline, String title, Boolean video, Float voteAverage, Integer voteCount) {
        this.adult = adult;
        this.backdropPath = backdropPath;
        this.belongsToCollection = null;
        this.budget = budget;
        this.genres = null;
        this.homepage = homepage;
        this.id = id;
        this.imdbId = imdbId;
        this.originalLanguage = originalLanguage;
        this.originalTitle = originalTitle;
        this.overview = overview;
        this.popularity = popularity;
        this.posterPath = posterPath;
        this.releaseDate = releaseDate;
        this.revenue = revenue;
        this.runtime = runtime;
        this.status = status;
        this.tagline = tagline;
        this.title = title;
        this.video = video;
        this.voteAverage = voteAverage;
        this.voteCount = voteCount;
    }

    /**
     * Return the movie thumbnail url.
     *
     * @return valid url or NULL otherwise.
     */
    public @Nullable
    String getThumbnailUrl() {
        return TextUtils.isEmpty(posterPath) ? null : "https://image.tmdb.org/t/p/w500" + posterPath;
    }

    public @NonNull
    String getTitle() {
        return title == null ? "" : title;
    }

    public Boolean getAdult() {
        return adult;
    }

    /**
     * Return the movie backdrop img url.
     *
     * @return valid url or NULL otherwise.
     */
    public String getBackdropPath() {
        return TextUtils.isEmpty(backdropPath) ? null : "https://image.tmdb.org/t/p/w500" + backdropPath;
    }

    public Object getBelongsToCollection() {
        return belongsToCollection;
    }

    public Integer getBudget() {
        return budget;
    }

    public List<Genre> getGenres() {
        return genres;
    }

    public String getHomepage() {
        return homepage;
    }

    public Long getId() {
        return id;
    }

    public String getImdbId() {
        return imdbId;
    }

    public String getOriginalLanguage() {
        return originalLanguage;
    }

    public String getOriginalTitle() {
        return originalTitle;
    }

    public String getOverview() {
        return overview;
    }

    public Double getPopularity() {
        return popularity;
    }

    public String getYear() {
        return releaseDate;
    }

    public Long getRevenue() {
        return revenue;
    }

    public Integer getRuntime() {
        return runtime;
    }

    public String getStatus() {
        return status;
    }

    public String getTagline() {
        return tagline;
    }

    public Boolean getVideo() {
        return video;
    }

    public Float getVoteAverage() {
        return voteAverage;
    }

    public String getPosterPath() {
        return posterPath;
    }

    public String getReleaseDate() {
        return releaseDate;
    }

    public String getDisplayTitle() {
        String title = this.title;
        if (this.releaseDate != null) {
            try {
                title += " (" + this.releaseDate.substring(0, this.releaseDate.indexOf("-")) + ")";
            } catch (IndexOutOfBoundsException ex) {
                ex.printStackTrace();
                // ignore
            }
        }
        return title;
    }

    public Integer getVoteCount() {
        return voteCount;
    }
}
