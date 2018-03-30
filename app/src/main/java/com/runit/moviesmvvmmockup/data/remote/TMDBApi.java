package com.runit.moviesmvvmmockup.data.remote;

import com.runit.moviesmvvmmockup.data.model.MovieModel;
import com.runit.moviesmvvmmockup.data.model.ServerResponse;
import com.runit.moviesmvvmmockup.data.model.Token;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

/**
 * Created by Radovan Ristovic on 3/29/2018.
 * Quantox.com
 * radovanr995@gmail.com
 */

public interface TMDBApi {
    @GET("authentication/token/new")
    Call<Token> getAuthToken();

    @GET("movie/now_playing")
    Call<ServerResponse<MovieModel>> getNowPlayingMovies(@Query("page") Integer page);

    @GET("movie/popular")
    Call<ServerResponse<MovieModel>> getPopularMovies(@Query("page") Integer page);

    @GET("movie/top_rated")
    Call<ServerResponse<MovieModel>> getTopRated(@Query("page") Integer page);

    @GET("movie/upcoming")
    Call<ServerResponse<MovieModel>> getUpcoming(@Query("page") Integer page);


}
