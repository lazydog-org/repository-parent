package org.lazydog.data.access;

import java.util.ServiceConfigurationError;
import java.util.ServiceLoader;


/**
 * Criteria factory.
 * 
 * @author  Ron Rickard
 */
public abstract class CriteriaFactory {

    public abstract <T> Criteria<T> createCriteria(Class<T> entityClass);
           
    /**
     * Get an implementation of the criteria factory.
     *
     * @return  an implementation of the criteria factory.
     *
     * @throws  IllegalArgumentException   if not exactly one criteria factory
     *                                     is found.
     * @throws  ServiceConfigurationError  if unable to create the criteria
     *                                     factory due to a provider
     *                                     configuration error.
     */
    public static synchronized CriteriaFactory instance() {

        // Declare.
        CriteriaFactory criteriaFactory;
        ServiceLoader<CriteriaFactory> criteriaFactoryLoader;

        // Initialize.
        criteriaFactory = null;
        criteriaFactoryLoader = ServiceLoader.load(CriteriaFactory.class);

        // Loop through the criteria factory implementations.
        for (CriteriaFactory foundCriteriaFactory : criteriaFactoryLoader) {

            // Check if a criteria factory implementation has not been found.
            if (criteriaFactory == null) {

                // Set the criteria factory.
                criteriaFactory = foundCriteriaFactory;
            }
            else {
                throw new IllegalArgumentException(
                        "More than one criteria factory found.");
            }
        }

        // Check if a criteria factory implementation has not been found.
        if (criteriaFactory == null) {
            throw new IllegalArgumentException(
                    "No criteria factory found.");
        }

        return criteriaFactory;
    }
}
