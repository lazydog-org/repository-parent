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

import static org.junit.Assert.assertEquals;
import org.junit.Test;

/**
 * Join test.
 *
 * @author  Ron Rickard
 */
public class JoinTest {
    
    @Test
    public void testJoin() {
        Criterion expectedCriterion = Criterion.newInstance();
        expectedCriterion.setJoinOperator(Join.Operator.JOIN);
        expectedCriterion.setOperand("operand");
        assertEquals(expectedCriterion, Join.join(expectedCriterion.getOperand()));
    }
    
    @Test
    public void testJoinFetch() {
        Criterion expectedCriterion = Criterion.newInstance();
        expectedCriterion.setJoinOperator(Join.Operator.JOIN_FETCH);
        expectedCriterion.setOperand("operand");
        assertEquals(expectedCriterion, Join.joinFetch(expectedCriterion.getOperand()));
    }
    
    @Test
    public void testLeftJoin() {
        Criterion expectedCriterion = Criterion.newInstance();
        expectedCriterion.setJoinOperator(Join.Operator.LEFT_JOIN);
        expectedCriterion.setOperand("operand");
        assertEquals(expectedCriterion, Join.leftJoin(expectedCriterion.getOperand()));
    }
        
    @Test
    public void testLeftJoinFetch() {
        Criterion expectedCriterion = Criterion.newInstance();
        expectedCriterion.setJoinOperator(Join.Operator.LEFT_JOIN_FETCH);
        expectedCriterion.setOperand("operand");
        assertEquals(expectedCriterion, Join.leftJoinFetch(expectedCriterion.getOperand()));
    }
}

