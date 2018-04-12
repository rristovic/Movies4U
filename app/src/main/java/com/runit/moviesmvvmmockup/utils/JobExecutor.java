package com.runit.moviesmvvmmockup.utils;

import android.os.Looper;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;


/**
 * Helper class for executing tasks in background and main thread.
 */
public class JobExecutor {
    private static final int NUM_OF_THREADS = 5;
    private static ExecutorService executorService;

    static {
        executorService = Executors.newFixedThreadPool(NUM_OF_THREADS);
    }

    /**
     * Method for executing provided task in background thread.
     *
     * @param command {@link Runnable} task to be executed.
     */
    public static void execute(Runnable command) {
        executorService.execute(command);
    }

    /**
     * Method for executing provided task in Main/UI thread.
     *
     * @param command {@link Runnable} task to be executed.
     */
    public static void executeOnUI(Runnable command) {
        final android.os.Handler UIHandler = new android.os.Handler(Looper.getMainLooper());
        UIHandler.post(command);
    }
}
