package org.lazydog.repository;

import java.util.ArrayList;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.TypedQuery;
import org.lazydog.repository.internal.CriteriaImpl;


/**
 * Abstract repository implemented using the Java Persistence API.
 * 
 * @author  Ron Rickard
 */
public abstract class AbstractRepository implements Repository {

    private EntityManager entityManager;

    /**
     * Create the query from the criteria.
     *
     * @param  entityClass  the entity class.
     * @param  criteria     the criteria.
     * 
     * @return  the query.
     *
     * @throws  IllegalArgumentException  if the entity class or criteria are invalid.
     */
    private <T> TypedQuery<T> createQuery(Class<T> entityClass, Criteria<T> criteria) {

        // Declare.
        TypedQuery<T> query;

        // Check if the entity class is null.
        if (entityClass == null) {
            throw new IllegalArgumentException("The entity class is invalid.");
        }

        // Check if the criteria is null.
        if (criteria == null) {
            throw new IllegalArgumentException("The criteria is invalid.");
        }

        // Create the query from the criteria query language string.
        query = this.entityManager.createQuery(criteria.getQlString(), entityClass);

        // Loop through the parameters.
        for(String key : criteria.getParameters().keySet()) {

            // Set the query parameters.
            query.setParameter(key, criteria.getParameters().get(key));
        }

        return query;
    }

    /**
     * Find the entity.
     *
     * @param  entityClass  the entity class.
     * @param  id           the ID.
     * 
     * @return  the entity.
     */
    @Override
    public <T> T find(Class<T> entityClass, Integer id) {
        return this.entityManager.find(entityClass, id);
    }
      
    /**
     * Find the entity.
     *
     * @param  entityClass  the entity class.
     * @param  criteria     the criteria.
     * 
     * @return  the entity.
     */
    @Override
    public <T> T find(Class<T> entityClass, Criteria<T> criteria) {

        // Declare.
        T entity;

        // Initialize.
        entity = null;
        
        try {

            // Get the entity.
            entity = this.createQuery(entityClass, criteria).getSingleResult();
        }
        catch(NoResultException e) {
            // Ignore.
        }

        return entity;
    }

    /**
     * Find the list of entities.
     *
     * @param  entityClass  the entity class.
     * 
     * @return  the list of entities.
     */
    @Override
    public <T> List<T> findList(Class<T> entityClass) {
        return this.findList(entityClass, this.getCriteria(entityClass));
    }

    /**
     * Find the list of entities.
     *
     * @param  entityClass  the entity class.
     * @param  criteria     the criteria.
     *
     * @return  the list of entities.
     */
    @Override
    public <T> List<T> findList(Class<T> entityClass, Criteria<T> criteria) {
        return this.createQuery(entityClass, criteria).getResultList();
    }

    /**
     * Get the criteria.
     *
     * @param  entityClass  the entity class.
     *
     * @return  the criteria.
     */
    @Override
    public <T> Criteria<T> getCriteria(Class<T> entityClass) {
        return new CriteriaImpl<T>(entityClass, this.entityManager);
    }

    /**
     * Get the entity manager.
     * 
     * @return  the entity manager.
     */
    protected EntityManager getEntityManager() {
        return this.entityManager;
    }

    /**
     * Persist the entity.
     *
     * @param  entity  the entity.
     *
     * @return  the persisted entity.
     */
    @Override
    public <T> T persist(T entity) {

        // Declare.
        T persistedEntity;

        // Persist the entity.
        persistedEntity = this.entityManager.merge(entity);
        this.entityManager.flush();

        return persistedEntity;
    }

    /**
     * Persist the list of entities.
     *
     * @param  entities  the entities.
     *
     * @return  the persisted list of entities.
     */
    @Override
    public <T> List<T> persistList(List<T> entities) {
        
        // Declare.
        List<T> persistedEntities;
        
        // Initialize.
        persistedEntities = new ArrayList<T>();

        // Loop through the entities.
        for (T entity : entities) {

            // Declare.
            T persistedEntity;

            // Persist the entity.
            persistedEntity = this.persist(entity);

            // Add the persisted entity.
            persistedEntities.add(persistedEntity);
        }

        return persistedEntities;
    }

    /**
     * Remove the entity specified by ID.
     *
     * @param  entityClass  the entity class.
     * @param  id           the ID.
     */
    @Override
    public <T> void remove(Class<T> entityClass, Integer id) {

        // Declare.
        T entity;

        // Get the entity.
        entity = this.entityManager.getReference(entityClass, id);

        // Remove the entity.
        this.entityManager.remove(entity);
    }

    /**
     * Remove the entities specified by the list of IDs.
     *
     * @param  entityClass  the entity class.
     * @param  ids          the IDs.
     */
    @Override
    public <T> void removeList(Class<T> entityClass, List<Integer> ids) {

        // Loop through the IDs.
        for (Integer id: ids) {

            // Remove the entity.
            this.remove(entityClass, id);
        }
    }

    /**
     * Set the entity manager.
     *
     * @param  entityManager  the entity manager.
     */
    protected void setEntityManager(EntityManager entityManager) {
        this.entityManager = entityManager;
    }
}