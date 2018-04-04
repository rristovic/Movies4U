package com.runit.moviesmvvmmockup.utils.exception;


/**
 * Interface to implement when an error has occurred while fetching new data.
 */
public interface ErrorListener {
    /**
     * Called when an error has occurred.
     *
     * @param error {@link ErrorBundle} object containing a message.
     */
    void onErrorCallback(ErrorBundle error);
}
