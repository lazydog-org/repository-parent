package org.lazydog.repository.jpa.internal;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import javax.persistence.EntityManager;
import javax.persistence.metamodel.Attribute;
import javax.persistence.metamodel.EntityType;
import javax.persistence.metamodel.PluralAttribute;
import javax.persistence.metamodel.SingularAttribute;
import org.eclipse.persistence.annotations.BatchFetchType;
import org.eclipse.persistence.config.QueryHints;
import org.lazydog.repository.Criteria;
import org.lazydog.repository.criterion.Criterion;
import org.lazydog.repository.criterion.Enclosure;


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
    private List<Criterion> orders;
    private StringBuilder ordersStringBuilder;
    private Map<String, Object> parameters;
    private List<Criterion> restrictions;
    private StringBuilder restrictionsStringBuilder;

    /**
     * Constructor.
     *
     * @param  entityClass    the entity class.
     * @param  entityManager  the entity manager.
     *
     * @throws  IllegalArgumentException  if the entity class or entity manager are invalid.
     */
    public CriteriaImpl(final Class<T> entityClass, final EntityManager entityManager) {

        // Check if the entity class is null.
        if (entityClass == null) {
            throw new IllegalArgumentException("The entity class is invalid.");
        }

        // Check if the entity manager is null.
        if (entityManager == null) {
            throw new IllegalArgumentException("The entity manager is invalid.");
        }

        // Check if the entity class and entity manager are valid.
        entityManager.getMetamodel().entity(entityClass);
        
        // Set the entity class and entity manager.
        this.entityClass = entityClass;
        this.entityManager = entityManager;

        // Set the entity alias.
        this.entityAlias = this.entityClass.getSimpleName().toLowerCase();

        // Initialize the hints.
        this.hints = new LinkedHashMap<Object, String>();
        
        // Initialize the parameters.
        this.parameters = new LinkedHashMap<String, Object>();

        // Initialize the orders and restrictions string builders.
        this.ordersStringBuilder = new StringBuilder();
        this.restrictionsStringBuilder = new StringBuilder();

        // Initialize the orders and restrictions.
        this.orders = new ArrayList<Criterion>();
        this.restrictions = new ArrayList<Criterion>();

        // Optimize the query.
        //this.optimizeQuery(this.entityClass, this.entityAlias, new HashSet<Class<?>>());
    }

    /**
     * Add a restriction criterion.
     *
     * @param  criterion  the restriction criterion.
     *
     * @return  the criteria.
     */
    @Override
    public Criteria<T> add(final Criterion criterion) {

        try {

            // Check if a restriction has not already been processed.
            if (this.restrictions.isEmpty()) {

                // Add the WHERE-clause to the restrictions string builder.
                this.restrictionsStringBuilder.append(" where ");
            }
            else {

                // Add the logical operator to the restrictions string builder.
                switch (criterion.getLogicalOperator()) {

                    case AND:
                        this.restrictionsStringBuilder.append(" and ");
                        break;
                    case OR:
                        this.restrictionsStringBuilder.append(" or ");
                        break;
                }
            }

            // Check if the begin enclosure operator needs to be added.
            if (criterion.getEnclosureOperator() == Enclosure.Operator.BEGIN) {
                this.restrictionsStringBuilder.append("(");
            }
 
            // Add the operand, comparison operator, and parameter if
            // necessary to the restrictions string builder.
            switch (criterion.getComparisonOperator()) {

                case EQUAL:
                    this.restrictionsStringBuilder
                            .append(this.qualifyOperand(criterion.getOperand()))
                            .append(" = ")
                            .append(this.boundNextParameterName());
                    break;
                case GREATER_THAN:
                    this.restrictionsStringBuilder
                            .append(this.qualifyOperand(criterion.getOperand()))
                            .append(" > ")
                            .append(this.boundNextParameterName());
                    break;
                case GREATER_THAN_OR_EQUAL:
                    this.restrictionsStringBuilder
                            .append(this.qualifyOperand(criterion.getOperand()))
                            .append(" >= ")
                            .append(this.boundNextParameterName());
                    break;
                case IS_EMPTY:
                    this.restrictionsStringBuilder
                            .append(this.qualifyOperand(criterion.getOperand()))
                            .append(" is empty");
                    break;
                case IS_NOT_EMPTY:
                    this.restrictionsStringBuilder
                            .append(this.qualifyOperand(criterion.getOperand()))
                            .append(" is not empty");
                    break;
                case IS_NULL:
                    this.restrictionsStringBuilder
                            .append(this.qualifyOperand(criterion.getOperand()))
                            .append(" is null");
                    break;
                case IS_NOT_NULL:
                    this.restrictionsStringBuilder
                            .append(this.qualifyOperand(criterion.getOperand()))
                            .append(" is not null");
                    break;
                case LESS_THAN:
                    this.restrictionsStringBuilder
                            .append(this.qualifyOperand(criterion.getOperand()))
                            .append(" < ")
                            .append(this.boundNextParameterName());
                    break;
                case LESS_THAN_OR_EQUAL:
                    this.restrictionsStringBuilder
                            .append(this.qualifyOperand(criterion.getOperand()))
                            .append(" <= ")
                            .append(this.boundNextParameterName());
                    break;
                case LIKE:
                    this.restrictionsStringBuilder
                            .append(this.qualifyOperand(criterion.getOperand()))
                            .append(" like ")
                            .append(this.boundNextParameterName());
                    break;
                case MEMBER_OF:
                    this.restrictionsStringBuilder
                            .append(this.boundNextParameterName())
                            .append(" member of ")
                            .append(this.qualifyOperand(criterion.getOperand()));
                    break;
                case NOT_EQUAL:
                    this.restrictionsStringBuilder
                            .append(this.qualifyOperand(criterion.getOperand()))
                            .append(" <> ")
                            .append(this.boundNextParameterName());
                    break;
                case NOT_LIKE:
                    this.restrictionsStringBuilder
                            .append(this.qualifyOperand(criterion.getOperand()))
                            .append(" not like ")
                            .append(this.boundNextParameterName());
                    break;
                case NOT_MEMBER_OF:
                    this.restrictionsStringBuilder
                            .append(this.boundNextParameterName())
                            .append(" not member of ")
                            .append(this.qualifyOperand(criterion.getOperand()));
                    break;
            }

            // Check if the end enclosure operator needs to be added.
            if (criterion.getEnclosureOperator() == Enclosure.Operator.END) {
                this.restrictionsStringBuilder.append(")");
            }
 
            // Check if there is a value.
            if (criterion.getValue() != null) {

                // Add the parameter.
                this.parameters.put(
                    this.nextParameterName(),
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
    public Criteria<T> add(final List<Criterion> criterions) {

        // Loop through the restriction criterions.
        for (Criterion criterion : criterions) {

            // Add the restriction criterion.
            this.add(criterion);
        }

        return this;
    }

    /**
     * Add a batch fetch (exists) hint.
     * 
     * @param  pathExpression  the path expression.
     */
    private void addBatchFetchExistsHint(String pathExpression) {
        
        // Add a batch fetch exists hint.
        this.hints.put(BatchFetchType.EXISTS, QueryHints.BATCH_TYPE);
        this.hints.put(pathExpression, QueryHints.BATCH);
    }
    
    /**
     * Add a left join fetch hint.
     * 
     * @param  pathExpression  the path expression.
     */
    private void addLeftJoinFetchHint(String pathExpression) {

        // Add a left join fetch hint.
        this.hints.put(pathExpression, QueryHints.LEFT_FETCH);
    }

    /**
     * Add a order criterion.
     *
     * @param  criterion  the order criterion.
     *
     * @return  the criteria.
     */
    @Override
    public Criteria<T> addOrder(final Criterion criterion) {

        try {

            // Check if an order has not already been processed.
            if (this.orders.isEmpty()) {

                // Add the ORDER BY-clause to the orders string builder.
                this.ordersStringBuilder.append(" order by ");
            }
            else {

                // Add a comma to the orders string builder.
                this.ordersStringBuilder.append(", ");
            }

            // Add the operand to the orders string builder.
            this.ordersStringBuilder
                    .append(this.qualifyOperand(criterion.getOperand()));

            // Add the order direction to the orders string builder.
            switch (criterion.getOrderDirection()) {

                case ASC:
                    this.ordersStringBuilder.append(" asc");
                    break;
                case DESC:
                    this.ordersStringBuilder.append(" desc");
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
    public Criteria<T> addOrders(final List<Criterion> criterions) {

        // Loop through the order criterions.
        for (Criterion criterion : criterions) {

            // Add the order criterion.
            this.addOrder(criterion);
        }

        return this;
    }

    /**
     * Check if the attribute has a collection-valued association.
     * 
     * @param  attribute  the attribute.
     * 
     * @return  true if the attribute has a collection-valued association, otherwise false.
     */
    private static boolean collectionValuedAssociation(final Attribute attribute) {
        return (attribute.getPersistentAttributeType() == Attribute.PersistentAttributeType.MANY_TO_MANY ||
                attribute.getPersistentAttributeType() == Attribute.PersistentAttributeType.ONE_TO_MANY);
    }

    /**
     * Get the hints.
     *
     * @return  the hints.
     */
    public Map<Object, String> getHints() {
        return this.hints;
    }

    /**
     * Get the parameters.
     * 
     * @return  the parameters.
     */
    public Map<String, Object> getParameters() {
        return this.parameters;
    }

    /**
     * Get the new path expression created by appending the path to the path expression.
     *
     * @param  pathExpression  the path expression.
     * @param  path            the path.
     *
     * @return  the new path expression.
     */
    private static String getPathExpression(final String pathExpression, final String path) {
        return new StringBuilder().append(pathExpression).append(".").append(path).toString();
    }

    /**
     * Get the query language string.
     * 
     * @return  the query language string.
     */
    public String getQlString() {
        return new StringBuilder()
                .append("select ")
                .append(this.entityAlias)
                .append(" from ")
                .append(this.entityClass.getSimpleName())
                .append(" ")
                .append(this.entityAlias)
                .append(this.restrictionsStringBuilder)
                .append(this.ordersStringBuilder)
                .toString();
    }

    /**
     * Get the bound next parameter name.
     * 
     * @return  the bound next parameter name.
     */
    private String boundNextParameterName() {
        return new StringBuilder().append(":").append(this.nextParameterName()).toString();
    }
    
    /**
     * Get the next parameter name.
     * 
     * @return  the next parameter name.
     */
    private String nextParameterName() {
        return new StringBuilder().append("param").append(this.parameters.size() + 1).toString();
    }
    
    /**
     * Optimize the query.
     * 
     * @param  entityClass           the entity class.
     * @param  pathExpression        the path expression.
     * @param  visitedEntityClasses  the visited entity classes.
     */
    private <U> void optimizeQuery(final Class<U> entityClass, final String pathExpression, final Set<Class<?>> visitedEntityClasses) {

        // Check if the entity class is not a visited entity classes.
        if (!visitedEntityClasses.contains(entityClass)) {

            // Add the entity class to the visited entity classes.
            visitedEntityClasses.add(entityClass);
            
            // Get the metamodel entity type.
            EntityType<U> entityType = this.entityManager.getMetamodel().entity(entityClass);

            // Loop through the attributes with multiple values.
            for (PluralAttribute<? super U, ?, ?> attribute : entityType.getPluralAttributes()) {

                // Check if the attribute can be optimized.
                if (collectionValuedAssociation(attribute)) {

                    // Get the new path expression for the attribute.
                    String newPathExpression = getPathExpression(pathExpression, attribute.getName());

                    // Add the batch fetch (exists) hint.
                    this.addBatchFetchExistsHint(newPathExpression);    

                    // Optimize the query.
                    this.optimizeQuery(attribute.getBindableJavaType(), newPathExpression, visitedEntityClasses);
                }
            }

            // Loop through the attributes with single values.
            for (SingularAttribute<? super U, ?> attribute : entityType.getSingularAttributes()) {

                // Check if the attribute can be optimized.
                if (singleValuedAssociation(attribute)) {

                    // Get the new path expression for the attribute.
                    String newPathExpression = getPathExpression(pathExpression, attribute.getName());

                    // Add the left join fetch hint.
                    this.addLeftJoinFetchHint(newPathExpression);

                    // Optimize the query.
                    this.optimizeQuery(attribute.getBindableJavaType(), newPathExpression, visitedEntityClasses);
                }
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
     * Qualify the operand.
     * 
     * @param  operand  the operand.
     * 
     * @return  the operand qualified by the entity alias.
     */
    private String qualifyOperand(String operand) {
        return qualifyOperand(operand, this.entityAlias);
    }
    
    /**
     * Qualify the operand.
     * 
     * @param  operand    the operand.
     * @param  qualifier  the qualifier.
     * 
     * @return  the operand qualified by the qualifier.
     */
    private static String qualifyOperand(String operand, String qualifier) {
        return new StringBuilder().append(qualifier).append(".").append(operand).toString();
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
    
    /**
     * Check if the attribute has a single-valued association.
     * 
     * @param  attribute  the attribute.
     * 
     * @return  true if the attribute has a single-valued association, otherwise false.
     */
    private static boolean singleValuedAssociation(final Attribute attribute) {
        return (attribute.getPersistentAttributeType() == Attribute.PersistentAttributeType.MANY_TO_ONE ||
                attribute.getPersistentAttributeType() == Attribute.PersistentAttributeType.ONE_TO_ONE);
    }
}