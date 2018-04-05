package com.runit.moviesmvvmmockup.utils.exception;


/**
 * General exceptions to throw indicating wrong use of the code.
 */
public class ApplicationException extends RuntimeException {
    public ApplicationException(String msg) {
        super(msg);
    }
}
