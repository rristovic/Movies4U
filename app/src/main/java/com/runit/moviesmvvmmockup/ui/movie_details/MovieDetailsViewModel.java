package com.runit.moviesmvvmmockup.ui.movie_details;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MediatorLiveData;
import android.arch.lifecycle.ViewModel;
import android.content.Context;
import android.databinding.ObservableBoolean;
import android.view.View;

import com.runit.moviesmvvmmockup.data.MoviesRepository;
import com.runit.moviesmvvmmockup.data.RepositoryProvider;
import com.runit.moviesmvvmmockup.data.local.UserCredentials;
import com.runit.moviesmvvmmockup.data.model.MovieModel;
import com.runit.moviesmvvmmockup.data.model.Result;
import com.runit.moviesmvvmmockup.data.model.ServerResponse;
import com.runit.moviesmvvmmockup.data.model.request.MovieBookmarkRequest;
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
     * Bookmark click listener method. Bookmarks current movie if user is logged in.
     */
    public void onBookmarkClicked(Context context) {
        if (mMovie.getValue() != null && mMovie.getValue().isSuccess()) {
            UserCredentials userCredentials = UserCredentials.getInstance(context);
            if (userCredentials.getUserAccount() != null && userCredentials.getSessionId() != null) {
                RetrofitClient.getClient().bookmark(userCredentials.getUserAccount().getId(), userCredentials.getSessionId().session(),
                        new MovieBookmarkRequest(mMovie.getValue().get().getId())).enqueue(new Callback<ServerResponse<MovieModel>>() {
                    @Override
                    public void onResponse(Call<ServerResponse<MovieModel>> call, Response<ServerResponse<MovieModel>> response) {
                          
                    }

                    @Override
                    public void onFailure(Call<ServerResponse<MovieModel>> call, Throwable t) {

                    }
                });
            }
        }
    }

    /**
     * Rate movie click listener method. Rates current movie if user is logged in.
     */
    public void rateMovie(Context context, int rate) {
        if (mMovie.getValue() != null) {

        }
    }
}
