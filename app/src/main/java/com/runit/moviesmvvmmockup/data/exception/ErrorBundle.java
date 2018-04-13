package com.runit.moviesmvvmmockup.data.exception;


import com.runit.moviesmvvmmockup.utils.exception.ErrorMessage;

/**
 * Class for wrapping an exception.
 */
public class ErrorBundle {
    private Exception exception;

    public ErrorBundle(Exception exception) {
        this.exception = exception;
    }

    public String getMessage() {
        return this.exception != null ? this.exception.getMessage() : ErrorMessage.DEFAULT_ERROR;
    }

    /**
     * Return new object with {@link RuntimeException} as default exception with provided message object.
     * @param msg string message to be set as error message.
     * @return newly created object. 
     */
    public static ErrorBundle error(String msg) {
        return new ErrorBundle(new RuntimeException(msg));
    }

    /**
     * Creates default server error with message {@link ErrorMessage#SERVER_ERROR} and {@link RuntimeException}.
     *
     * @return newly created ErrorBundle object.
     */
    public static ErrorBundle defaultServerError() {
        return new ErrorBundle(new RuntimeException(ErrorMessage.SERVER_ERROR));
    }

    /**
     * Creates default server connection error with message {@link ErrorMessage#CONNECTION_ERROR} and {@link RuntimeException}.
     *
     * @return newly created ErrorBundle object.
     */
    public static ErrorBundle defaultConnectionError() {
        return new ErrorBundle(new RuntimeException(ErrorMessage.CONNECTION_ERROR));
    }

    /**
     * Creates default error with message {@link ErrorMessage#DEFAULT_ERROR} and {@link RuntimeException}.
     *
     * @return newly created ErrorBundle object.
     */
    public static ErrorBundle defaultError() {
        return new ErrorBundle(new RuntimeException(ErrorMessage.DEFAULT_ERROR));
    }

    /**
     * Creates default not logged in error with message {@link ErrorMessage#NOT_LOGGED_IN} and {@link RuntimeException}.
     *
     * @return newly created ErrorBundle object.
     */
    public static ErrorBundle defaulNotLoggedInError() {
        return new ErrorBundle(new RuntimeException(ErrorMessage.NOT_LOGGED_IN));
    }
}
