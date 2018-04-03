package com.runit.moviesmvvmmockup.ui.movie_details;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;
import android.arch.lifecycle.ViewModel;
import android.content.Intent;
import android.databinding.ObservableBoolean;
import android.net.Uri;
import android.view.View;

import com.runit.moviesmvvmmockup.data.model.MovieModel;
import com.runit.moviesmvvmmockup.data.remote.RetrofitClient;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by Radovan Ristovic on 4/3/2018.
 * Quantox.com
 * radovanr995@gmail.com
 */

public class MovieDetailsViewModel extends ViewModel {
    public ObservableBoolean isLoading = new ObservableBoolean(true);
    private MutableLiveData<MovieModel> mMovie;

    /**
     * Helper method for download movie details information.
     *
     * @param id Movie id.
     */
    public LiveData<MovieModel> getMovie(long id) {
        if (mMovie == null) {
            mMovie = new MutableLiveData<>();
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
        RetrofitClient.getClient().getMovieDetails(id).enqueue(new Callback<MovieModel>() {
            @Override
            public void onResponse(Call<MovieModel> call, Response<MovieModel> response) {
                if (response.isSuccessful() && response.body() != null) {
                    mMovie.setValue(response.body());
                }
                isLoading.set(false);
            }

            @Override
            public void onFailure(Call<MovieModel> call, Throwable t) {
                isLoading.set(false);
            }
        });
    }

    public void onHomepageClicked(View v) {
        Intent i = new Intent(Intent.ACTION_VIEW);
        i.setData(Uri.parse(mMovie.getValue().getHomepage()));
        v.getContext().startActivity(i);
    }
}
