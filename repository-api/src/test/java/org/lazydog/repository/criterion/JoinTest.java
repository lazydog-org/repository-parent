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
        Criterion expectedCriterion = new Criterion();
        expectedCriterion.setJoinOperator(Join.Operator.JOIN);
        expectedCriterion.setOperand("operand");
        assertEquals(expectedCriterion, Join.join(expectedCriterion.getOperand()));
    }
    
    @Test
    public void testJoinFetch() {
        Criterion expectedCriterion = new Criterion();
        expectedCriterion.setJoinOperator(Join.Operator.JOIN_FETCH);
        expectedCriterion.setOperand("operand");
        assertEquals(expectedCriterion, Join.joinFetch(expectedCriterion.getOperand()));
    }
    
    @Test
    public void testLeftJoin() {
        Criterion expectedCriterion = new Criterion();
        expectedCriterion.setJoinOperator(Join.Operator.LEFT_JOIN);
        expectedCriterion.setOperand("operand");
        assertEquals(expectedCriterion, Join.leftJoin(expectedCriterion.getOperand()));
    }
        
    @Test
    public void testLeftJoinFetch() {
        Criterion expectedCriterion = new Criterion();
        expectedCriterion.setJoinOperator(Join.Operator.LEFT_JOIN_FETCH);
        expectedCriterion.setOperand("operand");
        assertEquals(expectedCriterion, Join.leftJoinFetch(expectedCriterion.getOperand()));
    }
}

