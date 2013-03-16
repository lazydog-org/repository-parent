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
 * Order.
 * 
 * @author  Ron Rickard
 */
public final class Order {

    /**
    * Order direction.
    */
    public enum Direction {
        ASC,
        DESC,
        UNDEFINED;
    };

    /**
     * Hide the constructor.
     */
    private Order() {    
    }
    
    /**
     * Ascending order.
     * 
     * @param  operand  the operand.
     * 
     * @return  the resulting criterion.
     */
    public static Criterion asc(final String operand) {
        return Order.order(Order.Direction.ASC, operand);
    }
    
    /**
     * Descending order.
     * 
     * @param  operand  the operand.
     * 
     * @return  the resulting criterion.
     */
    public static Criterion desc(final String operand) {
        return Order.order(Order.Direction.DESC, operand);
    }
           
    /**
     * Order.
     * 
     * @param  orderDirection  the order direction.
     * @param  operand         the operand.
     * 
     * @return  the resulting criterion.
     */
    private static Criterion order(final Order.Direction orderDirection, final String operand) {

        // Set the criterion.
        Criterion criterion = Criterion.newInstance();
        criterion.setOrderDirection(orderDirection);
        criterion.setOperand(operand);
        
        return criterion;
    }
}
