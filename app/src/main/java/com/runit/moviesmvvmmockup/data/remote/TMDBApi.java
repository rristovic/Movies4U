package com.runit.moviesmvvmmockup.data.remote;

import com.runit.moviesmvvmmockup.data.model.Account;
import com.runit.moviesmvvmmockup.data.model.MovieModel;
import com.runit.moviesmvvmmockup.data.model.ServerResponse;
import com.runit.moviesmvvmmockup.data.model.Session;
import com.runit.moviesmvvmmockup.data.model.Token;
import com.runit.moviesmvvmmockup.data.model.request.MovieBookmarkRequest;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.Headers;
import retrofit2.http.Path;
import retrofit2.http.Query;

/**
 * Created by Radovan Ristovic on 3/29/2018.
 * Quantox.com
 * radovanr995@gmail.com
 */

public interface TMDBApi {
    @GET("authentication/token/new")
    Call<Token> getAuthToken();

    @GET("authentication/session/new")
    Call<Session> getSession(@Query("request_token") String requestToken);

    @GET("account")
    Call<Account> getAccount(@Query("session_id") String session_id);

    @GET("movie/now_playing")
    Call<ServerResponse<MovieModel>> getNowPlayingMovies(@Query("page") Integer page);

    @GET("movie/popular")
    Call<ServerResponse<MovieModel>> getPopularMovies(@Query("page") Integer page);

    @GET("movie/top_rated")
    Call<ServerResponse<MovieModel>> getTopRated(@Query("page") Integer page);

    @GET("movie/upcoming")
    Call<ServerResponse<MovieModel>> getUpcoming(@Query("page") Integer page);

    @GET("movie/{movie_id}")
    Call<MovieModel> getMovieDetails(@Path("movie_id") long movieId);

    @GET("search/movie?include_adult=true")
    Call<ServerResponse<MovieModel>> search(@Query("query") String searchQuery, @Query("page") Integer page);

    @GET("account/{account_id}/watchlist")
    @Headers("Content-Type: application/json;charset=utf-8")
    Call<ServerResponse<MovieModel>> bookmark(@Path("account_id") long account_id,
                                              @Query("session_id") String sessionId,
                                              @Body MovieBookmarkRequest requestBody);

    @GET("movie/{movie_id}/account_states")
    @Headers("Content-Type: application/json;charset=utf-8")
    Call<ServerResponse<MovieModel>> isMovieBookmarked(@Path("movie_id") long movieId,
                                                       @Query("account_id") long account_id,
                                              @Query("session_id") String sessionId,
                                              @Body MovieBookmarkRequest requestBody);
}
