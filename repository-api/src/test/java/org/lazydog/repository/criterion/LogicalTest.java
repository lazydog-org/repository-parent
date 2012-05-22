package org.lazydog.repository.criterion;

import static org.junit.Assert.assertEquals;
import org.junit.Test;


/**
 * Logical test.
 *
 * @author  Ron Rickard
 */
public class LogicalTest {
    
    @Test
    public void testAnd() {
        Criterion expectedCriterion = new Criterion();
        expectedCriterion.setComparisonOperator(Comparison.Operator.EQUAL);
        expectedCriterion.setLogicalOperator(Logical.Operator.AND);
        expectedCriterion.setOperand("operand");
        expectedCriterion.setValue("value");
        assertEquals(expectedCriterion, Logical.and(Comparison.eq(expectedCriterion.getOperand(), expectedCriterion.getValue())));
    }
    
    @Test
    public void testOr() {
        Criterion expectedCriterion = new Criterion();
        expectedCriterion.setComparisonOperator(Comparison.Operator.EQUAL);
        expectedCriterion.setLogicalOperator(Logical.Operator.OR);
        expectedCriterion.setOperand("operand");
        expectedCriterion.setValue("value");
        assertEquals(expectedCriterion, Logical.or(Comparison.eq(expectedCriterion.getOperand(), expectedCriterion.getValue())));
    }
}

