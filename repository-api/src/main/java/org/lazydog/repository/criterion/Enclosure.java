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
 * Enclosure.
 * 
 * @author rrickard
 */
public final class Enclosure {

    /**
     * Enclosure operator.
     */
    public enum Operator {
        BEGIN,
        END,
        UNDEFINED;
    };
    
    /**
     * Hide the constructor.
     */
    private Enclosure() {    
    }
    
    /**
     * Begin enclosure operation.
     * 
     * @return  the resulting criterion.
     */
    public static Criterion begin() {
        return Enclosure.operation(Enclosure.Operator.BEGIN);
    }
        
    /**
     * End enclosure operation.
     * 
     * @return  the resulting criterion.
     */
    public static Criterion end() {
        return Enclosure.operation(Enclosure.Operator.END);
    }
    
    /**
     * Enclosure operation.
     * 
     * @param  enclosureOperator  the enclosure operator.
     * 
     * @return  the resulting criterion.
     */
    private static Criterion operation(final Enclosure.Operator enclosureOperator) {
        
        // Set the criterion.
        Criterion criterion = Criterion.newInstance();
        criterion.setEnclosureOperator(enclosureOperator);

        return criterion;
    }
}
