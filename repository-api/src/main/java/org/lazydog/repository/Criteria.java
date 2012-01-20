package org.lazydog.repository;

import java.util.List;
import org.lazydog.repository.criterion.Criterion;


/**
 * Criteria.
 * 
 * @author  Ron Rickard
 */
public interface Criteria<T> {

    /**
     * Add a restriction criterion.
     *
     * @param  criterion  the restriction criterion.
     *
     * @return  the criteria.
     */
    Criteria<T> add(Criterion criterion);

    /**
     * Add restriction criterions.
     *
     * @param  criterions  the restriction criterions.
     *
     * @return  the criteria.
     */
    Criteria<T> add(List<Criterion> criterions);

    /**
     * Add a order criterion.
     *
     * @param  criterion  the order criterion.
     *
     * @return  the criteria.
     */
    Criteria<T> addOrder(Criterion criterion);

    /**
     * Add order criterions.
     *
     * @param  criterions  the order criterions.
     *
     * @return  the criteria.
     */
    Criteria<T> addOrders(List<Criterion> criterions);

    /**
     * Check if an order criterion exists.
     *
     * @return  true if an order criterion exists, otherwise false.
     */
    boolean orderExists();

    /**
     * Check if a restriction criterion exists.
     *
     * @return  true if a restriction criterion exists, otherwise false.
     */
    boolean restrictionExists();
}
