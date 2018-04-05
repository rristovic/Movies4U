package com.runit.moviesmvvmmockup.ui.movie_details;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MediatorLiveData;
import android.arch.lifecycle.ViewModel;
import android.databinding.ObservableBoolean;

import com.runit.moviesmvvmmockup.data.MoviesRepository;
import com.runit.moviesmvvmmockup.data.RepositoryProvider;
import com.runit.moviesmvvmmockup.data.model.MovieModel;
import com.runit.moviesmvvmmockup.data.model.Result;

/**
 * Created by Radovan Ristovic on 4/3/2018.
 * Quantox.com
 * radovanr995@gmail.com
 */

public class MovieDetailsViewModel extends ViewModel {
    // Flag indicating is data is still loading
    public ObservableBoolean isLoading = new ObservableBoolean(true);
    // Current movie data
    private MediatorLiveData<Result<MovieModel>> mMovie;
    // Repo instance
    private MoviesRepository mRepository;

    public MovieDetailsViewModel() {
        mRepository = RepositoryProvider.getMoviesRepository();
    }

    /**
     * Retrieves movie's details.
     *
     * @param id Id of the requested movie.
     */
    public LiveData<Result<MovieModel>> getMovie(long id) {
        if (mMovie == null) {
            mMovie = new MediatorLiveData<>();
            fetchMovie(id);
        }

        return mMovie;
    }

    /**
     * Helper method for download movie details.
     *
     * @param id Movie's id which details should be downloaded.
     */
    private void fetchMovie(long id) {
        final LiveData<Result<MovieModel>> source = mRepository.getMovie(id);
        mMovie.addSource(source, movieModel -> {
            mMovie.setValue(movieModel);

            if (isLoading.get()) {
                isLoading.set(false);
            }
        });
    }
}
