package com.runit.moviesmvvmmockup.ui.search;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MediatorLiveData;
import android.arch.lifecycle.ViewModel;
import android.databinding.ObservableBoolean;

import com.runit.moviesmvvmmockup.data.RepositoryProvider;
import com.runit.moviesmvvmmockup.data.SearchRepository;
import com.runit.moviesmvvmmockup.data.model.Result;
import com.runit.moviesmvvmmockup.data.model.MovieModel;

import java.util.List;

/**
 * Created by Radovan Ristovic on 4/4/2018.
 * Quantox.com
 * radovanr995@gmail.com
 */

public class SearchViewModel extends ViewModel {
    // Flag indicating if new search is in progress
    public ObservableBoolean isInitSearching = new ObservableBoolean(true);
    // Search repository instance
    private SearchRepository mRepository;
    // Current search results observable
    private MediatorLiveData<Result<List<MovieModel>>> mMovieList;
    // Current search result page
    private int mCurrentPage;
    // Last search query executed
    private String mLastSearchQuery;

    public SearchViewModel() {
        mRepository = RepositoryProvider.getSearchRepository();
        mMovieList = new MediatorLiveData<>();
        mCurrentPage = 1;
        mLastSearchQuery = "";
    }

    /**
     * Returns {@link LiveData} observable containing most recent search results retrieved by calling {@link #search(String)}.
     *
     * @return observable which emits most recent search results.
     */
    public LiveData<Result<List<MovieModel>>> getSearchResults() {
        return mMovieList;
    }

    /**
     * Call this method to execute new search query.
     *
     * @param searchQuery requested search query.
     */
    public void search(String searchQuery) {
        if (!mLastSearchQuery.equals(searchQuery)) {
            // Reset current page
            mCurrentPage = 1;
            isInitSearching.set(true);
        }
        mLastSearchQuery = searchQuery;
        LiveData<Result<List<MovieModel>>> source = mRepository.searchMovies(searchQuery, mCurrentPage);
        mMovieList.addSource(source, items -> {
            mMovieList.removeSource(source);
            mMovieList.setValue(items);

            if (isInitSearching.get()) {
                isInitSearching.set(false);
            }
        });
    }

    /**
     * Call when next page of search results is needed.
     */
    public void getNextPage() {
        mCurrentPage++;
        this.search(mLastSearchQuery);
    }
}
