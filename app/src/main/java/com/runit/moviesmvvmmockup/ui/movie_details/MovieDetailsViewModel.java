package com.runit.moviesmvvmmockup.ui.movie_details;

import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;
import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MediatorLiveData;
import android.databinding.ObservableBoolean;
import android.support.annotation.NonNull;

import com.runit.moviesmvvmmockup.data.MoviesRepository;
import com.runit.moviesmvvmmockup.data.RepositoryProvider;
import com.runit.moviesmvvmmockup.data.local.UserCredentials;
import com.runit.moviesmvvmmockup.data.model.MovieModel;
import com.runit.moviesmvvmmockup.data.model.Result;
import com.runit.moviesmvvmmockup.utils.SingleLiveEvent;

/**
 * Created by Radovan Ristovic on 4/3/2018.
 * Quantox.com
 * radovanr995@gmail.com
 */

public class MovieDetailsViewModel extends AndroidViewModel {
    // Flag indicating that data is still loading
    public ObservableBoolean isLoading = new ObservableBoolean(true);
    // Current movie data
    private MediatorLiveData<Result<MovieModel>> mMovie;
    // LiveData emitting toast message
    private SingleLiveEvent<String> mToastMessage;
    // Repo instance
    private MoviesRepository mRepository;


    public MovieDetailsViewModel(@NonNull Application application) {
        super(application);
        mToastMessage = new SingleLiveEvent<>();
        mRepository = RepositoryProvider.getMoviesRepository();
    }

    /**
     * Retrieves movie's details.
     *
     * @param id Id of the requested movie.
     * @return {@link LiveData} observable which emits movie object.
     */
    public LiveData<Result<MovieModel>> getMovie(long id) {
        if (mMovie == null) {
            mMovie = new MediatorLiveData<>();
            fetchMovie(id);
        }

        return mMovie;
    }

    /**
     * Helper method for downloading movie details.
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

    /**
     * Return {@link LiveData} object that emits string representing a toast message to display.
     *
     * @return LiveData observable object.
     */
    public LiveData<String> onToastMessage() {
        return mToastMessage;
    }

    /**
     * Return boolean indicating if account features are available. Return false if user needs to login first.
     *
     * @return true if user can proceed with account features such as rating, bookmarking.
     */
    public boolean isAccountFeatureAvailable() {
        return UserCredentials.getInstance(getApplication()).isLoggedIn();
    }
}
