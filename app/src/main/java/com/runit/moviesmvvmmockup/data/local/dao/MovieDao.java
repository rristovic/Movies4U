package com.runit.moviesmvvmmockup.data.local.dao;

import android.arch.lifecycle.LiveData;
import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;
import android.arch.persistence.room.Transaction;
import android.arch.persistence.room.Update;

import com.runit.moviesmvvmmockup.data.local.BookmarkedMovie;
import com.runit.moviesmvvmmockup.data.model.MovieModel;

import java.util.List;


@Dao
public abstract class MovieDao {

    @Transaction
    public void insert(MovieModel movieModel, long userId) {
        this.insert(movieModel);
        this.insert(new BookmarkedMovie(movieModel.getId(), userId));
    }

    @Insert
    protected abstract void insert(MovieModel... movieModel);

    @Insert
    protected abstract void insert(BookmarkedMovie... bookmarkedMovie);

    @Update
    abstract void update(MovieModel... movieModel);

    @Delete
    public abstract void delete(BookmarkedMovie... bookmarkedMovies);

    @Delete
    public abstract void delete(MovieModel... movieModel);

    @Query("SELECT * FROM boomarked_movie where userId=:userId")
    public abstract LiveData<List<BookmarkedMovie>> getAllMoviesForUser(long userId);

    @Query("SELECT * FROM boomarked_movie where movieId=:movieId")
    public abstract LiveData<List<BookmarkedMovie>> getAllUsersForMovieId(long movieId);
}
