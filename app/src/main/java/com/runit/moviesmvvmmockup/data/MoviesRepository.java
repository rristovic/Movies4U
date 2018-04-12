package com.runit.moviesmvvmmockup.data;

import android.arch.lifecycle.LiveData;

import com.runit.moviesmvvmmockup.data.local.UserCredentials;
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

    /**
     * Method for retrieving list of {@link MovieModel} that are bookmarked for the current user.
     * @param userId current user id.
     * @return {@link LiveData} observable to subscribe to, emitting bookmarked movies list.
     */
    LiveData<Result<List<MovieModel>>> getBookmarkedMovies(long userId);

    /**
     * Method for checking if a movie is bookmarked by a user.
     * @param movieId id of the movie.
     * @return {@link LiveData} observable to subscribe to, emitting true if movie has been bookmarked.
     */
    LiveData<Result<Boolean>> isMovieBookmarked(long movieId);

    /**
     * Method for bookmarking a movie.
     * @param movieModel {@link MovieModel} to be bookmarked.
     */
    void bookmark(MovieModel movieModel);

    /**
     * Method for removing movie from bookmark.
     * @param movieModel {@link MovieModel} to be removed.
     */
    void removeFromBookmark(MovieModel movieModel);
}
