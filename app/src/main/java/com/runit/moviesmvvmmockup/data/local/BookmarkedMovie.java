package com.runit.moviesmvvmmockup.data.local;

import android.arch.persistence.room.Entity;
import android.arch.persistence.room.ForeignKey;

import com.runit.moviesmvvmmockup.data.model.MovieModel;


@Entity(tableName = "boomarked_movie",
        primaryKeys = {"userId", "movieId"},
        foreignKeys = {
                @ForeignKey(entity = MovieModel.class,
                        parentColumns = "id",
                        childColumns = "movieId")
        }
)
public class BookmarkedMovie {
    public final long userId;
    public final long movieId;

    public BookmarkedMovie(long userId, long movieId) {
        this.userId = userId;
        this.movieId = movieId;
    }
}

