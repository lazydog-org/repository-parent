/**
 * Copyright 2010-2013 lazydog.org.
 *
 * This file is part of repository.
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
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
