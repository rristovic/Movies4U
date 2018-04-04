package com.runit.moviesmvvmmockup.utils.exception;

/**
 * Created by Radovan Ristovic on 3/29/2018.
 * Quantox.com
 * radovanr995@gmail.com
 */

/**
 * General exceptions to throw indicating wrong use of the code.
 */
public class ApplicationException extends RuntimeException {
    public ApplicationException(String msg) {
        super(msg);
    }
}
