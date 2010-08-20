package org.lazydog.data.access.internal;

import org.lazydog.data.access.Criteria;
import org.lazydog.data.access.CriteriaFactory;


/**
 * Criteria factory implemented using the Java Persistence API.
 * 
 * @author  Ron Rickard
 */
public class CriteriaFactoryImpl extends CriteriaFactory {

    /**
     * Create the criteria.
     *
     * @param  entityClass  the entity class.
     *
     * @return  the criteria.
     */
    @Override
    public <T> Criteria<T> createCriteria(Class<T> entityClass) {
        return new CriteriaImpl<T>(entityClass);
    }
}
