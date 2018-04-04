package com.runit.moviesmvvmmockup.data.remote;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;
import android.support.annotation.NonNull;

import com.runit.moviesmvvmmockup.data.MoviesRepository;
import com.runit.moviesmvvmmockup.data.model.MovieListCategory;
import com.runit.moviesmvvmmockup.data.model.MovieModel;
import com.runit.moviesmvvmmockup.data.model.ServerResponse;
import com.runit.moviesmvvmmockup.utils.exception.RepositoryException;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/*
  Created by Radovan Ristovic on 4/4/2018.
  Quantox.com
  radovanr995@gmail.com
 */


/**
 * {@link MoviesRepository} implementation which returns data from the {@link TMDBApi} network api.
 */
public class MoviesRemoteRepository implements MoviesRepository {
    private static MoviesRemoteRepository mInstance = new MoviesRemoteRepository();

    public static MoviesRemoteRepository getInstance() {
        return mInstance;
    }

    @Override
    public LiveData<MovieModel> getMovie(long movieId) {
        final MutableLiveData<MovieModel> movie = new MutableLiveData<>();
        RetrofitClient.getClient().getMovieDetails(movieId).enqueue(new Callback<MovieModel>() {
            @Override
            public void onResponse(Call<MovieModel> call, Response<MovieModel> response) {
                if (response.isSuccessful() && response.body() != null) {
                    movie.setValue(response.body());
                } else {
                    movie.setValue(null);
                }
            }

            @Override
            public void onFailure(Call<MovieModel> call, Throwable t) {
                movie.setValue(null);
            }
        });
        return movie;
    }


    @Override
    public LiveData<List<MovieModel>> getMovieList(MovieListCategory category, int page) {
        LiveData<List<MovieModel>> movies;
        switch (category) {
            case POPULAR: {
                movies = fetchPopular(page);
                break;
            }
            case UPCOMING: {
                movies = fetchUpcoming(page);
                break;
            }
            case TOP_RATED: {
                movies = fetchTopRated(page);
                break;
            }
            case NOW_PLAYING: {
                movies = fetchNowPlaying(page);
                break;
            }
            default: {
                throw new RepositoryException(String.format("No implementation found for handling %s category.", category.toString()));
            }
        }

        return movies;
    }

    /**
     * Helper method for downloading "now_playing" movie list.
     *
     * @param page desired page of the list.
     * @return {@link LiveData} observable.
     */
    private LiveData<List<MovieModel>> fetchNowPlaying(int page) {
        final MutableLiveData<List<MovieModel>> movies = new MutableLiveData<>();
        RetrofitClient.getClient().getNowPlayingMovies(page).enqueue(new Callback<ServerResponse<MovieModel>>() {
            @Override
            public void onResponse(Call<ServerResponse<MovieModel>> call, Response<ServerResponse<MovieModel>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    onResponseSuccess(movies, response.body().getResults());
                } else {
                    onResponseFailure(movies);
                }
            }

            @Override
            public void onFailure(Call<ServerResponse<MovieModel>> call, Throwable t) {
                onResponseFailure(movies);
            }
        });
        return movies;
    }

    /**
     * Helper method for downloading "top_rated" movie list.
     *
     * @param page desired page of the list.
     * @return {@link LiveData} observable.
     */
    private LiveData<List<MovieModel>> fetchTopRated(int page) {
        final MutableLiveData<List<MovieModel>> movies = new MutableLiveData<>();
        RetrofitClient.getClient().getTopRated(page).enqueue(new Callback<ServerResponse<MovieModel>>() {
            @Override
            public void onResponse(Call<ServerResponse<MovieModel>> call, Response<ServerResponse<MovieModel>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    onResponseSuccess(movies, response.body().getResults());
                } else {
                    onResponseFailure(movies);
                }
            }

            @Override
            public void onFailure(Call<ServerResponse<MovieModel>> call, Throwable t) {
                onResponseFailure(movies);
            }
        });
        return movies;
    }

    /**
     * Helper method for downloading "upcomming" movie list.
     *
     * @param page desired page of the list.
     * @return {@link LiveData} observable.
     */
    private LiveData<List<MovieModel>> fetchUpcoming(int page) {
        final MutableLiveData<List<MovieModel>> movies = new MutableLiveData<>();
        RetrofitClient.getClient().getUpcoming(page).enqueue(new Callback<ServerResponse<MovieModel>>() {
            @Override
            public void onResponse(Call<ServerResponse<MovieModel>> call, Response<ServerResponse<MovieModel>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    onResponseSuccess(movies, response.body().getResults());
                } else {
                    onResponseFailure(movies);
                }
            }

            @Override
            public void onFailure(Call<ServerResponse<MovieModel>> call, Throwable t) {
                onResponseFailure(movies);
            }
        });
        return movies;
    }

    /**
     * Helper method for downloading "popular" movie list.
     *
     * @param page desired page of the list.
     * @return {@link LiveData} observable.
     */
    private LiveData<List<MovieModel>> fetchPopular(int page) {
        final MutableLiveData<List<MovieModel>> movies = new MutableLiveData<>();
        RetrofitClient.getClient().getPopularMovies(page).enqueue(new Callback<ServerResponse<MovieModel>>() {
            @Override
            public void onResponse(Call<ServerResponse<MovieModel>> call, Response<ServerResponse<MovieModel>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    onResponseSuccess(movies, response.body().getResults());
                } else {
                    onResponseFailure(movies);
                }
            }

            @Override
            public void onFailure(Call<ServerResponse<MovieModel>> call, Throwable t) {
                onResponseFailure(movies);
            }
        });
        return movies;
    }

    /**
     * Method to call when network response has been successful.
     *
     * @param observable LiveData observable on which to post new data.
     * @param movieList  Newly downloaded list of movies.
     */
    private void onResponseSuccess(MutableLiveData<List<MovieModel>> observable, @NonNull List<MovieModel> movieList) {
        observable.setValue(movieList);
    }

    /**
     * Method to call when network response has failed.
     *
     * @param observable LiveData observable on which to post an error.
     */
    private void onResponseFailure(MutableLiveData<List<MovieModel>> observable) {
        observable.setValue(null);
    }
}
