package org.lazydog.repository.ldap.internal;

import java.io.Serializable;


/**
 * Loggable.
 * 
 * @author  Ron Rickard
 */
public abstract class LoggableException extends Exception implements Serializable {

    private static final long serialVersionUID = 1L;
    private static boolean logState;
	
    /**
     * Constructs a new exception with no message.
     */
    public LoggableException() {
        super();
    }

    /**
     * Constructs a new exception with the specified message.
     *
     * @param  message  the message.
     */
    public LoggableException(String message) {
        super(message);
    }

    /**
     * Constructs a new exception with the specified message and cause.
     *
     * @param  message  the message.
     * @param  cause    the cause.
     */
    public LoggableException(String message, Throwable cause) {
        super(message, cause);
    }

    /**
     * Constructs a new exception with the specified cause.
     *
     * @param  cause  the cause.
     */
    public LoggableException(Throwable cause) {
        super(cause);
    }
    
    /**
     * Get the log state.
     * 
     * @return  true if logging is enable, otherwise false.
     */
    public static boolean getLogState() {
        return logState;
    }
	
    /**
     * Set the logging state.
     * 
     * @param  logState  true to enable logging, otherwise false.
     */
    public static void setLogState(boolean logState) {
        LoggableException.logState = logState;
    }
}
