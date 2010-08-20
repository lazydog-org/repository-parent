package org.lazydog.data.access;

import java.util.List;
import java.util.Map;
import org.lazydog.data.access.criterion.Criterion;


/**
 * Criteria.
 * 
 * @author  Ron Rickard
 */
public interface Criteria<T> {

    public Criteria<T> add(Criterion criterion);

    public Criteria<T> add(List<Criterion> criterions);

    public Criteria<T> addOrder(Criterion criterion);

    public Criteria<T> addOrders(List<Criterion> criterion);

    public Map<String, Object> getParameters();

    public String getQlString();
}
