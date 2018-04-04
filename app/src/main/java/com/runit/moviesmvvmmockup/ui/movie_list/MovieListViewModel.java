package com.runit.moviesmvvmmockup.ui.movie_list;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MediatorLiveData;
import android.arch.lifecycle.ViewModel;
import android.databinding.ObservableBoolean;
import android.graphics.RectF;

import com.runit.moviesmvvmmockup.data.MoviesRepository;
import com.runit.moviesmvvmmockup.data.RepositoryFactory;
import com.runit.moviesmvvmmockup.data.model.MovieListCategory;
import com.runit.moviesmvvmmockup.data.model.MovieModel;
import com.runit.moviesmvvmmockup.data.remote.MoviesRemoteRepository;
import com.runit.moviesmvvmmockup.utils.exception.ErrorBundle;
import com.runit.moviesmvvmmockup.utils.exception.ErrorListener;

import java.util.List;

/**
 * Created by Radovan Ristovic on 3/29/2018.
 * Quantox.com
 * radovanr995@gmail.com
 */

public class MovieListViewModel extends ViewModel {
    // Flag indicating if initial load of data is in progress
    public ObservableBoolean isInitialLoading = new ObservableBoolean(true);
    // Holds the current movie list data
    private MediatorLiveData<List<MovieModel>> mMovies;
    // Current category of movies in the list
    private MovieListCategory mCurrentCategory;
    // Current result page of the list
    private int mCurrentPage;
    // Repository instance
    private MoviesRepository mRepository;
    // Error listener
    private ErrorListener mErrorListener;

    public MovieListViewModel() {
        mRepository = RepositoryFactory.getMoviesRepository();
    }

    /**
     * Method for retrieving movie list for selected movie category. Can be a category from {@link MovieListCategory} enum.
     *
     * @param category      desired category for move list.
     * @param errorListener listener to call when an error has occurred while retrieving the data.
     * @return LiveData observable.
     */
    LiveData<List<MovieModel>> getMoviesForCategory(MovieListCategory category, ErrorListener errorListener) {
        if (mMovies == null) {
            this.mCurrentCategory = category;
            this.mCurrentPage = 1;
            this.mErrorListener = errorListener;
            this.mMovies = new MediatorLiveData<>();
            getData();
        }
        return mMovies;
    }

    /**
     * Helper method for retrieving data. Handles {@link MovieListViewModel#isInitialLoading} value and errors.
     */
    void getData() {
        LiveData<List<MovieModel>> source = mRepository.getMovieList(mCurrentCategory, mCurrentPage);
        mMovies.addSource(source, movies -> {
            // Stop watching the list after data is loaded
            mMovies.removeSource(source);
            if (movies != null) {
                mMovies.setValue(movies);
            } else {
                // TODO : handle error
                mErrorListener.onErrorCallback(new ErrorBundle(ErrorBundle.ErrorMessage.SERVER_ERROR));
            }

            if (isInitialLoading.get()) {
                isInitialLoading.set(false);
            }
        });
    }

    /**
     * Call when next page of movie list is needed.
     */
    void getNextPage() {
        mCurrentPage += 1;
        getData();
    }
}
