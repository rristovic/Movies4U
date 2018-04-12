package com.runit.moviesmvvmmockup.data.local;

import android.arch.persistence.room.Entity;
import android.arch.persistence.room.ForeignKey;
import android.support.annotation.NonNull;

import com.runit.moviesmvvmmockup.data.model.MovieModel;


@Entity(tableName = "boomarked_movie",
        primaryKeys = {"userId", "movieId"},
        foreignKeys = {@ForeignKey(entity = MovieModel.class,
                parentColumns = "id",
                childColumns = "movieId")
        }

)
public class BookmarkedMovie {
    public final @NonNull
    Long userId;
    public final @NonNull
    Long movieId;

    public BookmarkedMovie(long movieId, long userId) {
        this.userId = userId;
        this.movieId = movieId;
    }
}

