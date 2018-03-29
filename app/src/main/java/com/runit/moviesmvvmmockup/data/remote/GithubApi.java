package com.runit.moviesmvvmmockup.data.remote;

import com.runit.moviesmvvmmockup.data.model.Token;

import retrofit2.Call;
import retrofit2.http.GET;

/**
 * Created by Radovan Ristovic on 3/29/2018.
 * Quantox.com
 * radovanr995@gmail.com
 */

public interface GithubApi {
    @GET("authentication/token/new")
    Call<Token> getAuthToken();
}
