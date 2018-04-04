package com.runit.moviesmvvmmockup.data;

import android.arch.lifecycle.LiveData;

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
     * @param page     list page.
     * @return {@link LiveData} observable to subscribe to. If value is null, there has been an error with retrieving the list.
     */
    LiveData<List<MovieModel>> getMovieList(MovieListCategory category, int page);

    /**
     * Method for retrieving a {@link MovieModel} object.
     * @param movieId id of the requested movie.
     * @return {@link LiveData} observable to subscribe to. If value is null, there has been an error with retrieving the movie.
     */
    LiveData<MovieModel> getMovie(long movieId);
}
