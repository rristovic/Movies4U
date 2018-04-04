package com.runit.moviesmvvmmockup.utils.exception;


/**
 * Class for holding error message and status.
 */
public class ErrorBundle {
    private String message;
    // TODO : implement status
    private int status;

    public ErrorBundle(String message, int status) {
        this.message = message;
        this.status = status;
    }

    public ErrorBundle(String message) {
        this(message, -1);
    }

    public String getMessage() {
        return message;
    }

    public int getStatus() {
        return status;
    }

    public static class ErrorMessage {
        public static final String SERVER_ERROR = "Unable to connect to the server.";
    }
}
