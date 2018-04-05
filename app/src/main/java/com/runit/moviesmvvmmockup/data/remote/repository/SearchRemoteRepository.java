package com.runit.moviesmvvmmockup.data.remote.repository;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;
import android.util.Log;

import com.runit.moviesmvvmmockup.data.SearchRepository;
import com.runit.moviesmvvmmockup.data.exception.ErrorBundle;
import com.runit.moviesmvvmmockup.data.model.Result;
import com.runit.moviesmvvmmockup.data.model.MovieModel;
import com.runit.moviesmvvmmockup.data.model.ServerResponse;
import com.runit.moviesmvvmmockup.data.model.TvShow;
import com.runit.moviesmvvmmockup.data.remote.RetrofitClient;
import com.runit.moviesmvvmmockup.data.remote.TMDBApi;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


/**
 * Remote search repository implementation of {@link SearchRepository}. Searches through data available on {@link TMDBApi} API.
 */
public class SearchRemoteRepository implements SearchRepository {

    private static final SearchRemoteRepository mInstance = new SearchRemoteRepository();

    public static SearchRemoteRepository getInstance() {
        return mInstance;
    }

    @Override
    public LiveData<Result<List<MovieModel>>> searchMovies(String searchQuery, Integer page) {
        MutableLiveData<Result<List<MovieModel>>> results = new MutableLiveData<>();
        RetrofitClient.getClient().search(searchQuery, page).enqueue(new Callback<ServerResponse<MovieModel>>() {
            @Override
            public void onResponse(Call<ServerResponse<MovieModel>> call, Response<ServerResponse<MovieModel>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    results.setValue(new Result<>(response.body().getResults()));
                } else {
                    results.setValue(new Result<>(ErrorBundle.defaultServerError()));
                }
            }

            @Override
            public void onFailure(Call<ServerResponse<MovieModel>> call, Throwable t) {
                results.setValue(new Result<>(ErrorBundle.defaultConnectionError()));
            }
        });
        return results;
    }

    @Override
    public LiveData<Result<List<TvShow>>> searchTvShows(String searchQuery, Integer page) {
        Log.d("SearchRemoteRepository", "searchTvShows() not yet implemented.");
        return new MutableLiveData<>();
    }
}
