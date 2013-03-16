/**
 * Copyright 2010-2013 lazydog.org.
 *
 * This file is part of repository.
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
package org.lazydog.repository.jpa.internal;

import java.io.Serializable;
import java.lang.reflect.Field;
import java.lang.reflect.ParameterizedType;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
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
     * @param  entityClass  the entity class.
     *
     * @throws  IllegalArgumentException  if the entity class is invalid.
     */
    public CriteriaImpl(final Class<T> entityClass) {

        // Check if the entity class is null.
        if (entityClass == null) {
            throw new IllegalArgumentException("The entity class is invalid.");
        }

        // Set the entity class.
        this.entityClass = entityClass;

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
     * Add a join criterion.
     * 
     * @param  criterion  the join criterion.
     * 
     * @return  the criteria.
     */
    public Criteria<T> addJoin(final Criterion criterion) {
        
        try {

            // Check if the join criterion is not already added.
            if (!this.joins.contains(criterion)) {
                
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
     * Get the join criterions.  This method is used to handle collection association fields.
     * 
     * @param  entityClass  the entity class.
     * @param  operand      the operand.
     * 
     * @return  the join criterions.
     * 
     * @throws  Exception  if unable to get the join criterions.
     */
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
}