package org.lazydog.repository.jpa.internal;

import java.io.Serializable;
import java.lang.reflect.Field;
import java.lang.reflect.ParameterizedType;
import java.util.ArrayList;
import java.util.HashSet;
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
import org.lazydog.repository.criterion.Join;


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
    private List<Criterion> joins;
    private StringBuilder joinsStringBuilder;
    private List<Criterion> orders;
    private StringBuilder ordersStringBuilder;
    private Map<Object, String> queryHints;
    private Map<String, Object> queryParameters;
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
        // TODO: remove reliance on entity manager by parsing XML ourselves.
        this.entityClass = entityClass;
        this.entityManager = entityManager;

        // Set the entity alias.
        this.entityAlias = this.entityClass.getSimpleName().toLowerCase();

        // Initialize the query hints.
        this.queryHints = new LinkedHashMap<Object, String>();
        
        // Initialize the query parameters.
        this.queryParameters = new LinkedHashMap<String, Object>();

        // Initialize the joins, orders, and restrictions string builders.
        this.joinsStringBuilder = new StringBuilder();
        this.ordersStringBuilder = new StringBuilder();
        this.restrictionsStringBuilder = new StringBuilder();

        // Initialize the orders and restrictions.
        this.joins = new ArrayList<Criterion>();
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
                this.restrictionsStringBuilder.append(" WHERE ");
            }
            else {

                // Add the logical operator to the restrictions string builder.
                switch (criterion.getLogicalOperator()) {

                    case AND:
                        this.restrictionsStringBuilder.append(" AND ");
                        break;
                    case OR:
                        this.restrictionsStringBuilder.append(" OR ");
                        break;
                }
            }

            // Check if the begin enclosure operator needs to be added.
            if (criterion.getEnclosureOperator() == Enclosure.Operator.BEGIN) {
                this.restrictionsStringBuilder.append("(");
            }
 
            // Get the qualified operand.
            String qualifiedOperand = this.qualifyOperand(criterion.getOperand());
            
            // Get the join criterions.
            List<Criterion> joinCriterions = getJoinCriterions(this.entityClass, qualifiedOperand);
            
            // Check if there are join criterions.
            if (!joinCriterions.isEmpty()) {
                
                // Add the joins.
                this.addJoins(joinCriterions);
                
                // Determine the qualified operand.
                String lastJoinOperand = joinCriterions.get(joinCriterions.size() - 1).getOperand();
                String qualifier = lastJoinOperand.substring(lastJoinOperand.indexOf(".") + 1);
                qualifiedOperand = qualifiedOperand.substring(qualifiedOperand.indexOf(qualifier));
            }
            
            // Add the comparison operator to the restrictions string builder.
            switch (criterion.getComparisonOperator()) {

                case EQUAL:
                    this.restrictionsStringBuilder
                            .append(qualifiedOperand)
                            .append(" = ")
                            .append(this.boundNextQueryParameterName());
                    break;
                case GREATER_THAN:
                    this.restrictionsStringBuilder
                            .append(qualifiedOperand)
                            .append(" > ")
                            .append(this.boundNextQueryParameterName());
                    break;
                case GREATER_THAN_OR_EQUAL:
                    this.restrictionsStringBuilder
                            .append(qualifiedOperand)
                            .append(" >= ")
                            .append(this.boundNextQueryParameterName());
                    break;
                case IS_EMPTY:
                    this.restrictionsStringBuilder
                            .append(qualifiedOperand)
                            .append(" IS EMPTY");
                    break;
                case IS_NOT_EMPTY:
                    this.restrictionsStringBuilder
                            .append(qualifiedOperand)
                            .append(" IS NOT EMPTY");
                    break;
                case IS_NULL:
                    this.restrictionsStringBuilder
                            .append(qualifiedOperand)
                            .append(" IS NULL");
                    break;
                case IS_NOT_NULL:
                    this.restrictionsStringBuilder
                            .append(qualifiedOperand)
                            .append(" IS NOT NULL");
                    break;
                case LESS_THAN:
                    this.restrictionsStringBuilder
                            .append(qualifiedOperand)
                            .append(" < ")
                            .append(this.boundNextQueryParameterName());
                    break;
                case LESS_THAN_OR_EQUAL:
                    this.restrictionsStringBuilder
                            .append(qualifiedOperand)
                            .append(" <= ")
                            .append(this.boundNextQueryParameterName());
                    break;
                case LIKE:
                    this.restrictionsStringBuilder
                            .append(qualifiedOperand)
                            .append(" LIKE ")
                            .append(this.boundNextQueryParameterName());
                    break;
                case MEMBER_OF:
                    this.restrictionsStringBuilder
                            .append(this.boundNextQueryParameterName())
                            .append(" MEMBER OF ")
                            .append(qualifiedOperand);
                    break;
                case NOT_EQUAL:
                    this.restrictionsStringBuilder
                            .append(qualifiedOperand)
                            .append(" <> ")
                            .append(this.boundNextQueryParameterName());
                    break;
                case NOT_LIKE:
                    this.restrictionsStringBuilder
                            .append(qualifiedOperand)
                            .append(" NOT LIKE ")
                            .append(this.boundNextQueryParameterName());
                    break;
                case NOT_MEMBER_OF:
                    this.restrictionsStringBuilder
                            .append(this.boundNextQueryParameterName())
                            .append(" NOT MEMBER OF ")
                            .append(qualifiedOperand);
                    break;
            }

            // Check if the end enclosure operator needs to be added.
            if (criterion.getEnclosureOperator() == Enclosure.Operator.END) {
                this.restrictionsStringBuilder.append(")");
            }
 
            // Check if there is a value.
            if (criterion.getValue() != null) {

                // Add the query parameter.
                this.queryParameters.put(
                    this.nextQueryParameterName(),
                    criterion.getValue());
            }

            // Add the criterion to the restrictions.
            this.restrictions.add(criterion);
        }
        catch (Exception e) {
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
    private void addBatchFetchExistsHint(final String pathExpression) {
        
        // Add a batch fetch exists hint.
        this.queryHints.put(BatchFetchType.EXISTS, QueryHints.BATCH_TYPE);
        this.queryHints.put(pathExpression, QueryHints.BATCH);
    }
    
    /**
     * Add a batch fetch (join) hint.
     * 
     * @param  pathExpression  the path expression.
     */
    private void addBatchFetchJoinHint(final String pathExpression) {
        
        // Add a batch fetch join hint.
        this.queryHints.put(BatchFetchType.JOIN, QueryHints.BATCH_TYPE);
        this.queryHints.put(pathExpression, QueryHints.BATCH);
    }
    
    /**
     * Add a join criterion.
     * 
     * @param  criterion  the join criterion.
     * 
     * @return  the criteria.
     */
    public Criteria<T> addJoin(final Criterion criterion) {
        
        try {

            // Add the join operator to the joins string builder.
            switch (criterion.getJoinOperator()) {
                
                case JOIN:
                    joinsStringBuilder.append(" JOIN ");
                    break;
                case JOIN_FETCH:
                    joinsStringBuilder.append(" JOIN FETCH ");
                    break;
                case LEFT_JOIN:
                    joinsStringBuilder.append(" LEFT JOIN ");
                    break;
                case LEFT_JOIN_FETCH:
                    joinsStringBuilder.append(" LEFT JOIN FETCH ");
                    break;
            }

            // Add the operand and alias to the orders string builder.
            this.joinsStringBuilder
                    .append(criterion.getOperand())
                    .append(" ")
                    .append(criterion.getOperand().substring(criterion.getOperand().indexOf(".") + 1));
            
            // Add the criterion to the joins.
            this.joins.add(criterion);
        }
        catch (Exception e) {
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
    public Criteria<T> addJoins(final List<Criterion> criterions) {

        // Loop through the join criterions.
        for (Criterion criterion : criterions) {

            // Add the join criterion.
            this.addJoin(criterion);
        }

        return this;
    }

    /**
     * Add a left join fetch hint.
     * 
     * @param  pathExpression  the path expression.
     */
    private void addLeftJoinFetchHint(final String pathExpression) {

        // Add a left join fetch hint.
        this.queryHints.put(pathExpression, QueryHints.LEFT_FETCH);
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
                this.ordersStringBuilder.append(" ORDER BY ");
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
                    this.ordersStringBuilder.append(" ASC");
                    break;
                case DESC:
                    this.ordersStringBuilder.append(" DESC");
                    break;
            }

            // Add the criterion to the orders.
            this.orders.add(criterion);
        }
        catch (Exception e) {
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
     * Get the bound next query parameter name.
     * 
     * @return  the bound next query parameter name.
     */
    private String boundNextQueryParameterName() {
        return new StringBuilder().append(":").append(this.nextQueryParameterName()).toString();
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

    private static <U> List<Criterion> getJoinCriterions(final Class<U> entityClass, final String operand) throws Exception {

        // Initialize the join criterions.
        List<Criterion> joinCriterions = new ArrayList<Criterion>();
        
        try {
            
            // Get the property, previous property, and new operand.
            String previousProperty = operand.substring(0, operand.indexOf("."));
            String newOperand = operand.substring(operand.indexOf(".") + 1);
            String property = newOperand.substring(0, newOperand.indexOf("."));

            // Get the field.
            Field field = entityClass.getDeclaredField(property);
            
            // Set the new entity class to the field type.
            Class<?> newEntityClass = field.getType();
            
            // The join condition was not found.
            boolean joinConditionFound = false;
            
            // Check if the new entity class is a List class.
            if (newEntityClass == List.class) {
                
                // Set the new entity class to the type of List class.
                newEntityClass = (Class<?>)((ParameterizedType)field.getGenericType()).getActualTypeArguments()[0];
                
                // The join condition was found.
                joinConditionFound = true;
            }
            
            // Get the join criterions for the new entity class and new operand.
            joinCriterions = getJoinCriterions(newEntityClass, newOperand);
            
            // Check if the join condition was found or there are join criterions.
            if (joinConditionFound || !joinCriterions.isEmpty()) {
                
                // Add a join criterion to the join criterions.
                joinCriterions.add(0, Join.join(qualifyOperand(property, previousProperty)));
            }
        }
        catch (IndexOutOfBoundsException e) {
            // Reached the end of the operand.
        }
         
        return joinCriterions;
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
     * Get the query hints.
     *
     * @return  the query hints.
     */
    public Map<Object, String> getQueryHints() {
        return this.queryHints;
    }

    /**
     * Get the query language string.
     * 
     * @return  the query language string.
     */
    public String getQueryLanguageString() {
        return new StringBuilder()
                .append("SELECT ")
                .append((this.joinExists()) ? "DISTINCT " : "")
                .append(this.entityAlias)
                .append(" FROM ")
                .append(this.entityClass.getSimpleName())
                .append(" ")
                .append(this.entityAlias)
                .append(this.joinsStringBuilder)
                .append(this.restrictionsStringBuilder)
                .append(this.ordersStringBuilder)
                .toString();
    }

    /**
     * Get the query parameters.
     * 
     * @return  the query parameters.
     */
    public Map<String, Object> getQueryParameters() {
        return this.queryParameters;
    }

    /**
     * Check if a join criterion exists.
     *
     * @return  true if a join criterion exists, otherwise false.
     */
    public boolean joinExists() {
        return !this.joins.isEmpty();
    }

    /**
     * Get the next query parameter name.
     * 
     * @return  the next query parameter name.
     */
    private String nextQueryParameterName() {
        return new StringBuilder().append("param").append(this.queryParameters.size() + 1).toString();
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

            Set<Class<?>> newVisitedEntityClasses = new HashSet<Class<?>>();
            newVisitedEntityClasses.addAll(visitedEntityClasses);
            
            // Add the entity class to the visited entity classes.
            newVisitedEntityClasses.add(entityClass);
            
            // Get the metamodel entity type.
            EntityType<U> entityType = this.entityManager.getMetamodel().entity(entityClass);

            // Loop through the attributes with multiple values.
            for (PluralAttribute<? super U, ?, ?> attribute : entityType.getPluralAttributes()) {

                // Check if the attribute can be optimized.
                if (collectionValuedAssociation(attribute)) {

                    // Get the new path expression for the attribute.
                    String newPathExpression = getPathExpression(pathExpression, attribute.getName());

                    // Add the batch fetch (join) hint.
                    this.addBatchFetchJoinHint(newPathExpression);    

                    // Optimize the query.
                    this.optimizeQuery(attribute.getBindableJavaType(), newPathExpression, newVisitedEntityClasses);
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
                    this.optimizeQuery(attribute.getBindableJavaType(), newPathExpression, newVisitedEntityClasses);
                }
            }
        }
    }

    /**
     * Check if a order criterion exists.
     *
     * @return  true if a order criterion exists, otherwise false.
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
    private String qualifyOperand(final String operand) {
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
    private static String qualifyOperand(final String operand, final String qualifier) {
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