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
    public EntityFactoryException(Class<?> entityClass, String methodName) {
        super();
        this.entityClass = entityClass;
        this.methodName = methodName;
        
        if (logState) {
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
    public EntityFactoryException(String message, Class<?> entityClass, String methodName) {
        super(message);
        this.entityClass = entityClass;
        this.methodName = methodName;
        
        if (logState) {
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
    public EntityFactoryException(String message, Throwable cause, Class<?> entityClass, String methodName) {
        super(message, cause);
        this.entityClass = entityClass;
        this.methodName = methodName;
        
        if (logState) {
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
    public EntityFactoryException(Throwable cause, Class<?> entityClass, String methodName) {
        super(cause);
        this.entityClass = entityClass;
        this.methodName = methodName;
        
        if (logState) {
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
