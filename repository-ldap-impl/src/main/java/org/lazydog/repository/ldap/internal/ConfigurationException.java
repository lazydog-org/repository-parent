package org.lazydog.repository.ldap.internal;

import java.io.Serializable;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


/**
 * Configuration exception.
 *
 * @author  Ron Rickard
 */
public final class ConfigurationException extends LoggableException implements Serializable {

    private static final long serialVersionUID = 1L;
    private Logger logger = LoggerFactory.getLogger(this.getClass());

    /**
     * Constructs a new exception with no message.
     */
    public ConfigurationException() {
        super();

        if (getLogState()) {
            logger.error(this.getMessage());
        }
    }

    /**
     * Constructs a new exception with the specified message.
     *
     * @param  message  the message.
     */
    public ConfigurationException(String message) {
        super(message);
        
        if (getLogState()) {
            logger.error(this.getMessage());
        }
    }

    /**
     * Constructs a new exception with the specified message and cause.
     *
     * @param  message  the message.
     * @param  cause    the cause.
     */
    public ConfigurationException(String message, Throwable cause) {
        super(message, cause);
        
        if (getLogState()) {
            logger.error(this.getMessage());
        }
    }

    /**
     * Constructs a new exception with the specified cause.
     *
     * @param  cause  the cause.
     */
    public ConfigurationException(Throwable cause) {
        super(cause);
        
        if (getLogState()) {
            logger.error(this.getMessage());
        }
    }
}
