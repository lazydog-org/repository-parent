package org.lazydog.repository.ldap.internal;

import java.util.List;
import java.util.Set;
import org.lazydog.repository.Criteria;
import org.lazydog.repository.Entity;


/**
 * Validator.
 * 
 * @author  Ron Rickard
 */
public final class Validator {

    /**
     * Private constructor.
     */
    private Validator() {    
    }
    
    /**
     * Check for a valid criteria.
     * 
     * @param  criteria  the criteria.
     * 
     * @throws  IllegalArgumentException  if the criteria is null or invalid.
     */
    public static void validCriteria(Criteria<?> criteria) {

        // Check if the criteria is null.
        if (criteria == null) {
            throw new IllegalArgumentException("The criteria is null.");
        }

        // Check if the criteria is not a CriteriaImpl.
        else if (!CriteriaImpl.class.isInstance(criteria)) {
            throw new IllegalArgumentException("The criteria is not the correct type.");
        }
    }
	
    /**
     * Check for a valid entity.
     * 
     * @param  entity  the entity.
     * 
     * @throws  IllegalArgumentException  if the entity is null or invalid.
     */
    public static <T> void validEntity(T entity) {
        validEntity(entity, null);
    }
	
    /**
     * Check for a valid entity.
     * 
     * @param  entity                  the entity.
     * @param  supportedEntityClasses  the supported entity classes.
     * 
     * @throws  IllegalArgumentException  if the entity is null or invalid.
     */
    @SuppressWarnings("unchecked")
    public static <T> void validEntity(T entity, Set<Class<?>> supportedEntityClasses) {

        // Check if the entity is null.
        if (entity == null) {
            throw new IllegalArgumentException("the entity is null.");
        }

        // Validate the entity class and ID.
        validEntityClass(entity.getClass());
        validId(((Entity)entity).getId());
    }
	
    /**
     * Check for a valid entity class.
     * 
     * @param  entityClass  the entity class.
     * 
     * @throws  IllegalArgumentException  if the entity class is null or invalid.
     */
    public static void validEntityClass(Class<?> entityClass) {
        validEntityClass(entityClass, null);
    }
	
    /**
     * Check for a valid entity class.
     * 
     * @param  entityClass             the entity class.
     * @param  supportedEntityClasses  the supported entity classes.
     * 
     * @throws  IllegalArgumentException  if the entity class is null or invalid.
     */
    public static void validEntityClass(Class<?> entityClass, Set<Class<?>> supportedEntityClasses) {

        // Check if the entity class is null.
        if (entityClass == null) {
            throw new IllegalArgumentException("The entity class is null.");
        }

        // Check if the entity class is not a subclass of Entity.
        else if (!Entity.class.isAssignableFrom(entityClass)) {
            throw new IllegalArgumentException("The entity " + entityClass + " does not extend " + Entity.class + ".");
        }

        // Check if there are supported entity classes.
        else if (supportedEntityClasses != null && supportedEntityClasses.size() > 0) {

            // Loop through the supported entity classes.
            for (Class<?> supportedEntityClass : supportedEntityClasses) {

                // Check if the entity class is supported.
                if (entityClass == supportedEntityClass) {
                    return;
                }
            }

            throw new IllegalArgumentException("The entity " + entityClass + " is not a supported entity.");
        }
    }
	
    /**
     * Check for a valid ID.
     * 
     * @param  id  the ID.
     * 
     * @throws  IllegalArgumentException  if the ID is null or invalid.
     */
    public static <T> void validId(T id) {

        // Check if the ID is null.
        if (id == null) {
            throw new IllegalArgumentException("The ID is null.");
        }

        // Check if the ID is not a String.
        else if (!String.class.isInstance(id)) {
            throw new IllegalArgumentException("The ID is not a String.");
        }
    }
	
    /**
     * Check for a valid list.
     * 
     * @param  list  the list.
     * 
     * @throws  IllegalArgumentException  if the list is null or invalid.
     */
    public static <T> void validList(List<T> list) {

        // Check if the list is null.
        if (list == null) {
            throw new IllegalArgumentException("The list is null.");
        }

        // Check if the list is empty.
        else if (list.size() <= 0) {
            throw new IllegalArgumentException("The list is empty.");
        }
    }
}
