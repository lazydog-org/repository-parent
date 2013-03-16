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

import java.util.ArrayList;
import java.util.List;
import static org.junit.Assert.assertEquals;
import org.junit.Test;

/**
 * Logical test.
 *
 * @author  Ron Rickard
 */
public class LogicalTest {
    
    @Test
    public void testAndOne() {
        Criterion expectedCriterion = Criterion.newInstance();
        expectedCriterion.setComparisonOperator(Comparison.Operator.EQUAL);
        expectedCriterion.setLogicalOperator(Logical.Operator.AND);
        expectedCriterion.setOperand("operand");
        expectedCriterion.setValue("value");
        assertEquals(expectedCriterion, Logical.and(Comparison.eq(expectedCriterion.getOperand(), expectedCriterion.getValue())));
    }
    
    @Test
    public void testAndMany() {
        List<Criterion> expectedCriterions = new ArrayList<Criterion>();
        Criterion criterionBegin = Criterion.newInstance();
        criterionBegin.setEnclosureOperator(Enclosure.Operator.BEGIN);
        criterionBegin.setLogicalOperator(Logical.Operator.AND);
        expectedCriterions.add(criterionBegin);
        Criterion criterion1 = Criterion.newInstance();
        criterion1.setComparisonOperator(Comparison.Operator.EQUAL);
        criterion1.setOperand("operand");
        criterion1.setValue("value1");
        expectedCriterions.add(criterion1);
        Criterion criterion2 = Criterion.newInstance();
        criterion2.setComparisonOperator(Comparison.Operator.EQUAL);
        criterion2.setLogicalOperator(Logical.Operator.OR);
        criterion2.setOperand("operand");
        criterion2.setValue("value2");
        expectedCriterions.add(criterion2);
        Criterion criterion3 = Criterion.newInstance();
        criterion3.setComparisonOperator(Comparison.Operator.EQUAL);
        criterion3.setLogicalOperator(Logical.Operator.OR);
        criterion3.setOperand("operand");
        criterion3.setValue("value3");
        expectedCriterions.add(criterion3);
        Criterion criterionEnd = Criterion.newInstance();
        criterionEnd.setEnclosureOperator(Enclosure.Operator.END);
        expectedCriterions.add(criterionEnd);
        assertEquals(expectedCriterions, Logical.and(Comparison.in(criterion1.getOperand(), criterion1.getValue(), criterion2.getValue(), criterion3.getValue())));
    }
        
    @Test
    public void testOrOne() {
        Criterion expectedCriterion = Criterion.newInstance();
        expectedCriterion.setComparisonOperator(Comparison.Operator.EQUAL);
        expectedCriterion.setLogicalOperator(Logical.Operator.OR);
        expectedCriterion.setOperand("operand");
        expectedCriterion.setValue("value");
        assertEquals(expectedCriterion, Logical.or(Comparison.eq(expectedCriterion.getOperand(), expectedCriterion.getValue())));
    }
    
    @Test
    public void testOrMany() {
        List<Criterion> expectedCriterions = new ArrayList<Criterion>();
        Criterion criterionBegin = Criterion.newInstance();
        criterionBegin.setEnclosureOperator(Enclosure.Operator.BEGIN);
        criterionBegin.setLogicalOperator(Logical.Operator.OR);
        expectedCriterions.add(criterionBegin);
        Criterion criterion1 = Criterion.newInstance();
        criterion1.setComparisonOperator(Comparison.Operator.EQUAL);
        criterion1.setOperand("operand");
        criterion1.setValue("value1");
        expectedCriterions.add(criterion1);
        Criterion criterion2 = Criterion.newInstance();
        criterion2.setComparisonOperator(Comparison.Operator.EQUAL);
        criterion2.setLogicalOperator(Logical.Operator.OR);
        criterion2.setOperand("operand");
        criterion2.setValue("value2");
        expectedCriterions.add(criterion2);
        Criterion criterion3 = Criterion.newInstance();
        criterion3.setComparisonOperator(Comparison.Operator.EQUAL);
        criterion3.setLogicalOperator(Logical.Operator.OR);
        criterion3.setOperand("operand");
        criterion3.setValue("value3");
        expectedCriterions.add(criterion3);
        Criterion criterionEnd = Criterion.newInstance();
        criterionEnd.setEnclosureOperator(Enclosure.Operator.END);
        expectedCriterions.add(criterionEnd);
        assertEquals(expectedCriterions, Logical.or(Comparison.in(criterion1.getOperand(), criterion1.getValue(), criterion2.getValue(), criterion3.getValue())));
    }
}

