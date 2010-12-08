package org.lazydog.repository;

import java.util.List;
import java.util.Map;
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
    public Criteria<T> add(Criterion criterion);

    /**
     * Add restriction criterions.
     *
     * @param  criterions  the restriction criterions.
     *
     * @return  the criteria.
     */
    public Criteria<T> add(List<Criterion> criterions);

    /**
     * Add a join criterion.
     *
     * @param  criterion  the join criterion.
     *
     * @return  the criteria.
     */
    public Criteria<T> addJoin(Criterion criterion);

    /**
     * Add join criterions.
     *
     * @param  criterions  the join criterions.
     *
     * @return  the criteria.
     */
    public Criteria<T> addJoins(List<Criterion> criterions);

    /**
     * Add a order criterion.
     *
     * @param  criterion  the order criterion.
     *
     * @return  the criteria.
     */
    public Criteria<T> addOrder(Criterion criterion);

    /**
     * Add order criterions.
     *
     * @param  criterions  the order criterions.
     *
     * @return  the criteria.
     */
    public Criteria<T> addOrders(List<Criterion> criterions);

    /**
     * Get the hints.
     *
     * @return  the hints.
     */
    public Map<Object, String> getHints();

    /**
     * Get the parameters.
     *
     * @return  the parameters.
     */
    public Map<String, Object> getParameters();

    /**
     * Get the query language string.
     *
     * @return  the query language string.
     */
    public String getQlString();

    /**
     * Check if an join criterion exists.
     *
     * @return  true if an join criterion exists, otherwise false.
     */
    public boolean joinExists();

    /**
     * Check if an order criterion exists.
     *
     * @return  true if an order criterion exists, otherwise false.
     */
    public boolean orderExists();

    /**
     * Check if a restriction criterion exists.
     *
     * @return  true if a restriction criterion exists, otherwise false.
     */
    public boolean restrictionExists();
}
