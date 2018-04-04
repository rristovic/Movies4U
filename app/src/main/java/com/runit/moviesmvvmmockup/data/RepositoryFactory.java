package com.runit.moviesmvvmmockup.data;

import com.runit.moviesmvvmmockup.data.remote.MoviesRemoteRepository;

/**
 * Provides repository implementation objects.
 */
public class RepositoryFactory {

    /**
     * Retrieves {@link MoviesRepository} object for managing {@link com.runit.moviesmvvmmockup.data.model.MovieModel} objects.
     * @return repository object.
     */
    public static MoviesRepository getMoviesRepository() {
        return MoviesRemoteRepository.getInstance();
    }
}
