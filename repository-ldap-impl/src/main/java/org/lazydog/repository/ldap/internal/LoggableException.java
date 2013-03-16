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
    public LoggableException(final String message) {
        super(message);
    }

    /**
     * Constructs a new exception with the specified message and cause.
     *
     * @param  message  the message.
     * @param  cause    the cause.
     */
    public LoggableException(final String message, final Throwable cause) {
        super(message, cause);
    }

    /**
     * Constructs a new exception with the specified cause.
     *
     * @param  cause  the cause.
     */
    public LoggableException(final Throwable cause) {
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
    public static void setLogState(final boolean logState) {
        LoggableException.logState = logState;
    }
}
