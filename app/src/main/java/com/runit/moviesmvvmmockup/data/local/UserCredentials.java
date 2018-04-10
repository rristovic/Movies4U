package com.runit.moviesmvvmmockup.data.local;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.SharedPreferences;

import com.google.gson.Gson;
import com.runit.moviesmvvmmockup.data.model.Account;
import com.runit.moviesmvvmmockup.data.model.Session;
import com.runit.moviesmvvmmockup.data.model.Token;


/**
 * Locally stored user credentials. Saves {@link Session} and {@link Token} models onto the disk.
 */
public class UserCredentials {

    // Current instance
    private static UserCredentials mInstance;
    // Application context
    private final Context mContext;

    // Current user token
    private Token mRequestToken;
    // Current user session
    private Session mSessionId;
    // Current user account
    private Account mAccount;

    // Shared preference keys
    private final String PREF_NAME = "user_credentials";
    private final String KEY_TOKEN = "user_request_token";
    private final String KEY_SESSION = "user_session_id";
    private final String KEY_ACCOUNT = "user_account";

    public static UserCredentials getInstance(Context context) {
        if (mInstance == null) {
            synchronized (UserCredentials.class) {
                if (mInstance == null) {
                    mInstance = new UserCredentials(context.getApplicationContext());
                }
            }
        }
        return mInstance;
    }

    private UserCredentials(Context context) {
        this.mContext = context;
    }

    /**
     * Retrieves current user account.
     *
     * @return {@link Account} object, null if there is no account saved.
     */
    public Account getUserAccount() {
        if (mAccount != null)
            return mAccount;
        String result = getPreferences().getString(KEY_ACCOUNT, "");
        mAccount = result.equals("") ? null : new Gson().fromJson(result, Account.class);
        return mAccount;
    }

    /**
     * Set current user account.
     *
     * @param account new user account.
     */
    public void setAccount(Account account) {
        this.mAccount = account;
        getPreferences().edit().putString(KEY_ACCOUNT, new Gson().toJson(account)).apply();
    }

    /**
     * Retrieves request token that is used for retrieving sessionID.
     *
     * @return {@link Token} object, null if there is no token saved.
     */
    public Token getRequestToken() {
        if (mRequestToken != null)
            return mRequestToken;
        String result = getPreferences().getString(KEY_TOKEN, "");
        mRequestToken = result.equals("") ? null : new Gson().fromJson(result, Token.class);
        return mRequestToken;
    }

    /**
     * Set current user token that is used for retrieving sessionID.
     *
     * @param token new token to be used for future calls.
     */
    public void setToken(Token token) {
        this.mRequestToken = token;
        getPreferences().edit().putString(KEY_TOKEN, new Gson().toJson(token)).apply();
    }

    /**
     * Set current user session ID that is used for authorizing network calls.
     *
     * @param sessionId new session ID to be used for future calls.
     */
    public void setSessionId(Session sessionId) {
        this.mSessionId = sessionId;
        getPreferences().edit().putString(KEY_SESSION, new Gson().toJson(sessionId)).apply();
    }

    /**
     * Retrieves session ID object that is used in protected network calls to authorize the request.
     *
     * @return {@link Session} object ready for future network calls, null if there is no session saved.
     */
    public Session getSessionId() {
        if (mSessionId != null) {
            return mSessionId;
        }
        String result = getPreferences().getString(KEY_SESSION, "");
        mSessionId = result.equals("") ? null : new Gson().fromJson(result, Session.class);
        return mSessionId;
    }

    /**
     * Deletes current user session token.
     */
    @SuppressLint("ApplySharedPref")
    public void clearCredentials() {
        this.mRequestToken = null;
        this.mSessionId = null;
        getPreferences().edit().clear().commit();
    }

    /**
     * Retrieves {@link SharedPreferences} preference used for storing user credentials.
     *
     * @return private preference for this application.
     */
    private SharedPreferences getPreferences() {
        return mContext.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
    }

    /**
     * Checks if user credentials are currently present in order to send authorized network requests.
     *
     * @return true is credentials are present.
     */
    public boolean isLoggedIn() {
        return this.getSessionId() != null && this.getUserAccount() != null;
    }
}
