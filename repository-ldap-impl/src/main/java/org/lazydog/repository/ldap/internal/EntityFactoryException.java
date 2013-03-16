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
 * Entity factory exception.
 *
 * @author  Ron Rickard
 */
public final class EntityFactoryException extends LoggableException implements Serializable {

    private static final long serialVersionUID = 1L;
    private Class<?> entityClass;
    private Logger logger = LoggerFactory.getLogger(this.getClass());
    private String methodName;
    
    /**
     * Constructs a new exception with no message.
     *
     * @param  entityClass  the entity class.
     * @param  methodName   the method name.
     *
     */
    public EntityFactoryException(final Class<?> entityClass, final String methodName) {
        super();
        this.entityClass = entityClass;
        this.methodName = methodName;
        
        if (getLogState()) {
            logger.error(this.getMessage());
        }
    }

    /**
     * Constructs a new exception with the specified message.
     *
     * @param  message      the message.
     * @param  entityClass  the entity class.
     * @param  methodName   the method name.
     */
    public EntityFactoryException(final String message, final Class<?> entityClass, final String methodName) {
        super(message);
        this.entityClass = entityClass;
        this.methodName = methodName;
        
        if (getLogState()) {
            logger.error(this.getMessage());
        }
    }

    /**
     * Constructs a new exception with the specified message and cause.
     *
     * @param  message      the message.
     * @param  cause        the cause.
     * @param  entityClass  the entity class.
     * @param  methodName   the method name.
     */
    public EntityFactoryException(final String message, final Throwable cause, final Class<?> entityClass, final String methodName) {
        super(message, cause);
        this.entityClass = entityClass;
        this.methodName = methodName;
        
        if (getLogState()) {
            logger.error(this.getMessage());
        }
    }

    /**
     * Constructs a new exception with the specified cause.
     *
     * @param  cause        the cause.
     * @param  entityClass  the entity class.
     * @param  methodName   the method name.
     */
    public EntityFactoryException(final Throwable cause, final Class<?> entityClass, final String methodName) {
        super(cause);
        this.entityClass = entityClass;
        this.methodName = methodName;
        
        if (getLogState()) {
            logger.error(this.getMessage());
        }
    }

    /**
     * Get the entity class.
     * 
     * @return  the entity class.
     */
    public Class<?> getEntityClass() {
    	return this.entityClass;
    }

    /**
     * Get the method name.
     *
     * @return  the method name.
     */
    public String getMethodName() {
        return this.methodName;
    }
}
