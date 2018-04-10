package com.runit.moviesmvvmmockup.ui.movie_list;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MediatorLiveData;
import android.arch.lifecycle.ViewModel;
import android.databinding.ObservableBoolean;

import com.runit.moviesmvvmmockup.data.MoviesRepository;
import com.runit.moviesmvvmmockup.data.RepositoryProvider;
import com.runit.moviesmvvmmockup.data.model.MovieListCategory;
import com.runit.moviesmvvmmockup.data.model.MovieModel;
import com.runit.moviesmvvmmockup.data.model.Result;

import java.util.List;

/**
 * Created by Radovan Ristovic on 3/29/2018.
 * Quantox.com
 * radovanr995@gmail.com
 */

public class MovieListViewModel extends ViewModel {
    // Flag indicating if initial loading of data is in progress
    public ObservableBoolean isInitialLoading = new ObservableBoolean(true);
    // Holds the current movie list data
    private MediatorLiveData<Result<List<MovieModel>>> mMovies;
    // Current category of movies in the list
    private MovieListCategory mCurrentCategory;
    // Current result page of the list
    private int mCurrentPage;
    // Repository instance
    private MoviesRepository mRepository;
    // last page total item count
    private int mLastPageItemCount;

    public MovieListViewModel() {
        mRepository = RepositoryProvider.getMoviesRepository();
    }

    /**
     * Method for retrieving movie list for selected movie category. Can be a category from {@link MovieListCategory} enum.
     * If result is an empty list, it means that the current loaded page has no items.
     *
     * @param category desired category for move list.
     * @return LiveData observable emitting list of movies.
     */
    LiveData<Result<List<MovieModel>>> getMoviesForCategory(MovieListCategory category) {
        if (mMovies == null) {
            this.mCurrentCategory = category;
            this.mCurrentPage = 1;
            this.mMovies = new MediatorLiveData<>();
            getData();
        }
        return mMovies;
    }

    /**
     * Helper method for retrieving data. Handles {@link MovieListViewModel#isInitialLoading} value and errors.
     */
    void getData() {
        LiveData<Result<List<MovieModel>>> source = mRepository.getMovieList(mCurrentCategory, mCurrentPage);
        mMovies.addSource(source, movies -> {
            // Stop watching the list after data is loaded
            mMovies.removeSource(source);
            if (movies.isSuccess()) {
                mLastPageItemCount = movies.get().size();
            }
            if (mMovies.getValue() != null && movies.isSuccess()) {
                // Append to the current list
                List<MovieModel> currentData = mMovies.getValue().get();
                currentData.addAll(movies.get());
                mMovies.setValue(new Result<>(currentData));
            } else {
                // Set new list value or error
                mMovies.setValue(movies);
            }

            if (isInitialLoading.get()) {
                isInitialLoading.set(false);
            }
        });
    }

    /**
     * Retrieves total number of items retried for the last page.
     *
     * @return last page item count.
     */
    int getLastPageItemCount() {
        return this.mLastPageItemCount;
    }

    /**
     * Call when next page of movie list is needed.
     */
    void getNextPage() {
        mCurrentPage += 1;
        getData();
    }
}
