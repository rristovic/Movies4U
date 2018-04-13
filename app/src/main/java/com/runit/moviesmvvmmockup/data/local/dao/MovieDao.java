package com.runit.moviesmvvmmockup.data.local.dao;

import android.arch.lifecycle.LiveData;
import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.OnConflictStrategy;
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

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    abstract void insert(MovieModel movieModel);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    abstract void insert(BookmarkedMovie bookmarkedMovie);

    @Update
    public abstract void update(MovieModel movieModel);

    @Delete
    public abstract void delete(BookmarkedMovie bookmarkedMovies);

    @Delete
    public abstract void delete(MovieModel movieModel);

    @Query("SELECT movieId FROM boomarked_movie where userId=:userId")
    public abstract LiveData<List<Long>> getAllMovieIdsForUser(long userId);

    @Query("SELECT userId FROM boomarked_movie where movieId=:movieId")
    public abstract LiveData<List<Long>> getAllUserIdsForMovieId(long movieId);

    @Query("SELECT userId FROM boomarked_movie where movieId=:movieId")
    public abstract List<Long> getAllUserIdsForMovieIdAsync(long movieId);

    @Query("SELECT * FROM MovieModel WHERE id IN (:movieIds)")
    public abstract LiveData<List<MovieModel>> getMovies(List<Long> movieIds);

    @Query("SELECT * FROM MovieModel WHERE id=:movieId")
    public abstract LiveData<MovieModel> getMovie(Long movieId);

    @Query("SELECT movieId FROM boomarked_movie WHERE movieId=:movieId AND userId=:userId")
    public abstract LiveData<Long> isMovieBookmarked(long movieId, long userId);

    @Query("SELECT movieId FROM boomarked_movie WHERE movieId=:movieId AND userId=:userId")
    public abstract Long isMovieBookmarkedAsync(long movieId, long userId);
}
