package com.runit.moviesmvvmmockup.utils;

import android.content.Context;
import android.widget.Toast;

/**
 * Created by Radovan Ristovic on 3/29/2018.
 * Quantox.com
 * radovanr995@gmail.com
 */

/**
 * UI Utils class for showing dialogs and toasts.
 */
public class UIUtils {
    public static void showToast(Context context, String msg) {
        if (context != null)
            Toast.makeText(context, msg, Toast.LENGTH_LONG).show();
    }

    public static void showShortToast(Context context, String msg) {
        if (context != null)
            Toast.makeText(context, msg, Toast.LENGTH_SHORT).show();
    }
}
