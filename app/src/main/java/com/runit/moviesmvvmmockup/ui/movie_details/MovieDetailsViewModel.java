package com.runit.moviesmvvmmockup.ui.movie_details;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MediatorLiveData;
import android.arch.lifecycle.ViewModel;
import android.content.Intent;
import android.databinding.ObservableBoolean;
import android.net.Uri;
import android.view.View;

import com.runit.moviesmvvmmockup.data.MoviesRepository;
import com.runit.moviesmvvmmockup.data.RepositoryFactory;
import com.runit.moviesmvvmmockup.data.model.MovieModel;
import com.runit.moviesmvvmmockup.utils.exception.ErrorBundle;
import com.runit.moviesmvvmmockup.utils.exception.ErrorListener;

/**
 * Created by Radovan Ristovic on 4/3/2018.
 * Quantox.com
 * radovanr995@gmail.com
 */

public class MovieDetailsViewModel extends ViewModel {
    // Flag indicating is data is still loading
    public ObservableBoolean isLoading = new ObservableBoolean(true);
    // Current movie data
    private MediatorLiveData<MovieModel> mMovie;
    // Repo instance
    private MoviesRepository mRepository;
    // Error listener
    private ErrorListener mErrorListener;

    public MovieDetailsViewModel() {
        mRepository = RepositoryFactory.getMoviesRepository();
    }

    /**
     * Retrieves movie's details.
     *
     * @param id Id of the requested movie.
     */
    public LiveData<MovieModel> getMovie(long id, ErrorListener errorListener) {
        if (mMovie == null) {
            this.mErrorListener = errorListener;
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
        final LiveData<MovieModel> source = mRepository.getMovie(id);
        mMovie.addSource(source, movieModel -> {
            if (movieModel != null) {
                mMovie.setValue(movieModel);
            } else {
                // TODO : handle error
                mErrorListener.onErrorCallback(new ErrorBundle(ErrorBundle.ErrorMessage.SERVER_ERROR));
            }

            if (isLoading.get()) {
                isLoading.set(false);
            }
        });
    }

    /**
     * Homepage view listener.
     *
     * @param v View clicked.
     */
    public void onHomepageClicked(View v) {
        Intent i = new Intent(Intent.ACTION_VIEW);
        i.setData(Uri.parse(mMovie.getValue().getHomepage()));
        v.getContext().startActivity(i);
    }
}
