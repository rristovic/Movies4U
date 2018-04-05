package com.runit.moviesmvvmmockup.data;

import android.arch.lifecycle.LiveData;

import com.runit.moviesmvvmmockup.data.model.Result;
import com.runit.moviesmvvmmockup.data.model.MovieModel;
import com.runit.moviesmvvmmockup.data.model.TvShow;

import java.util.List;

/**
 * Search repository class for searching data. Currently only movie search is available.
 */
public interface SearchRepository {
    /**
     * Search for {@link MovieModel} objects.
     *
     * @param searchQuery desired search query to execute upon.
     * @param page        desired search result's page
     * @return {@link LiveData} observable to subscribe to, emitting list of search results.
     */
    LiveData<Result<List<MovieModel>>> searchMovies(String searchQuery, Integer page);

    /**
     * Search for {@link TvShow} objects.
     *
     * @param searchQuery desired search query to execute upon.
     * @param page        desired search result's page
     * @return {@link LiveData} observable to subscribe to, emitting list of search results.
     */
    LiveData<Result<List<TvShow>>> searchTvShows(String searchQuery, Integer page);
}
