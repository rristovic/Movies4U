package com.runit.moviesmvvmmockup.ui.profile;

import android.arch.lifecycle.ViewModel;
import android.content.Context;
import android.databinding.ObservableField;

import com.runit.moviesmvvmmockup.data.local.UserCredentials;
import com.runit.moviesmvvmmockup.data.model.Account;
import com.runit.moviesmvvmmockup.data.remote.RetrofitClient;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by Radovan Ristovic on 4/4/2018.
 * Quantox.com
 * radovanr995@gmail.com
 */

public class ProfileViewModel extends ViewModel {
    public ObservableField<String> title = new ObservableField<>();

    public void getProfile(Context context) {
        RetrofitClient.getClient().getAccount(UserCredentials.getInstance(context).getSessionId().session()).enqueue(new Callback<Account>() {
            @Override
            public void onResponse(Call<Account> call, Response<Account> response) {
                if(response.isSuccessful()) {
                    title.set(response.body().getName());
                }
            }

            @Override
            public void onFailure(Call<Account> call, Throwable t) {

            }
        });
    }
}
