package com.runit.moviesmvvmmockup.data;

import android.content.Context;

import com.runit.moviesmvvmmockup.data.repository.MoviesTMDBRepository;
import com.runit.moviesmvvmmockup.data.remote.repository.SearchRemoteRepository;

/**
 * Provides repository implementation objects.
 */
public class RepositoryProvider {

    /**
     * Retrieves {@link MoviesRepository} object for managing {@link com.runit.moviesmvvmmockup.data.model.MovieModel} objects.
     * @return repository object.
     */
    public static MoviesRepository getMoviesRepository(Context context) {
        return MoviesTMDBRepository.getInstance(context);
    }

    /**
     * Retrieves {@link SearchRepository} object for searching through available data.
     * @return repository object.
     */
    public static SearchRepository getSearchRepository() {
        return SearchRemoteRepository.getInstance();
    }
}
