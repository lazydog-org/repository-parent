package org.lazydog.repository.ldap.internal;

import java.io.Serializable;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


/**
 * Directory exception.
 *
 * @author  Ron Rickard
 */
public final class DirectoryException extends LoggableException implements Serializable {

    private static final long serialVersionUID = 1L;
    private String dn;
    private Logger logger = LoggerFactory.getLogger(this.getClass());

    /**
     * Constructs a new exception with no message.
     * 
     * @param  dn  the distinguished name.
     */
    public DirectoryException(final String dn) {
        super();
        this.dn = dn;
        
        if (getLogState()) {
            logger.error(this.getMessage());
        }
    }

    /**
     * Constructs a new exception with the specified message.
     *
     * @param  message  the message.
     * @param  dn       the distinguished name.
     */
    public DirectoryException(final String message, final String dn) {
        super(message);
        this.dn = dn;
        
        if (getLogState()) {
            logger.error(this.getMessage());
        }
    }

    /**
     * Constructs a new exception with the specified message and cause.
     *
     * @param  message  the message.
     * @param  cause    the cause.
     * @param  dn       the distinguished name.
     */
    public DirectoryException(final String message, final Throwable cause, final String dn) {
        super(message, cause);
        this.dn = dn;
        
        if (getLogState()) {
            logger.error(this.getMessage());
        }
    }

    /**
     * Constructs a new exception with the specified cause.
     *
     * @param  cause  the cause.
     * @param  dn     the distinguished name.
     */
    public DirectoryException(final Throwable cause, final String dn) {
        super(cause);
        this.dn = dn;
        
        if (getLogState()) {
            logger.error(this.getMessage());
        }
    }
    
    /**
     * Get the distinguished name.
     *
     * @return  the distinguished name.
     */
    public String getDn() {
        return this.dn;
    }
}
