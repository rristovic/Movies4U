package com.runit.moviesmvvmmockup.utils;

import android.os.Looper;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;


/**
 * Helper class for executing tasks in background and main thread.
 */
public class JobExecutor {
    private static final int NUM_OF_THREADS = 5;
    private static ExecutorService executorService;
    private static ScheduledExecutorService scheduledExecutorService;

    static {
        executorService = Executors.newFixedThreadPool(NUM_OF_THREADS);
        scheduledExecutorService = Executors.newSingleThreadScheduledExecutor();
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
     * Method for executing provided task on Main/UI thread.
     *
     * @param command {@link Runnable} task to be executed.
     */
    public static void executeOnUI(Runnable command) {
        final android.os.Handler UIHandler = new android.os.Handler(Looper.getMainLooper());
        UIHandler.post(command);
    }

    /**
     * Method for executing provided task on Main/UI thread after period of time has elapsed.
     * @param command {@link Runnable} task to be executed.
     * @param ms period of time after which task will be executed in {@link TimeUnit#MILLISECONDS}.
     */
    public static void executeOnUI(Runnable command, int ms) {
        scheduledExecutorService.schedule(() -> {
            executeOnUI(command);
        }, ms, TimeUnit.MILLISECONDS);
    }
}
