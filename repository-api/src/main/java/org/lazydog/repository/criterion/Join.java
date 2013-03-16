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

/**
 * Join.
 * 
 * @author  Ron Rickard
 */
public final class Join {

    /**
     * Join operator.
     */
    public enum Operator {
        JOIN,
        JOIN_FETCH,
        LEFT_JOIN,
        LEFT_JOIN_FETCH,
        UNDEFINED;
    };
    
    /**
     * Hide the constructor.
     */
    private Join() {    
    }
       
    /**
     *Join operation.
     * 
     * @param  operand  the operand.
     * 
     * @return  the resulting criterion.
     */
    public static Criterion join(final String operand) {
        return Join.operation(Join.Operator.JOIN, operand);
    }
       
    /**
     *Join fetch operation.
     * 
     * @param  operand  the operand.
     * 
     * @return  the resulting criterion.
     */
    public static Criterion joinFetch(final String operand) {
        return Join.operation(Join.Operator.JOIN_FETCH, operand);
    }
           
    /**
     * Left join operation.
     * 
     * @param  operand  the operand.
     * 
     * @return  the resulting criterion.
     */
    public static Criterion leftJoin(final String operand) {
        return Join.operation(Join.Operator.LEFT_JOIN, operand);
    }
               
    /**
     * Left join fetch operation.
     * 
     * @param  operand  the operand.
     * 
     * @return  the resulting criterion.
     */
    public static Criterion leftJoinFetch(final String operand) {
        return Join.operation(Join.Operator.LEFT_JOIN_FETCH, operand);
    }
    
    /**
     * Join operation.
     * 
     * @param  joinOperator  the join operator.
     * @param  operand       the operand.
     * 
     * @return  the resulting criterion.
     */
    private static Criterion operation(final Join.Operator joinOperator, final String operand) {
        
        // Set the criterion.
        Criterion criterion = Criterion.newInstance();
        criterion.setJoinOperator(joinOperator);
        criterion.setOperand(operand);
        
        return criterion;
    }
}
