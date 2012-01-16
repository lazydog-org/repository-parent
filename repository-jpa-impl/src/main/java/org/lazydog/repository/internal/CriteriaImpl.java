package org.lazydog.repository.internal;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import javax.persistence.EntityManager;
import javax.persistence.metamodel.Attribute;
import javax.persistence.metamodel.EntityType;
import org.eclipse.persistence.config.QueryHints;
import org.lazydog.repository.Criteria;
import org.lazydog.repository.criterion.Criterion;
import org.lazydog.repository.criterion.JoinOperation;


/**
 * Criteria implemented using the Java Persistence API.
 * 
 * @author  Ron Rickard
 */
public class CriteriaImpl<T> implements Criteria<T>, Serializable {

    private static final long serialVersionUID = 1L;
    
    private String entityAlias;
    private Class<T> entityClass;
    private EntityManager entityManager;
    private Map<Object, String> hints;
    private List<Criterion> joins;
    private StringBuffer joinsStringBuffer;
    private List<Criterion> orders;
    private StringBuffer ordersStringBuffer;
    private Map<String, Object> parameters;
    private List<Criterion> restrictions;
    private StringBuffer restrictionsStringBuffer;

    /**
     * Constructor.
     *
     * @param  entityClass    the entity class.
     * @param  entityManager  the entity manager.
     *
     * @throws  IllegalArgumentException  if the entity class or entity manager are invalid.
     */
    public CriteriaImpl(Class<T> entityClass, EntityManager entityManager) {

        // Check if the entity class is null.
        if (entityClass == null) {
            throw new IllegalArgumentException("The entity class is invalid.");
        }

        // Check if the entity manager is null.
        if (entityManager == null) {
            throw new IllegalArgumentException("The entity manager is invalid.");
        }

        // Set the entity class and entity manager.
        this.entityClass = entityClass;
        this.entityManager = entityManager;

        // Set the entity alias.
        this.entityAlias = this.entityClass.getSimpleName().toLowerCase();

        // Initialize the hints.
        this.hints = new LinkedHashMap<Object, String>();
        
        // Initialize the parameters.
        this.parameters = new LinkedHashMap<String, Object>();

        // Initialize the joins, orders, and restrictions string buffers.
        this.joinsStringBuffer = new StringBuffer();
        this.ordersStringBuffer = new StringBuffer();
        this.restrictionsStringBuffer = new StringBuffer();

        // Initialize the joins, orders, and restrictions.
        this.joins = new ArrayList<Criterion>();
        this.orders = new ArrayList<Criterion>();
        this.restrictions = new ArrayList<Criterion>();

        // Optimize the query.
        this.optimizeQuery();
    }

    /**
     * Add a restriction criterion.
     *
     * @param  criterion  the restriction criterion.
     *
     * @return  the criteria.
     */
    @Override
    public Criteria<T> add(Criterion criterion) {

        try {

            // Check if a restriction has not already been processed.
            if (this.restrictions.isEmpty()) {

                // Add the WHERE-clause to the restrictions string buffer.
                this.restrictionsStringBuffer.append(" where ");
            }
            else {

                // Add the logical operator to the restrictions string buffer.
                switch (criterion.getLogicalOperator()) {

                    case AND:
                        this.restrictionsStringBuffer.append(" and ");
                        break;
                    case OR:
                        this.restrictionsStringBuffer.append(" or ");
                        break;
                }
            }

            // Add the operand, comparison operator, and parameter if
            // necessary to the restrictions string buffer.
            switch (criterion.getComparisonOperator()) {

                case EQUAL:
                    this.restrictionsStringBuffer
                            .append(this.entityAlias)
                            .append(".")
                            .append(criterion.getOperand())
                            .append(" = ")
                            .append(":param")
                            .append(this.parameters.size() + 1);
                    break;
                case GREATER_THAN:
                    this.restrictionsStringBuffer
                            .append(this.entityAlias)
                            .append(".")
                            .append(criterion.getOperand())
                            .append(" > ")
                            .append(":param")
                            .append(this.parameters.size() + 1);
                    break;
                case GREATER_THAN_OR_EQUAL:
                    this.restrictionsStringBuffer
                            .append(this.entityAlias)
                            .append(".")
                            .append(criterion.getOperand())
                            .append(" >= ")
                            .append(":param")
                            .append(this.parameters.size() + 1);
                    break;
                case IS_EMPTY:
                    this.restrictionsStringBuffer
                            .append(this.entityAlias)
                            .append(".")
                            .append(criterion.getOperand())
                            .append(" is empty");
                    break;
                case IS_NOT_EMPTY:
                    this.restrictionsStringBuffer
                            .append(this.entityAlias)
                            .append(".")
                            .append(criterion.getOperand())
                            .append(" is not empty");
                    break;
                case IS_NULL:
                    this.restrictionsStringBuffer
                            .append(this.entityAlias)
                            .append(".")
                            .append(criterion.getOperand())
                            .append(" is null");
                    break;
                case IS_NOT_NULL:
                    this.restrictionsStringBuffer
                            .append(this.entityAlias)
                            .append(".")
                            .append(criterion.getOperand())
                            .append(" is not null");
                    break;
                case LESS_THAN:
                    this.restrictionsStringBuffer
                            .append(this.entityAlias)
                            .append(".")
                            .append(criterion.getOperand())
                            .append(" < ")
                            .append(":param")
                            .append(this.parameters.size() + 1);
                    break;
                case LESS_THAN_OR_EQUAL:
                    this.restrictionsStringBuffer
                            .append(this.entityAlias)
                            .append(".")
                            .append(criterion.getOperand())
                            .append(" <= ")
                            .append(":param")
                            .append(this.parameters.size() + 1);
                    break;
                case LIKE:
                    this.restrictionsStringBuffer
                            .append(this.entityAlias)
                            .append(".")
                            .append(criterion.getOperand())
                            .append(" like ")
                            .append(":param")
                            .append(this.parameters.size() + 1);
                    break;
                case MEMBER_OF:
                    this.restrictionsStringBuffer
                            .append(":param")
                            .append(this.parameters.size() + 1)
                            .append(" member of ")
                            .append(this.entityAlias)
                            .append(".")
                            .append(criterion.getOperand());
                    break;
                case NOT_EQUAL:
                    this.restrictionsStringBuffer
                            .append(this.entityAlias)
                            .append(".")
                            .append(criterion.getOperand())
                            .append(" <> ")
                            .append(":param")
                            .append(this.parameters.size() + 1);
                    break;
                case NOT_LIKE:
                    this.restrictionsStringBuffer
                            .append(this.entityAlias)
                            .append(".")
                            .append(criterion.getOperand())
                            .append(" not like ")
                            .append(":param")
                            .append(this.parameters.size() + 1);
                    break;
                case NOT_MEMBER_OF:
                    this.restrictionsStringBuffer
                            .append(":param")
                            .append(this.parameters.size() + 1)
                            .append(" not member of ")
                            .append(this.entityAlias)
                            .append(".")
                            .append(criterion.getOperand());
                    break;
            }

            // Check if there is a value.
            if (criterion.getValue() != null) {

                // Add the parameter.
                this.parameters.put(
                    "param" + (this.parameters.size() + 1),
                    criterion.getValue());
            }

            // Add the criterion to the restrictions.
            this.restrictions.add(criterion);
        }
        catch(Exception e) {
            // TODO: handle this.
        }

        return this;
    }

    /**
     * Add restriction criterions.
     *
     * @param  criterions  the restriction criterions.
     *
     * @return  the criteria.
     */
    @Override
    public Criteria<T> add(List<Criterion> criterions) {

        // Loop through the restriction criterions.
        for (Criterion criterion : criterions) {

            // Add the restriction criterion.
            this.add(criterion);
        }

        return this;
    }

    /**
     * Add a join criterion.
     *
     * @param  criterion  the join criterion.
     *
     * @return  the criteria.
     */
    @Override
    public Criteria<T> addJoin(Criterion criterion) {

        try {

            // Declare.
            EntityType<T> entityType;

            // Add the join operator to the joins string buffer.
            switch (criterion.getJoinOperator()) {

                case JOIN:
                    joinsStringBuffer
                            .append(" join ")
                            .append(this.entityAlias)
                            .append(".")
                            .append(criterion.getOperand());
                    break;
                case JOIN_FETCH:
                    joinsStringBuffer
                            .append(" join fetch ")
                            .append(this.entityAlias)
                            .append(".")
                            .append(criterion.getOperand());
                    break;
                case LEFT_JOIN:
                    joinsStringBuffer
                            .append(" left join ")
                            .append(this.entityAlias)
                            .append(".")
                            .append(criterion.getOperand());
                    break;
                case LEFT_JOIN_FETCH:
                    joinsStringBuffer
                            .append(" left join fetch ")
                            .append(this.entityAlias)
                            .append(".")
                            .append(criterion.getOperand());
                    break;
            }

            // Add the criterion to the joins.
            this.joins.add(criterion);
/*
            // Get the metamodel entity type.
            entityType = this.entityManager.getMetamodel().entity(this.entityClass);

            // Loop through the attributes.
            for (Attribute<? super T, ?> attribute : entityType.getAttributes()) {

                // Check if the attribute is the operand and it can be optimized.
                if (attribute.getName().equals(criterion.getOperand()) &&
                    optimize(attribute)) {

                    // Optimize the join.
                    this.optimizeJoin(attribute.getJavaType(), getPathExpression(this.entityAlias, attribute.getName()));
                }
            }
 */
        }
        catch(Exception e) {
            // TODO: handle this.
        }

        return this;
    }

    /**
     * Add join criterions.
     *
     * @param  criterions  the join criterions.
     *
     * @return  the criteria.
     */
    @Override
    public Criteria<T> addJoins(List<Criterion> criterions) {

        // Loop through the join criterions.
        for (Criterion criterion : criterions) {

            // Add the join criterion.
            this.addJoin(criterion);
        }

        return this;
    }

    /**
     * Add a order criterion.
     *
     * @param  criterion  the order criterion.
     *
     * @return  the criteria.
     */
    @Override
    public Criteria<T> addOrder(Criterion criterion) {

        try {

            // Check if an order has not already been processed.
            if (this.orders.isEmpty()) {

                // Add the ORDER BY-clause to the orders string buffer.
                this.ordersStringBuffer.append(" order by ");
            }
            else {

                // Add a comma to the orders string buffer.
                this.ordersStringBuffer.append(", ");
            }

            // Add the operand to the orders string buffer.
            this.ordersStringBuffer
                    .append(this.entityAlias)
                    .append(".")
                    .append(criterion.getOperand());

            // Add the order direction to the orders string buffer.
            switch (criterion.getOrderDirection()) {

                case ASC:
                    this.ordersStringBuffer.append(" asc");
                    break;
                case DESC:
                    this.ordersStringBuffer.append(" desc");
                    break;
            }

            // Add the criterion to the orders.
            this.orders.add(criterion);
        }
        catch(Exception e) {
            // TODO: handle this.
        }

        return this;
    }

    /**
     * Add order criterions.
     *
     * @param  criterions  the order criterions.
     *
     * @return  the criteria.
     */
    @Override
    public Criteria<T> addOrders(List<Criterion> criterions) {

        // Loop through the order criterions.
        for (Criterion criterion : criterions) {

            // Add the order criterion.
            this.addOrder(criterion);
        }

        return this;
    }

    /**
     * Get the new path expression created by appending the path to the
     * path expression.
     *
     * @param  pathExpression  the path expression.
     * @param  path            the path.
     *
     * @return  the new path expression.
     */
    private static String getPathExpression(String pathExpression, String path) {
        return new StringBuffer()
                .append(pathExpression)
                .append(".")
                .append(path)
                .toString();
    }

    /**
     * Get the hints.
     *
     * @return  the hints.
     */
    @Override
    public Map<Object, String> getHints() {
        return this.hints;
    }

    /**
     * Get the parameters.
     * 
     * @return  the parameters.
     */
    @Override
    public Map<String, Object> getParameters() {
        return this.parameters;
    }

    /**
     * Get the query language string.
     * 
     * @return  the query language string.
     */
    @Override
    public String getQlString() {
        return new StringBuffer()
                .append("select ")
                .append((!this.joins.isEmpty()) ? "distinct " : "")
                .append(this.entityAlias)
                .append(" from ")
                .append(this.entityClass.getSimpleName())
                .append(" ")
                .append(this.entityAlias)
                .append(this.joinsStringBuffer)
                .append(this.restrictionsStringBuffer)
                .append(this.ordersStringBuffer)
                .toString();
    }

    /**
     * Check if an join criterion exists.
     *
     * @return  true if an join criterion exists, otherwise false.
     */
    @Override
    public boolean joinExists() {
        return !this.joins.isEmpty();
    }

    /**
     * Check if the attribute can be optimized.
     * 
     * @param  attribute  the attribute.
     * 
     * @return  true if the attribute can be optimized, otherwise false.
     */
    private static boolean optimize(Attribute attribute) {
        return (attribute.getPersistentAttributeType() == Attribute.PersistentAttributeType.MANY_TO_MANY ||
                attribute.getPersistentAttributeType() == Attribute.PersistentAttributeType.MANY_TO_ONE ||
                attribute.getPersistentAttributeType() == Attribute.PersistentAttributeType.ONE_TO_MANY ||
                attribute.getPersistentAttributeType() == Attribute.PersistentAttributeType.ONE_TO_ONE);
    }

    /**
     * Optimize the join.
     *
     * @param  entityClass     the entity class.
     * @param  pathExpression  the path expression.
     */
    private <U> void optimizeJoin(Class<U> entityClass, String pathExpression) {

        // Declare.
        EntityType<U> entityType;

        // Get the metamodel entity type.
        entityType = this.entityManager.getMetamodel().entity(entityClass);

        // Loop through the attributes.
        for (Attribute<? super U, ?> attribute : entityType.getAttributes()) {

            // Check if the attribute can be optimized.
            if (//attribute.getPersistentAttributeType() == Attribute.PersistentAttributeType.MANY_TO_MANY ||
                attribute.getPersistentAttributeType() == Attribute.PersistentAttributeType.MANY_TO_ONE ||
                //attribute.getPersistentAttributeType() == Attribute.PersistentAttributeType.ONE_TO_MANY ||
                attribute.getPersistentAttributeType() == Attribute.PersistentAttributeType.ONE_TO_ONE) {

                // Declare.
                String newPathExpression;

                // Set the new path expression.
                newPathExpression = getPathExpression(pathExpression, attribute.getName());

                // Add a hint.
                // TODO: Try QueryHints.BATCH or combination of QueryHints.LEFT_FETCH
                // and QueryHints.BATCH when eclipse 2.1.2 is released.
                this.hints.put(newPathExpression, QueryHints.LEFT_FETCH);

                // Optimize the join.
                this.optimizeJoin(attribute.getJavaType(), newPathExpression);
            }
        }
    }

    /**
     * Optimize the query.
     */
    private void optimizeQuery() {

        // Declare.
        EntityType<T> entityType;

        // Get the metamodel entity type.
        entityType = this.entityManager.getMetamodel().entity(this.entityClass);

        // Loop through the attributes.
        for (Attribute<? super T, ?> attribute : entityType.getAttributes()) {

            // Check if the attribute can be optimized.
            if (optimize(attribute)) {

                // Add the join operation.
                this.addJoin(JoinOperation.leftJoinFetch(attribute.getName()));
            }
        }
    }

    /**
     * Check if an order criterion exists.
     *
     * @return  true if an order criterion exists, otherwise false.
     */
    @Override
    public boolean orderExists() {
        return !this.orders.isEmpty();
    }

    /**
     * Check if a restriction criterion exists.
     *
     * @return  true if a restriction criterion exists, otherwise false.
     */
    @Override
    public boolean restrictionExists() {
        return !this.restrictions.isEmpty();
    }
}