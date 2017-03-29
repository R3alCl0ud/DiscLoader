package io.discloader.discloader.common.exceptions;

/**
 * @author Perry Berman
 *
 */
public class GuildSyncException extends RuntimeException {

    /**
     * 
     */
    private static final long serialVersionUID = 1540218644989764083L;

    /**
     * 
     */
    public GuildSyncException() {
    }

    /**
     * @param message
     */
    public GuildSyncException(String message) {
        super(message);
    }

    /**
     * @param cause
     */
    public GuildSyncException(Throwable cause) {
        super(cause);
    }

    /**
     * @param message
     * @param cause
     */
    public GuildSyncException(String message, Throwable cause) {
        super(message, cause);
    }

    /**
     * @param message
     * @param cause
     * @param enableSuppression
     * @param writableStackTrace
     */
    public GuildSyncException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }

}
