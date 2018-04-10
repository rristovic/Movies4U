package com.runit.moviesmvvmmockup.ui.login;

import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;
import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;
import android.databinding.ObservableBoolean;
import android.support.annotation.NonNull;

import com.runit.moviesmvvmmockup.data.local.UserCredentials;
import com.runit.moviesmvvmmockup.data.model.Account;
import com.runit.moviesmvvmmockup.data.model.Session;
import com.runit.moviesmvvmmockup.data.model.Token;
import com.runit.moviesmvvmmockup.data.remote.NetworkConstants;
import com.runit.moviesmvvmmockup.data.remote.RetrofitClient;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by Radovan Ristovic on 3/29/2018.
 * Quantox.com
 * radovanr995@gmail.com
 */

public class LoginViewModel extends AndroidViewModel {
    public ObservableBoolean isLoading = new ObservableBoolean(true);
    private MutableLiveData<String> loginPage = new MutableLiveData<>();
    private MutableLiveData<Boolean> isLoggedIn = new MutableLiveData<>();
    private UserCredentials mUserCredentials;

    public LoginViewModel(@NonNull Application application) {
        super(application);
    }


    /**
     * Method for checking if the user is currently logged in.
     *
     * @return true is the user is logged in and can continue with user flow, false otherwise.
     */
    public LiveData<Boolean> isUserLoggedIn() {
        mUserCredentials = UserCredentials.getInstance(getApplication());
        if (mUserCredentials.getRequestToken() != null && mUserCredentials.getSessionId() != null) {
            // Try getting user data to see if session is still valid
            RetrofitClient.getClient().getAccount(mUserCredentials.getSessionId().session()).enqueue(new Callback<Account>() {
                @Override
                public void onResponse(Call<Account> call, Response<Account> response) {
                    if (response.isSuccessful()) {
                        mUserCredentials.setAccount(response.body());
                        isLoggedIn.setValue(true);
                    } else {
                        isLoggedIn.setValue(false);
                    }
                }

                @Override
                public void onFailure(Call<Account> call, Throwable t) {
                    isLoggedIn.setValue(false);
                }
            });
        } else {
            isLoggedIn.setValue(false);
        }
        return isLoggedIn;
    }

    /**
     * Get login page to let user authorize the application with the API.
     *
     * @return Observable string which returns valid login page or null if there has been an error.
     */
    public LiveData<String> getLoginPage() {
        fetchToken();
        return loginPage;
    }

    /**
     * Helper method for retrieving new request token from the network api and generating login page for the user.
     */
    private void fetchToken() {
        RetrofitClient.getClient().getAuthToken().enqueue(new Callback<Token>() {
            @Override
            public void onResponse(Call<Token> call, Response<Token> response) {
                if (response.isSuccessful() && response.body() != null) {
                    mUserCredentials.setToken(response.body());
                    loginPage.setValue(NetworkConstants.loginPage(response.body().token()));
                } else {
                    loginPage.setValue(null);
                }
            }

            @Override
            public void onFailure(Call<Token> call, Throwable t) {
                loginPage.setValue(null);
            }
        });
    }

    /**
     * Call this when new page has been loaded into a web view.
     *
     * @param url of newly loaded page.
     */
    public void onPageLoaded(String url) {
        if (url.contains("/allow")) {
            generateNewSession();
        }
        isLoading.set(false);
    }

    /**
     * Helper method used for generating new user session that will be used to authorize protected API calls.
     * Changes the status of logged in if retrieving of a new session was a failure.
     */
    private void generateNewSession() {
        isLoading.set(true);
        RetrofitClient.getClient().getSession(mUserCredentials.getRequestToken().token()).enqueue(new Callback<Session>() {
            @Override
            public void onResponse(Call<Session> call, Response<Session> response) {
                if (response.isSuccessful() && response.body() != null) {
                    mUserCredentials.setSessionId(response.body());
                    getProfile();
                } else {
                    isLoggedIn.setValue(false);
                }
                isLoading.set(false);
            }

            @Override
            public void onFailure(Call<Session> call, Throwable t) {
                isLoading.set(false);
                isLoggedIn.setValue(false);
            }
        });
    }

    /**
     * Helper method for retrieving user profile and storing it in db.
     * Changes the status of logged in if retrieving of a user profile was a success or failure.
     */
    private void getProfile() {
        RetrofitClient.getClient().getAccount(UserCredentials.getInstance(getApplication()).getSessionId().session()).enqueue(new Callback<Account>() {
            @Override
            public void onResponse(Call<Account> call, Response<Account> response) {
                if (response.isSuccessful()) {
                    UserCredentials.getInstance(getApplication()).setAccount(response.body());
                    isLoggedIn.setValue(true);
                } else {
                    isLoggedIn.setValue(false);
                }
            }

            @Override
            public void onFailure(Call<Account> call, Throwable t) {
                isLoggedIn.setValue(false);
            }
        });
    }
}
