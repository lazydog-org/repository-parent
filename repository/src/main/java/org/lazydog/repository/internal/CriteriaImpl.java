package org.lazydog.repository.internal;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import javax.persistence.EntityManager;
import javax.persistence.metamodel.Attribute;
import javax.persistence.metamodel.EntityType;
import org.lazydog.repository.Criteria;
import org.lazydog.repository.criterion.Criterion;


/**
 * Criteria implemented using the Java Persistence API.
 * 
 * @author  Ron Rickard
 */
public class CriteriaImpl<T> implements Criteria<T>, Serializable {

    private static final long serialVersionUID = 1L;
    
    private String entityAlias;
    private List<Criterion> orders;
    private StringBuffer ordersStringBuffer;
    private StringBuffer qlStringBuffer;
    private Map<String, Object> parameters;
    private List<Criterion> restrictions;

    /**
     * Constructor.
     *
     * @param  entityClass    the entity class.
     * @param  entityManager  the entity manager.
     *
     * @throws  IllegalArgumentException  if the entity class or entity manager are invalid.
     */
    public CriteriaImpl(Class<T> entityClass, EntityManager entityManager) {

        // Declare.
        String entityName;
        List<String> leftJoinFetchClauses;

        // Check if the entity class is null.
        if (entityClass == null) {
            throw new IllegalArgumentException("The entity class is invalid.");
        }

        // Check if the entity manager is null.
        if (entityManager == null) {
            throw new IllegalArgumentException("The entity manager is invalid.");
        }

        // Get the entity name.
        entityName = entityClass.getSimpleName();

        // Get the entity alias.
        this.entityAlias = entityName.toLowerCase();

        // Get the LEFT JOIN FETCH-clauses.
        leftJoinFetchClauses = getLeftJoinFetchClauses(entityClass, entityManager, this.entityAlias);

        // Initialize the query language string.
        this.qlStringBuffer = new StringBuffer();
        this.qlStringBuffer.append("select ")
                           .append((leftJoinFetchClauses.size() > 0) ? "distinct " : "")
                           .append(this.entityAlias)
                           .append(" from ")
                           .append(entityName)
                           .append(" ")
                           .append(this.entityAlias);

        // Loop through the LEFT JOIN FETCH-clauses.
        for (String leftJoinFetchClause : leftJoinFetchClauses) {

            // Add the LEFT JOIN FETCH-clause to the query language string.
            this.qlStringBuffer.append(leftJoinFetchClause);
        }

        // Initialize the orders string.
        this.ordersStringBuffer = new StringBuffer();
        
        // Initialize the parameters.
        this.parameters = new LinkedHashMap<String, Object>();

        // Initialize the orders and restrictions.
        this.orders = new ArrayList<Criterion>();
        this.restrictions = new ArrayList<Criterion>();
    }

    /**
     * Get the LEFT JOIN FETCH-clauses.
     *
     * @param  entityClass    the entity class.
     * @param  entityManager  the entity manager.
     * @param  entityAlias    the entity alias.
     *
     * @return  the LEFT JOIN FETCH-clauses.
     */
    private static <T> List<String> getLeftJoinFetchClauses(
            Class<T> entityClass, EntityManager entityManager, String entityAlias) {

        // Declare.
        EntityType<T> entityType;
        List<String> leftJoinFetchClauses;

        // Initialize.
        leftJoinFetchClauses = new ArrayList<String>();

        // Get the metamodel entity type.
        entityType = entityManager.getMetamodel().entity(entityClass);

        // Loop through the attributes.
        for (Attribute<? super T, ?> attribute : entityType.getAttributes()) {

            // Check if the persistent attribute type is many-to-many, 
            // many-to-one, one-to-many, or one-to-one.
            if (attribute.getPersistentAttributeType() == Attribute.PersistentAttributeType.MANY_TO_MANY ||
                attribute.getPersistentAttributeType() == Attribute.PersistentAttributeType.MANY_TO_ONE ||
                attribute.getPersistentAttributeType() == Attribute.PersistentAttributeType.ONE_TO_MANY ||
                attribute.getPersistentAttributeType() == Attribute.PersistentAttributeType.ONE_TO_ONE) {

                // Declare.
                StringBuffer leftJoinFetchClause;

                // Initialize.
                leftJoinFetchClause = new StringBuffer();

                // Create the LEFT JOIN FETCH-clause.
                leftJoinFetchClause.append(" left join fetch ")
                                   .append(entityAlias)
                                   .append(".")
                                   .append(attribute.getName());

                // Add the LEFT JOIN FETCH-clause to the list.
                leftJoinFetchClauses.add(leftJoinFetchClause.toString());

            }
        }

        return leftJoinFetchClauses;
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

        // Process the restriction criterion.
        if (!this.processRestriction(criterion)) {
            // Ignore.
        }

        // Add the criterion to the restrictions.
        this.restrictions.add(criterion);

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
     * Add a order criterion.
     *
     * @param  criterion  the order criterion.
     *
     * @return  the criteria.
     */
    @Override
    public Criteria<T> addOrder(Criterion criterion) {

        // Process the order criterion.
        if (!this.processOrder(criterion)) {
            // Ignore.
        }

        // Add the criterion to the orders.
        this.orders.add(criterion);

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
        return this.qlStringBuffer.toString()
             + this.ordersStringBuffer.toString();
    }

    /**
     * Process a order criterion.
     *
     * @param  criterion  the order criterion.
     *
     * @return  true if the order criterion is processed, otherwise false.
     */
    private boolean processOrder(Criterion criterion) {

        // Declare.
        boolean processed;

        processed = true;

        try {

            // Check if an order has not already been processed.
            if (this.orders.isEmpty()) {

                // Add the ORDER BY-clause to the query language string.
                this.ordersStringBuffer.append(" order by ");
            }
            else {

                // Add a comma to the query language string.
                this.ordersStringBuffer.append(", ");
            }

            // Add the operand to the query language string.
            this.ordersStringBuffer.append(this.entityAlias)
                                   .append(".")
                                   .append(criterion.getOperand());

            // Add the logical operator to the query language string.
            switch (criterion.getOrderDirection()) {

                case ASC:
                    this.ordersStringBuffer.append(" asc");
                    break;
                case DESC:
                    this.ordersStringBuffer.append(" desc");
                    break;
            }
        }
        catch(Exception e) {
            processed = false;
        }

        return processed;
    }

    /**
     * Process a restriction criterion.
     *
     * @param  criterion  the restriction criterion.
     *
     * @return  true if the restriction criterion is processed, otherwise false.
     */
    private boolean processRestriction(Criterion criterion) {

        // Declare.
        boolean processed;

        processed = true;

        try {

            // Check if a restriction has not already been processed.
            if (this.restrictions.isEmpty()) {

                // Add the WHERE-clause to the query language string.
                this.qlStringBuffer.append(" where ");
            }
            else {

                // Add the logical operator to the query language string.
                switch (criterion.getLogicalOperator()) {

                    case AND:
                        this.qlStringBuffer.append(" and ");
                        break;
                    case OR:
                        this.qlStringBuffer.append(" or ");
                        break;
                }
            }

            // Add the operand, comparison operator, and parameter if 
            // necessary to the query language string.
            switch (criterion.getComparisonOperator()) {

                case EQUAL:
                    this.qlStringBuffer.append(this.entityAlias)
                                       .append(".")
                                       .append(criterion.getOperand())
                                       .append(" = ")
                                       .append(":param" +
                                               (this.parameters.size() + 1));
                    break;
                case GREATER_THAN:
                    this.qlStringBuffer.append(this.entityAlias)
                                       .append(".")
                                       .append(criterion.getOperand())
                                       .append(" > ")
                                       .append(":param" +
                                               (this.parameters.size() + 1));
                    break;
                case GREATER_THAN_OR_EQUAL:
                    this.qlStringBuffer.append(this.entityAlias)
                                       .append(".")
                                       .append(criterion.getOperand())
                                       .append(" >= ")
                                       .append(":param" +
                                               (this.parameters.size() + 1));
                    break;
                case IS_EMPTY:
                    this.qlStringBuffer.append(this.entityAlias)
                                       .append(".")
                                       .append(criterion.getOperand())
                                       .append(" is empty");
                    break;
                case IS_NOT_EMPTY:
                    this.qlStringBuffer.append(this.entityAlias)
                                       .append(".")
                                       .append(criterion.getOperand())
                                       .append(" is not empty");
                    break;
                case IS_NULL:
                    this.qlStringBuffer.append(this.entityAlias)
                                       .append(".")
                                       .append(criterion.getOperand())
                                       .append(" is null");
                    break;
                case IS_NOT_NULL:
                    this.qlStringBuffer.append(this.entityAlias)
                                       .append(".")
                                       .append(criterion.getOperand())
                                       .append(" is not null");
                    break;
                case LESS_THAN:
                    this.qlStringBuffer.append(this.entityAlias)
                                       .append(".")
                                       .append(criterion.getOperand())
                                       .append(" < ")
                                       .append(":param" +
                                               (this.parameters.size() + 1));
                    break;
                case LESS_THAN_OR_EQUAL:
                    this.qlStringBuffer.append(this.entityAlias)
                                       .append(".")
                                       .append(criterion.getOperand())
                                       .append(" <= ")
                                       .append(":param" +
                                               (this.parameters.size() + 1));
                    break;
                case LIKE:
                    this.qlStringBuffer.append(this.entityAlias)
                                       .append(".")
                                       .append(criterion.getOperand())
                                       .append(" like ")
                                       .append(":param" +
                                               (this.parameters.size() + 1));
                    break;
                case MEMBER_OF:
                    this.qlStringBuffer.append(":param" +
                                               (this.parameters.size() + 1))
                                       .append(" member of ")
                                       .append(this.entityAlias)
                                       .append(".")
                                       .append(criterion.getOperand());
                    break;
                case NOT_EQUAL:
                    this.qlStringBuffer.append(this.entityAlias)
                                       .append(".")
                                       .append(criterion.getOperand())
                                       .append(" <> ")
                                       .append(":param" +
                                               (this.parameters.size() + 1));
                    break;
                case NOT_LIKE:
                    this.qlStringBuffer.append(this.entityAlias)
                                       .append(".")
                                       .append(criterion.getOperand())
                                       .append(" not like ")
                                       .append(":param" +
                                               (this.parameters.size() + 1));
                    break;
                case NOT_MEMBER_OF:
                    this.qlStringBuffer.append(":param" +
                                               (this.parameters.size() + 1))
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
        }
        catch(Exception e) {
            processed = false;
        }

        return processed;
    }
}