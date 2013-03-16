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
package org.lazydog.repository;

import java.util.List;

/**
 * Repository.
 *
 * @author  Ron Rickard
 */
public interface Repository {

    /**
     * Find the entity.
     *
     * @param  entityClass  the entity class.
     * @param  id           the ID.
     *
     * @return  the entity.
     */
    <T,U> T find(Class<T> entityClass, U id);

    /**
     * Find the entity.
     *
     * @param  entityClass  the entity class.
     * @param  criteria     the criteria.
     *
     * @return  the entity.
     */
   <T> T find(Class<T> entityClass, Criteria<T> criteria);

    /**
     * Find the list of entities.
     *
     * @param  entityClass  the entity class.
     *
     * @return  the list of entities.
     */
    <T> List<T> findList(Class<T> entityClass);

    /**
     * Find the list of entities.
     *
     * @param  entityClass  the entity class.
     * @param  criteria     the criteria.
     * 
     * @return  the list of entities.
     */
    <T> List<T> findList(Class<T> entityClass, Criteria<T> criteria);

    /**
     * Get the criteria.
     * 
     * @param  entityClass  the entity class.
     * 
     * @return  the criteria.
     */
    <T> Criteria<T> getCriteria(Class<T> entityClass);

    /**
     * Persist the entity.
     *
     * @param  entity  the entity.
     *
     * @return  the persisted entity.
     */
    <T> T persist(T entity);

    /**
     * Persist the list of entities.
     *
     * @param  entities  the entities.
     *
     * @return  the persisted list of entities.
     */
    <T> List<T> persistList(List<T> entities);

    /**
     * Remove the entity.
     *
     * @param  entityClass  the entity class.
     * @param  id           the ID.
     */
    <T,U> void remove(Class<T> entityClass, U id);

    /**
     * Remove the entities by the list of IDs.
     *
     * @param  entityClass  the entity class.
     * @param  ids          the IDs.
     */
    <T,U> void removeList(Class<T> entityClass, List<U> ids);
}
