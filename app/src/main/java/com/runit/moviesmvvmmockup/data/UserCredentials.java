package com.runit.moviesmvvmmockup.data;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.SharedPreferences;

import com.google.gson.Gson;
import com.runit.moviesmvvmmockup.data.model.Session;
import com.runit.moviesmvvmmockup.data.model.Token;

/**
 * Created by Radovan Ristovic on 3/30/2018.
 * Quantox.com
 * radovanr995@gmail.com
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

    // Shared preference keys
    private final String PREF_NAME = "user_credentials";
    private final String KEY_TOKEN = "user_request_token";
    private final String KEY_SESSION = "user_session_id";

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
     * @param token new token to be used for future calls.
     */
    public void setToken(Token token) {
        this.mRequestToken = token;
        getPreferences().edit().putString(KEY_TOKEN, new Gson().toJson(token)).apply();
    }

    /**
     * Set current user session ID that is used for authorizing network calls.
     * @param sessionId new session ID to be used for future calls.
     */
    public void setSessionId(Session sessionId) {
        this.mSessionId = sessionId;
        getPreferences().edit().putString(KEY_SESSION, new Gson().toJson(sessionId)).apply();
    }

    /**
     * Retrieves session ID object that is used in protected network calls to authorize the request.
     * @return {@link Session} object ready for future network calls, null if there is no session saved.
     */
    public Session getSessionId() {
        if(mSessionId != null) {
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
}
