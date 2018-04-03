package com.runit.moviesmvvmmockup.ui.movie_list;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;
import android.arch.lifecycle.ViewModel;
import android.databinding.ObservableBoolean;

import com.runit.moviesmvvmmockup.data.model.MovieModel;
import com.runit.moviesmvvmmockup.data.model.ServerResponse;
import com.runit.moviesmvvmmockup.data.remote.RetrofitClient;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by Radovan Ristovic on 3/29/2018.
 * Quantox.com
 * radovanr995@gmail.com
 */

public class MovieListViewModel extends ViewModel {
    // Boolean indicating is list is initial loading from network
    private ObservableBoolean isInitLoading = new ObservableBoolean(true);
    // Holds the current movie list data
    private MutableLiveData<List<MovieModel>> mMovies;
    // Current category of movies in the list
    private MovieListCategory mCurrentCategory;
    // Current result page of the list from network
    private int mCurrentPage;

    /**
     * Method for retrieving movie list for selected movie category. Can be a category from {@link MovieListCategory} enum.
     *
     * @param category desired category for move list.
     * @return LiveData observable.
     */
    LiveData<List<MovieModel>> getMoviesForCategory(MovieListCategory category) {
        if (mMovies == null) {
            mMovies = new MutableLiveData<>();
            this.mCurrentCategory = category;
            this.mCurrentPage = 1;
            fetchMovies();
        }
        return mMovies;
    }

    /**
     * Call when next page of move list is needed.
     */
    void getNextPage() {
        mCurrentPage += 1;
        fetchMovies();
    }

    /**
     * Helper method for downloading movie list for current category and current page.
     */
    private void fetchMovies() {
        switch (mCurrentCategory) {
            case POPULAR: {
                fetchPopular();
                break;
            }
            case UPCOMING: {
                fetchUpcoming();
                break;
            }
            case TOP_RATED: {
                fetchTopRated();
                break;
            }
            case NOW_PLAYING: {
                fetchNowPlaying();
                break;
            }
            default: {
            }
        }
    }

    private void fetchNowPlaying() {
        RetrofitClient.getClient().getNowPlayingMovies(mCurrentPage).enqueue(new Callback<ServerResponse<MovieModel>>() {
            @Override
            public void onResponse(Call<ServerResponse<MovieModel>> call, Response<ServerResponse<MovieModel>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    onResponseSuccess(response.body().getResults());
                } else {
                    onResponseFailure();
                }
            }

            @Override
            public void onFailure(Call<ServerResponse<MovieModel>> call, Throwable t) {
                onResponseFailure();
            }
        });
    }

    private void fetchTopRated() {
        RetrofitClient.getClient().getTopRated(mCurrentPage).enqueue(new Callback<ServerResponse<MovieModel>>() {
            @Override
            public void onResponse(Call<ServerResponse<MovieModel>> call, Response<ServerResponse<MovieModel>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    onResponseSuccess(response.body().getResults());
                } else {
                    onResponseFailure();
                }
            }

            @Override
            public void onFailure(Call<ServerResponse<MovieModel>> call, Throwable t) {
                onResponseFailure();
            }
        });
    }

    private void fetchUpcoming() {
        RetrofitClient.getClient().getUpcoming(mCurrentPage).enqueue(new Callback<ServerResponse<MovieModel>>() {
            @Override
            public void onResponse(Call<ServerResponse<MovieModel>> call, Response<ServerResponse<MovieModel>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    onResponseSuccess(response.body().getResults());
                } else {
                    onResponseFailure();
                }
            }

            @Override
            public void onFailure(Call<ServerResponse<MovieModel>> call, Throwable t) {
                onResponseFailure();
            }
        });
    }

    private void fetchPopular() {
        RetrofitClient.getClient().getPopularMovies(mCurrentPage).enqueue(new Callback<ServerResponse<MovieModel>>() {
            @Override
            public void onResponse(Call<ServerResponse<MovieModel>> call, Response<ServerResponse<MovieModel>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    onResponseSuccess(response.body().getResults());
                } else {
                    onResponseFailure();
                }
            }

            @Override
            public void onFailure(Call<ServerResponse<MovieModel>> call, Throwable t) {
                onResponseFailure();
            }
        });
    }

    /**
     * Method to call when network response has been successful.
     *
     * @param movies movie list from the response.
     */
    private void onResponseSuccess(List<MovieModel> movies) {
        this.isInitLoading.set(false);
        mMovies.setValue(movies);
    }

    /**
     * Method to call when network response has failed.
     */
    private void onResponseFailure() {
        this.isInitLoading.set(false);
        mMovies.setValue(null);
    }

    public ObservableBoolean getIsInitialLoading() {
        return isInitLoading;
    }
}
