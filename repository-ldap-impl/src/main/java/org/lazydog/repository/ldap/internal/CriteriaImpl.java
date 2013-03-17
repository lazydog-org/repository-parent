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
package org.lazydog.repository.ldap.internal;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import org.lazydog.repository.Criteria;
import org.lazydog.repository.criterion.Criterion;

/**
 * Criteria implementation using the Java Naming and Directory Interface.
 * 
 * @author  Ron Rickard
 */
public final class CriteriaImpl<T> implements Criteria<T>, Serializable {

    private static final long serialVersionUID = 1L;
    private StringBuilder filterStringBuilder;
    private Map<String,String> propertyAttributeMap;
    private List<Criterion> restrictions;
    private String searchBase;
    private SearchScope searchScope;
	
    /**
     * Constructor.
     *
     * @param  objectClassValues     the object class values.
     * @param  propertyAttributeMap  the property name-attribute name map.
     * @param  searchBase            the search base.
     * @param  searchScope           the search scope.
     *
     * @throws  IllegalArgumentException  if the object class values, the property 
     *                                    name-attribute name map, search base, or search scope is invalid.
     */
    public CriteriaImpl(final Set<String> objectClassValues, final Map<String,String> propertyAttributeMap, final String searchBase, final SearchScope searchScope) {

        // Check if the object class values is invalid.
        if (objectClassValues == null || objectClassValues.size() < 1) {
            throw new IllegalArgumentException("The object class values are invalid.");
        }

        // Check if the property name-attribute name map is invalid.
        if (propertyAttributeMap == null || propertyAttributeMap.size() < 1) {
            throw new IllegalArgumentException("The property name-attribute name map is invalid.");
        }

        // Check if the search base is invalid.
        if (searchBase == null || "".equals(searchBase)) {
            throw new IllegalArgumentException("The search base is invalid.");
        }
        
        // Check if the search scope is invalid.
        if (searchScope == null) {
            throw new IllegalArgumentException("The search scope is invalid.");
        }
        
        // Initialize the filter string builder.
        this.filterStringBuilder = new StringBuilder();
        this.filterStringBuilder.append((objectClassValues.size() == 1) ? "" : "(&");
        for (String objectClassValue : objectClassValues) {
            this.filterStringBuilder
                    .append("(objectclass=")
                    .append(objectClassValue)
                    .append(")");
        }
        this.filterStringBuilder.append((objectClassValues.size() == 1) ? "" : ")");
        
        // Set the property name-attribute name map.
        this.propertyAttributeMap = propertyAttributeMap;
        
        // Initialize the restrictions.
        this.restrictions = new ArrayList<Criterion>();
        
        // Initialize the search base and scope.
        this.searchBase = searchBase;
        this.searchScope = searchScope;
    }

    /**
     * Add a restriction criterion.
     *
     * @param  criterion  the restriction criterion.
     *
     * @return  the criteria.
     */
    @Override
    public CriteriaImpl<T> add(final Criterion criterion) {

        // Check if a restriction has not already been processed.
        if (!this.restrictionExists()) {

            // Add the AND logical operator to the filter string builder.
            this.filterStringBuilder.insert(0, "(&");
        } else {

            // Add the logical operator to the restrictions string builder.
            switch (criterion.getLogicalOperator()) {

                case AND:
                    this.filterStringBuilder.insert(0, "(&");
                    break;
                case OR:
                    this.filterStringBuilder.insert(0, "(|");
                    break;
            }
        }

        String attributeName = propertyAttributeMap.get(criterion.getOperand());
        
        // Add the operand, comparison operator, and parameter if
        // necessary to the restrictions string builder.
        switch (criterion.getComparisonOperator()) {

            case EQUAL:
                this.filterStringBuilder
                        .append("(")
                        .append(attributeName)
                        .append("=")
                        .append(criterion.getValue())
                        .append("))");
                break;
            case GREATER_THAN:
            	throw new UnsupportedOperationException("Comparison greaterThan is not applicable to this repository implementation.");
            case GREATER_THAN_OR_EQUAL:
            	this.filterStringBuilder
                        .append("(")
                        .append(attributeName)
                        .append(">=")
                        .append(criterion.getValue())
                        .append("))");
                break;
            case IS_EMPTY:
            	throw new UnsupportedOperationException("Comparison isEmpty is not applicable to this repository implementation.");
            case IS_NOT_EMPTY:
            	throw new UnsupportedOperationException("Comparison isNotEmpty is not applicable to this repository implementation.");
            case IS_NULL:
            	throw new UnsupportedOperationException("Comparison isNull is not applicable to this repository implementation.");
            case IS_NOT_NULL:
            	throw new UnsupportedOperationException("Comparison isNotNull is not applicable to this repository implementation.");
            case LESS_THAN:
            	throw new UnsupportedOperationException("Comparison lessThan is not applicable to this repository implementation.");
            case LESS_THAN_OR_EQUAL:
            	this.filterStringBuilder
                        .append("(")
                        .append(attributeName)
                        .append(">=")
                        .append(criterion.getValue())
                        .append("))");
                break;
            case LIKE:
            	throw new UnsupportedOperationException("Comparison like is not applicable to this repository implementation.");
            case MEMBER_OF:
            	throw new UnsupportedOperationException("Comparison memberOf is not applicable to this repository implementation.");
            case NOT_EQUAL:
            	this.filterStringBuilder
                        .append("(!")
                        .append(attributeName)
                        .append("=")
                        .append(criterion.getValue())
                        .append("))");
            	break;
            case NOT_LIKE:
            	throw new UnsupportedOperationException("Comparison notLike is not applicable to this repository implementation.");
            case NOT_MEMBER_OF:
            	throw new UnsupportedOperationException("Comparison notMemberOf is not applicable to this repository implementation.");
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
    public CriteriaImpl<T> add(final List<Criterion> criterions) {
    	
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
    public CriteriaImpl<T> addOrder(final Criterion criterion) {
    	throw new UnsupportedOperationException("Order is not applicable to this repository implementation.");
    }

    /**
     * Add order criterions.
     *
     * @param  criterions  the order criterions.
     *
     * @return  the criteria.
     */
    @Override
    public CriteriaImpl<T> addOrders(final List<Criterion> criterions) {
    	throw new UnsupportedOperationException("Order is not applicable to this repository implementation.");
    }

    /**
     * Get the filter.
     * 
     * @return  the filter.
     */
    public String getFilter() {
        return this.filterStringBuilder.toString();
    }

    /**
     * Get the search base.
     * 
     * @return  the search base.
     */
    public String getSearchBase() {
    	return this.searchBase;
    }
    
    /**
     * Get the search scope.
     * 
     * @return  the search scope.
     */
    public SearchScope getSearchScope() {
    	return this.searchScope;
    }
    
    /**
     * Check if an order criterion exists.
     *
     * @return  true if an order criterion exists, otherwise false.
     */
    @Override
    public boolean orderExists() {
    	return false;
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
     * Get this object as a String.
     *
     * @return  this object as a String.
     */
    @Override
    public String toString() {
        return new StringBuilder()
                .append("CriteriaImpl [")
                .append("filter = ").append(this.getFilter())
                .append(", searchBase = ").append(this.getSearchBase())
                .append(", searchScope = ").append(this.getSearchScope())
                .append("]")
                .toString();
    }
}
