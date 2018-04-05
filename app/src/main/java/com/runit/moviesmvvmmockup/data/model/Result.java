package com.runit.moviesmvvmmockup.data.model;


import android.support.annotation.Nullable;

import com.runit.moviesmvvmmockup.data.exception.ErrorBundle;

/**
 * Class used for wrapping the model for transferring it between layers, or {@link ErrorBundle} object if there has been an error while retrieving the model.
 * It contains status indicating if retrieving the model is success or if there has been an error.
 *
 * @param <T> Type of the model in the wrapper.
 */
public class Result<T> {
    private T model;
    private ErrorBundle error;

    /**
     * Constructor for successful response. If param is null, this wrapper will set to be unsuccessful with message {@link com.runit.moviesmvvmmockup.utils.ErrorMessage#DEFAULT_ERROR}.
     *
     * @param data model to be wrapped.
     */
    public Result(T data) {
        if (data == null) {
            error = ErrorBundle.defaulError();
        }
        this.model = data;
    }

    /**
     * Constructor for failed response.
     *
     * @param bundle ErrorBundle holding the exception.
     */
    public Result(ErrorBundle bundle) {
        this.error = bundle;
    }


    /**
     * Indicates if retrial of the model was a success.
     *
     * @return true is model is present in the wrapper.
     */
    public boolean isSuccess() {
        return model != null;
    }

    /**
     * Gets an error object contained in this wrapper.
     *
     * @return {@link ErrorBundle} object with error message.
     */
    public ErrorBundle error() {
        return error;
    }

    /**
     * Return model contained in this wrapper.
     *
     * @return model object if {@link #isSuccess()} returns true, NULL otherwise.
     */
    public T get() {
        return model;
    }
}
