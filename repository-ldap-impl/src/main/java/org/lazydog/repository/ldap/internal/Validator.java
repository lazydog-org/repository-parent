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
    public static void validCriteria(final Criteria<?> criteria) {

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
    public static <T> void validEntity(final T entity) {
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
    public static <T> void validEntity(final T entity, final Set<Class<?>> supportedEntityClasses) {

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
    public static void validEntityClass(final Class<?> entityClass) {
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
    public static void validEntityClass(final Class<?> entityClass, final Set<Class<?>> supportedEntityClasses) {

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
    public static <T> void validId(final T id) {

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
    public static <T> void validList(final List<T> list) {

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
