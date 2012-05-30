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
        Criterion expectedCriterion = Criterion.newInstance();
        expectedCriterion.setEnclosureOperator(Enclosure.Operator.BEGIN);
        assertEquals(expectedCriterion, Enclosure.begin());
    }
    
    @Test
    public void testEnd() {
        Criterion expectedCriterion = Criterion.newInstance();
        expectedCriterion.setEnclosureOperator(Enclosure.Operator.END);
        assertEquals(expectedCriterion, Enclosure.end());
    }
}

