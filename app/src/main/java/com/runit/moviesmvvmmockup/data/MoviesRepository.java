package com.runit.moviesmvvmmockup.data;

import android.arch.lifecycle.LiveData;

import com.runit.moviesmvvmmockup.data.model.Result;
import com.runit.moviesmvvmmockup.data.model.MovieListCategory;
import com.runit.moviesmvvmmockup.data.model.MovieModel;

import java.util.List;

/**
 * Repository class for loading movies.
 */
public interface MoviesRepository {
    /**
     * Method for retrieving movie list for provided list category and page.
     *
     * @param category movie list category to be retrieved.
     * @param page     desired movie list's page.
     * @return {@link LiveData} observable to subscribe to, emitting the movie list.
     */
    LiveData<Result<List<MovieModel>>> getMovieList(MovieListCategory category, int page);

    /**
     * Method for retrieving a {@link MovieModel} object.
     *
     * @param movieId id of the requested movie.
     * @return {@link LiveData} observable to subscribe to, emitting the movie list.
     */
    LiveData<Result<MovieModel>> getMovie(long movieId);
}
