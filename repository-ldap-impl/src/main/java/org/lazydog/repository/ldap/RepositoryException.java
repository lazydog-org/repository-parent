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
package org.lazydog.repository.ldap;

import java.io.Serializable;

/**
 * Repository exception.
 *
 * @author  Ron Rickard
 */
public final class RepositoryException extends RuntimeException implements Serializable {

    private static final long serialVersionUID = 1L;
    private Class<?> entityClass;

    /**
     * Constructs a new exception with no message.
     * 
     * @param  entityClass  the entity class.
     */
    public RepositoryException(final Class<?> entityClass) {
        super();
        this.entityClass = entityClass;
    }

    /**
     * Constructs a new exception with the specified message.
     *
     * @param  message          the message.
     * @param  entityClassName  the entity class.
     */
    public RepositoryException(final String message, final Class<?> entityClass) {
        super(message);
        this.entityClass = entityClass;
    }

    /**
     * Constructs a new exception with the specified message and cause.
     *
     * @param  message          the message.
     * @param  cause            the cause.
     * @param  entityClassName  the entity class.
     */
    public RepositoryException(final String message, final Throwable cause, final Class<?> entityClass) {
        super(message, cause);
        this.entityClass = entityClass;
    }

    /**
     * Constructs a new exception with the specified cause.
     *
     * @param  cause            the cause.
     * @param  entityClassName  the entity class name.
     */
    public RepositoryException(final Throwable cause, final Class<?> entityClass) {
        super(cause);
        this.entityClass = entityClass;
    }
    
    /**
     * Get the entity class.
     * 
     * @return  the entity class.
     */
    public Class<?> getEntityClass() {
    	return this.entityClass;
    }
}
