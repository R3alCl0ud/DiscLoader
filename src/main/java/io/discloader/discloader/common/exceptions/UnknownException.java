package io.discloader.discloader.common.exceptions;

import io.discloader.discloader.network.json.ExceptionJSON;

/**
 * @author Perry Berman
 *
 */
public class UnknownException extends RuntimeException {

    private static final long serialVersionUID = 3158890067864659909L;
    private final int errorCode;

    public UnknownException(ExceptionJSON data) {
        super(data.message);
        errorCode = data.code;
    }

    public int getErrorCode() {
        return errorCode;
    }
}
