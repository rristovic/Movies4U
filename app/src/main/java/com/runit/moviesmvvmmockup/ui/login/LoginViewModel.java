package com.runit.moviesmvvmmockup.ui.login;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;
import android.arch.lifecycle.ViewModel;
import android.databinding.ObservableBoolean;

import com.runit.moviesmvvmmockup.data.model.Token;
import com.runit.moviesmvvmmockup.data.remote.RetrofitClient;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by Radovan Ristovic on 3/29/2018.
 * Quantox.com
 * radovanr995@gmail.com
 */

public class LoginViewModel extends ViewModel {
    public ObservableBoolean isLoading = new ObservableBoolean(true);
    private MutableLiveData<String> token = new MutableLiveData<>();
    private MutableLiveData<Boolean> isLoggedIn = new MutableLiveData<>();

    /**
     * Get access token for network requests.
     * @return String access token.
     */
    public LiveData<String> getToken() {
        fetchToken();
        return token;
    }

    /**
     * Method for checking if the user is currently logged in.
     * @return true is the user is logged in and can continue with user flow.
     */
    public LiveData<Boolean> isUserLoggedIn() {
        isLoggedIn.setValue(false);
        return isLoggedIn;
    }

    private void fetchToken() {
        RetrofitClient.getClient().getAuthToken().enqueue(new Callback<Token>() {
            @Override
            public void onResponse(Call<Token> call, Response<Token> response) {
                if (response.isSuccessful() && response.body() != null) {
                    token.setValue(response.body().request_token);
                } else {
                    token.setValue(null);
                }
            }

            @Override
            public void onFailure(Call<Token> call, Throwable t)
            {
                token.setValue(null);
            }
        });
    }

    /**
     * Call this when new page has been loaded into a web view.
     * @param url of newly loaded page.
     */
    public void onPageLoaded(String url) {
        if (url.contains("/allow")) {
            isLoggedIn.setValue(true);
        }
        isLoading.set(false);
    }
}
