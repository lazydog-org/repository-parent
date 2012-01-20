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
