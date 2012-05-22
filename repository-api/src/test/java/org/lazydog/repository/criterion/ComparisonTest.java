package org.lazydog.repository.criterion;

import java.util.ArrayList;
import java.util.List;
import static org.junit.Assert.assertEquals;
import org.junit.Test;


/**
 * Comparison test.
 * 
 * @author  Ron Rickard
 */
public class ComparisonTest {
    
    @Test
    public void testBetween() {
        List<Criterion> expectedCriterions = new ArrayList<Criterion>();
        Criterion criterion1 = new Criterion();
        criterion1.setComparisonOperator(Comparison.Operator.GREATER_THAN_OR_EQUAL);
        criterion1.setOperand("operand");
        criterion1.setValue("value1");
        expectedCriterions.add(criterion1);
        Criterion criterion2 = new Criterion();
        criterion2.setComparisonOperator(Comparison.Operator.LESS_THAN_OR_EQUAL);
        criterion2.setLogicalOperator(Logical.Operator.AND);
        criterion2.setOperand("operand");
        criterion2.setValue("value2");
        expectedCriterions.add(criterion2);
        assertEquals(expectedCriterions, Comparison.between(criterion1.getOperand(), criterion1.getValue(), criterion2.getValue()));
    }
    
    @Test
    public void testEq() {
        Criterion expectedCriterion = new Criterion();
        expectedCriterion.setComparisonOperator(Comparison.Operator.EQUAL);
        expectedCriterion.setOperand("operand");
        expectedCriterion.setValue("value");
        assertEquals(expectedCriterion, Comparison.eq(expectedCriterion.getOperand(), expectedCriterion.getValue()));
    }
    
    @Test
    public void testGe() {
        Criterion expectedCriterion = new Criterion();
        expectedCriterion.setComparisonOperator(Comparison.Operator.GREATER_THAN_OR_EQUAL);
        expectedCriterion.setOperand("operand");
        expectedCriterion.setValue("value");
        assertEquals(expectedCriterion, Comparison.ge(expectedCriterion.getOperand(), expectedCriterion.getValue()));
    }
    
    @Test
    public void testGt() {
        Criterion expectedCriterion = new Criterion();
        expectedCriterion.setComparisonOperator(Comparison.Operator.GREATER_THAN);
        expectedCriterion.setOperand("operand");
        expectedCriterion.setValue("value");
        assertEquals(expectedCriterion, Comparison.gt(expectedCriterion.getOperand(), expectedCriterion.getValue()));
    }
    
    @Test
    public void testInOne() {
        List<Criterion> expectedCriterions = new ArrayList<Criterion>();
        Criterion criterion1 = new Criterion();
        criterion1.setComparisonOperator(Comparison.Operator.EQUAL);
        criterion1.setOperand("operand");
        criterion1.setValue("value");
        expectedCriterions.add(criterion1);
        assertEquals(expectedCriterions, Comparison.in(criterion1.getOperand(), criterion1.getValue()));
    }
        
    @Test
    public void testInMany() {
        List<Criterion> expectedCriterions = new ArrayList<Criterion>();
        Criterion criterion1 = new Criterion();
        criterion1.setComparisonOperator(Comparison.Operator.EQUAL);
        criterion1.setEnclosureOperator(Enclosure.Operator.BEGIN);
        criterion1.setOperand("operand");
        criterion1.setValue("value1");
        expectedCriterions.add(criterion1);
        Criterion criterion2 = new Criterion();
        criterion2.setComparisonOperator(Comparison.Operator.EQUAL);
        criterion2.setLogicalOperator(Logical.Operator.OR);
        criterion2.setOperand("operand");
        criterion2.setValue("value2");
        expectedCriterions.add(criterion2);
        Criterion criterion3 = new Criterion();
        criterion3.setComparisonOperator(Comparison.Operator.EQUAL);
        criterion3.setEnclosureOperator(Enclosure.Operator.END);
        criterion3.setLogicalOperator(Logical.Operator.OR);
        criterion3.setOperand("operand");
        criterion3.setValue("value3");
        expectedCriterions.add(criterion3);
        assertEquals(expectedCriterions, Comparison.in(criterion1.getOperand(), criterion1.getValue(), criterion2.getValue(), criterion3.getValue()));
    }
    
    @Test
    public void testIsEmpty() {
        Criterion expectedCriterion = new Criterion();
        expectedCriterion.setComparisonOperator(Comparison.Operator.IS_EMPTY);
        expectedCriterion.setOperand("operand");
        assertEquals(expectedCriterion, Comparison.isEmpty(expectedCriterion.getOperand()));
    }
    
    @Test
    public void testIsNotEmpty() {
        Criterion expectedCriterion = new Criterion();
        expectedCriterion.setComparisonOperator(Comparison.Operator.IS_NOT_EMPTY);
        expectedCriterion.setOperand("operand");
        assertEquals(expectedCriterion, Comparison.isNotEmpty(expectedCriterion.getOperand()));
    }
    
    @Test
    public void testIsNotNull() {
        Criterion expectedCriterion = new Criterion();
        expectedCriterion.setComparisonOperator(Comparison.Operator.IS_NOT_NULL);
        expectedCriterion.setOperand("operand");
        assertEquals(expectedCriterion, Comparison.isNotNull(expectedCriterion.getOperand()));
    }
    
    @Test
    public void testIsNull() {
        Criterion expectedCriterion = new Criterion();
        expectedCriterion.setComparisonOperator(Comparison.Operator.IS_NULL);
        expectedCriterion.setOperand("operand");
        assertEquals(expectedCriterion, Comparison.isNull(expectedCriterion.getOperand()));
    }
    
    @Test
    public void testLe() {
        Criterion expectedCriterion = new Criterion();
        expectedCriterion.setComparisonOperator(Comparison.Operator.LESS_THAN_OR_EQUAL);
        expectedCriterion.setOperand("operand");
        expectedCriterion.setValue("value");
        assertEquals(expectedCriterion, Comparison.le(expectedCriterion.getOperand(), expectedCriterion.getValue()));
    }
    
    @Test
    public void testLike() {
        Criterion expectedCriterion = new Criterion();
        expectedCriterion.setComparisonOperator(Comparison.Operator.LIKE);
        expectedCriterion.setOperand("operand");
        expectedCriterion.setValue("value");
        assertEquals(expectedCriterion, Comparison.like(expectedCriterion.getOperand(), expectedCriterion.getValue()));
    }
    
    @Test
    public void testLt() {
        Criterion expectedCriterion = new Criterion();
        expectedCriterion.setComparisonOperator(Comparison.Operator.LESS_THAN);
        expectedCriterion.setOperand("operand");
        expectedCriterion.setValue("value");
        assertEquals(expectedCriterion, Comparison.lt(expectedCriterion.getOperand(), expectedCriterion.getValue()));
    }
    
    @Test
    public void testMemberOf() {
        Criterion expectedCriterion = new Criterion();
        expectedCriterion.setComparisonOperator(Comparison.Operator.MEMBER_OF);
        expectedCriterion.setOperand("operand");
        expectedCriterion.setValue("value");
        assertEquals(expectedCriterion, Comparison.memberOf(expectedCriterion.getOperand(), expectedCriterion.getValue()));
    }
    
    @Test
    public void testNe() {
        Criterion expectedCriterion = new Criterion();
        expectedCriterion.setComparisonOperator(Comparison.Operator.NOT_EQUAL);
        expectedCriterion.setOperand("operand");
        expectedCriterion.setValue("value");
        assertEquals(expectedCriterion, Comparison.ne(expectedCriterion.getOperand(), expectedCriterion.getValue()));
    }
    
    @Test
    public void testNotBetween() {
        List<Criterion> expectedCriterions = new ArrayList<Criterion>();
        Criterion criterion1 = new Criterion();
        criterion1.setComparisonOperator(Comparison.Operator.LESS_THAN);
        criterion1.setOperand("operand");
        criterion1.setValue("value1");
        expectedCriterions.add(criterion1);
        Criterion criterion2 = new Criterion();
        criterion2.setComparisonOperator(Comparison.Operator.GREATER_THAN);
        criterion2.setLogicalOperator(Logical.Operator.OR);
        criterion2.setOperand("operand");
        criterion2.setValue("value2");
        expectedCriterions.add(criterion2);
        assertEquals(expectedCriterions, Comparison.notBetween(criterion1.getOperand(), criterion1.getValue(), criterion2.getValue()));
    }
        
    @Test
    public void testNotInOne() {
        List<Criterion> expectedCriterions = new ArrayList<Criterion>();
        Criterion criterion1 = new Criterion();
        criterion1.setComparisonOperator(Comparison.Operator.NOT_EQUAL);
        criterion1.setOperand("operand");
        criterion1.setValue("value");
        expectedCriterions.add(criterion1);
        assertEquals(expectedCriterions, Comparison.notIn(criterion1.getOperand(), criterion1.getValue()));
    }
        
    @Test
    public void testNotInMany() {
        List<Criterion> expectedCriterions = new ArrayList<Criterion>();
        Criterion criterion1 = new Criterion();
        criterion1.setComparisonOperator(Comparison.Operator.NOT_EQUAL);
        criterion1.setEnclosureOperator(Enclosure.Operator.BEGIN);
        criterion1.setOperand("operand");
        criterion1.setValue("value1");
        expectedCriterions.add(criterion1);
        Criterion criterion2 = new Criterion();
        criterion2.setComparisonOperator(Comparison.Operator.NOT_EQUAL);
        criterion2.setLogicalOperator(Logical.Operator.AND);
        criterion2.setOperand("operand");
        criterion2.setValue("value2");
        expectedCriterions.add(criterion2);
        Criterion criterion3 = new Criterion();
        criterion3.setComparisonOperator(Comparison.Operator.NOT_EQUAL);
        criterion3.setEnclosureOperator(Enclosure.Operator.END);
        criterion3.setLogicalOperator(Logical.Operator.AND);
        criterion3.setOperand("operand");
        criterion3.setValue("value3");
        expectedCriterions.add(criterion3);
        assertEquals(expectedCriterions, Comparison.notIn(criterion1.getOperand(), criterion1.getValue(), criterion2.getValue(), criterion3.getValue()));
    }
    
    @Test
    public void testNotLike() {
        Criterion expectedCriterion = new Criterion();
        expectedCriterion.setComparisonOperator(Comparison.Operator.NOT_LIKE);
        expectedCriterion.setOperand("operand");
        expectedCriterion.setValue("value");
        assertEquals(expectedCriterion, Comparison.notLike(expectedCriterion.getOperand(), expectedCriterion.getValue()));
    }
    
    @Test
    public void testNotMemberOf() {
        Criterion expectedCriterion = new Criterion();
        expectedCriterion.setComparisonOperator(Comparison.Operator.NOT_MEMBER_OF);
        expectedCriterion.setOperand("operand");
        expectedCriterion.setValue("value");
        assertEquals(expectedCriterion, Comparison.notMemberOf(expectedCriterion.getOperand(), expectedCriterion.getValue()));
    }
}
