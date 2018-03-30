package com.runit.moviesmvvmmockup.data.remote;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;

import com.runit.moviesmvvmmockup.utils.exception.ApplicationException;

import java.io.File;
import java.io.IOException;
import java.util.concurrent.TimeUnit;

import okhttp3.Authenticator;
import okhttp3.Cache;
import okhttp3.HttpUrl;
import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.Route;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

import static com.runit.moviesmvvmmockup.data.remote.NetworkConstants.API_KEY;
import static com.runit.moviesmvvmmockup.data.remote.NetworkConstants.BASE_URL;

/**
 * Created by Radovan Ristovic on 3/29/2018.
 * Quantox.com
 * radovanr995@gmail.com
 */

public class RetrofitClient {

    // Retrofit singleton instance
    private static Retrofit mInstance = null;
    // Application context
    @SuppressLint("StaticFieldLeak")
    private static Context mContext;
    private static TMDBApi mApiClient;

    /**
     * Initialize networking client.
     *
     * @param context Context which will be used for IO/SharedPreferences.
     */
    public static void init(Context context) {
        final HttpLoggingInterceptor loggingInterceptor = new HttpLoggingInterceptor();
        loggingInterceptor.setLevel(HttpLoggingInterceptor.Level.BODY);
        // Build cache dir
        File httpCacheDirectory = new File(context.getCacheDir(), "vat_mobile_cache");
        Cache cache = new Cache(httpCacheDirectory,NetworkConstants.CACHE_SIZE);
        // Build OkHttp client
        OkHttpClient okHttpClient = new OkHttpClient.Builder()
                .addNetworkInterceptor(loggingInterceptor)
                .addNetworkInterceptor(chain -> {
                    Request original = chain.request();
                    HttpUrl url = original.url().newBuilder().addQueryParameter("api_key", API_KEY).build();
                    Request request = original.newBuilder().url(url).build();
                    return chain.proceed(request);
                })
                .readTimeout(NetworkConstants.TIME_OUT_SECONDS, TimeUnit.SECONDS)
                .connectTimeout(NetworkConstants.TIME_OUT_SECONDS, TimeUnit.SECONDS)
                .cache(cache)
                .build();

        mInstance = new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .client(okHttpClient)
                .build();
        mApiClient = mInstance.create(TMDBApi.class);
        // Enforce application context
        mContext = context.getApplicationContext();
    }

    public static TMDBApi getClient() {
        if (mInstance == null) {
            throw new ApplicationException("Must call init() method in order to use RetrofitClient.");
        }

        return mApiClient;
    }
}
