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
package org.lazydog.repository.criterion;

import java.util.List;

/**
 * Logical.
 * 
 * @author  Ron Rickard
 */
public final class Logical {

    /**
     * Logical operator.
     */
    public enum Operator {
        AND,
        OR,
        UNDEFINED;
    };

    /**
     * Hide the constructor.
     */
    private Logical() {    
    }
    
    /**
     * And logical operation.
     * 
     * @param  criterion  the criterion.
     * 
     * @return  the resulting criterion.
     */
    public static Criterion and(final Criterion criterion) {
        return Logical.operation(Logical.Operator.AND, criterion);
    }
    
    /**
     * And logical operation.
     * 
     * @param  criterions  the criterions.
     * 
     * @return  the resulting criterions.
     */
    public static List<Criterion> and(final List<Criterion> criterions) {
        return Logical.operation(Logical.Operator.AND, criterions);
    }
    
    /**
     * Logical operation.
     * 
     * @param  logicalOperator  the logical operator.
     * @param  criterion        the criterion.
     * 
     * @return  the resulting criterion.
     */
    private static Criterion operation(final Logical.Operator logicalOperator, final Criterion criterion) {
        
        // Set the criterion logical operator.
        criterion.setLogicalOperator(logicalOperator);

        return criterion;
    }
        
    /**
     * Logical operation.
     * 
     * @param  logicalOperator  the logical operator.
     * @param  criterions       the criterions.
     * 
     * @return  the resulting criterion.
     */
    private static List<Criterion> operation(final Logical.Operator logicalOperator, final List<Criterion> criterions) {
        
        // Set the criterion logical operator.
        criterions.get(0).setLogicalOperator(logicalOperator);

        return criterions;
    }
    
    /**
     * Or logical operation.
     * 
     * @param  criterion  the criterion.
     * 
     * @return  the resulting criterion.
     */
    public static Criterion or(final Criterion criterion) {
        return Logical.operation(Logical.Operator.OR, criterion);
    }
    
    /**
     * Or logical operation.
     * 
     * @param  criterions  the criterions.
     * 
     * @return  the resulting criterion.
     */
    public static List<Criterion> or(final List<Criterion> criterions) {
        return Logical.operation(Logical.Operator.OR, criterions);
    }
}
