package com.runit.moviesmvvmmockup;

import android.app.Application;

import com.runit.moviesmvvmmockup.data.remote.RetrofitClient;

/**
 * Created by Radovan Ristovic on 3/29/2018.
 * Quantox.com
 * radovanr995@gmail.com
 */

public class MoviesApplication extends Application {

    @Override
    public void onCreate() {
        super.onCreate();
        RetrofitClient.init(this);
    }
}
