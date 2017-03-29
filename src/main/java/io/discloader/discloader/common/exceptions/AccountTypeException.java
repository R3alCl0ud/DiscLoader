package io.discloader.discloader.common.exceptions;

import io.discloader.discloader.network.json.ExceptionJSON;

public class AccountTypeException extends RuntimeException {

    private static final long serialVersionUID = -834830576849056357L;

    public AccountTypeException(ExceptionJSON data) {
        super(data.message);
    }

    /**
     * @param message
     */
    public AccountTypeException(String message) {
        super(message);
    }

    /**
     * @param cause
     */
    public AccountTypeException(Throwable cause) {
        super(cause);
    }

    /**
     * @param message
     * @param cause
     */
    public AccountTypeException(String message, Throwable cause) {
        super(message, cause);
    }

    /**
     * @param message
     * @param cause
     * @param enableSuppression
     * @param writableStackTrace
     */
    public AccountTypeException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }

}
