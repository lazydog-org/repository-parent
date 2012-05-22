package org.lazydog.repository.criterion;

import static org.junit.Assert.assertEquals;
import org.junit.Test;


/**
 * Enclosure test.
 *
 * @author  Ron Rickard
 */
public class EnclosureTest {
    
    @Test
    public void testBegin() {
        Criterion expectedCriterion = new Criterion();
        expectedCriterion.setComparisonOperator(Comparison.Operator.EQUAL);
        expectedCriterion.setEnclosureOperator(Enclosure.Operator.BEGIN);
        expectedCriterion.setOperand("operand");
        expectedCriterion.setValue("value");
        assertEquals(expectedCriterion, Enclosure.begin(Comparison.eq(expectedCriterion.getOperand(), expectedCriterion.getValue())));
    }
    
    @Test
    public void testEnd() {
        Criterion expectedCriterion = new Criterion();
        expectedCriterion.setComparisonOperator(Comparison.Operator.EQUAL);
        expectedCriterion.setEnclosureOperator(Enclosure.Operator.END);
        expectedCriterion.setOperand("operand");
        expectedCriterion.setValue("value");
        assertEquals(expectedCriterion, Enclosure.end(Comparison.eq(expectedCriterion.getOperand(), expectedCriterion.getValue())));
    }
}

