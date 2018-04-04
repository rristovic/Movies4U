package com.runit.moviesmvvmmockup.utils.exception;

/**
 * Created by Radovan Ristovic on 4/4/2018.
 * Quantox.com
 * radovanr995@gmail.com
 */

/**
 * General exception in repository data layer.
 */
public class RepositoryException extends ApplicationException {
    public RepositoryException(String msg) {
        super(msg);
    }
}
