package com.runit.moviesmvvmmockup.utils;

import android.app.AlertDialog;
import android.content.Context;
import android.support.annotation.Nullable;
import android.widget.Toast;

import com.runit.moviesmvvmmockup.R;
import com.runit.moviesmvvmmockup.ui.login.LoginActivity;

/**
 * Created by Radovan Ristovic on 3/29/2018.
 * Quantox.com
 * radovanr995@gmail.com
 */

/**
 * UI Utils class for showing dialogs and toasts.
 */
public class UIUtil {
    public static void showToast(Context context, String msg) {
        if (context != null)
            Toast.makeText(context, msg, Toast.LENGTH_LONG).show();
    }

    public static void showShortToast(Context context, String msg) {
        if (context != null)
            Toast.makeText(context, msg, Toast.LENGTH_SHORT).show();
    }

    public static void showLoginDialog(Context context, @Nullable String msg) {
        String message = msg != null ? msg : context.getString(R.string.please_login_msg);
        AlertDialog dialog = new AlertDialog.Builder(context)
                .setTitle("Not logged in")
                .setMessage(message)
                .setPositiveButton("Login", (dialog1, which) -> {
                    LoginActivity.startActivity(context);
                    dialog1.dismiss();
                })
                .setNegativeButton("Cancel", (dialog1, which) -> dialog1.dismiss())
                .create();
        dialog.show();
    }
}
