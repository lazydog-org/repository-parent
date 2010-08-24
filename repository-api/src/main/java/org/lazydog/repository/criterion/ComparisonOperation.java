package org.lazydog.repository.criterion;

import java.util.ArrayList;
import java.util.List;


/**
 * Comparison operation.
 * 
 * @author  Ron Rickard
 */
public class ComparisonOperation {

    /**
     * Between comparison operation.
     * 
     * @param  operand  the operand.
     * @param  value1   the first value.
     * @param  value2   the second value.
     * 
     * @return  the resulting criterions.
     */
    public static List<Criterion> between(String operand,
                                          Object value1,
                                          Object value2) {
        
        // Declare.
        List<Criterion> criterions;
        
        // Initialize.
        criterions = new ArrayList<Criterion>();
        
        // x BETWEEN y AND z is equivalent to x >= y AND x <= z
        criterions.add(ComparisonOperation.op(
            ComparisonOperator.GREATER_THAN_OR_EQUAL, operand, value1));
        criterions.add(LogicalOperation.and(ComparisonOperation.op(
            ComparisonOperator.LESS_THAN_OR_EQUAL, operand, value2)));
        
        return criterions;
    }
      
    /**
     * Equal comparison operation.
     * 
     * @param  operand  the operand.
     * @param  value    the value.
     * 
     * @return  the resulting criterion.
     */
    public static Criterion eq(String operand,
                               Object value) {
        return ComparisonOperation.op(
            ComparisonOperator.EQUAL, operand, value);
    }
              
    /**
     * Greater than or equal comparison operation.
     * 
     * @param  operand  the operand.
     * @param  value    the value.
     * 
     * @return  the resulting criterion.
     */
    public static Criterion ge(String operand,
                               Object value) {
        return ComparisonOperation.op(
            ComparisonOperator.GREATER_THAN_OR_EQUAL, operand, value);
    }
      
    /**
     * Greater than comparison operation.
     * 
     * @param  operand  the operand.
     * @param  value    the value.
     * 
     * @return  the resulting criterion.
     */
    public static Criterion gt(String operand,
                               Object value) {
        return ComparisonOperation.op(
            ComparisonOperator.GREATER_THAN, operand, value);
    }
      
    /**
     * Is empty comparison operation.
     * 
     * @param  operand  the operand.
     * 
     * @return  the resulting criterion.
     */
    public static Criterion isEmpty(String operand) {
        return ComparisonOperation.op(
            ComparisonOperator.IS_EMPTY, operand);
    }
               
    /**
     * Is not empty comparison operation.
     * 
     * @param  operand  the operand.
     * 
     * @return  the resulting criterion.
     */
    public static Criterion isNotEmpty(String operand) {
        return ComparisonOperation.op(
            ComparisonOperator.IS_NOT_EMPTY, operand);
    }
    
    /**
     * Is not null comparison operation.
     * 
     * @param  operand  the operand.
     * 
     * @return  the resulting criterion.
     */
    public static Criterion isNotNull(String operand) {
        return ComparisonOperation.op(
            ComparisonOperator.IS_NOT_NULL, operand);
    }
    
    /**
     * Is null comparison operation.
     * 
     * @param  operand  the operand.
     * 
     * @return  the resulting criterion.
     */
    public static Criterion isNull(String operand) {
        return ComparisonOperation.op(
            ComparisonOperator.IS_NULL, operand);
    }
    
    /**
     * Less than or equal comparison operation.
     * 
     * @param  operand  the operand.
     * @param  value    the value.
     * 
     * @return  the resulting criterion.
     */
    public static Criterion le(String operand,
                               Object value) {
        return ComparisonOperation.op(
            ComparisonOperator.LESS_THAN_OR_EQUAL, operand, value);
    }
      
    /**
     * Like comparison operation.
     * 
     * @param  operand  the operand.
     * @param  value    the value.
     * 
     * @return  the resulting criterion.
     */
    public static Criterion like(String operand,
                                 Object value) {
        return ComparisonOperation.op(
            ComparisonOperator.LIKE, operand, value);
    }
          
    /**
     * Less than comparison operation.
     * 
     * @param  operand  the operand.
     * @param  value    the value.
     * 
     * @return  the resulting criterion.
     */
    public static Criterion lt(String operand,
                               Object value) {
        return ComparisonOperation.op(
            ComparisonOperator.LESS_THAN, operand, value);
    }
     
    /**
     * Member of comparison operation.
     * 
     * @param operand  the operand.
     * @param value    the value.
     * 
     * @return  the resulting criterion.
     */
    public static Criterion memberOf(String operand,
                                     Object value) {
        return ComparisonOperation.op(
            ComparisonOperator.MEMBER_OF, operand, value);
    }
    
    /**
     * Not equal comparison operation.
     * 
     * @param  operand  the operand.
     * @param  value    the value.
     * 
     * @return  the resulting criterion.
     */
    public static Criterion ne(String operand,
                               Object value) {
        return ComparisonOperation.op(
            ComparisonOperator.NOT_EQUAL, operand, value);
    }
                
    /**
     * Not between comparison operation.
     * 
     * @param  operand  the property name.
     * @param  value1   the first value.
     * @param  value2   the second value.
     * 
     * @return  the resulting criterions.
     */
    public static List<Criterion> notBetween(String operand,
                                             Object value1,
                                             Object value2) {
        
        // Declare.
        List<Criterion> criterions;
        
        // Initialize.
        criterions = new ArrayList<Criterion>();
        
        // x NOT BETWEEN y AND z is equivalent to x < y AND x > z
        criterions.add(ComparisonOperation.op(
            ComparisonOperator.LESS_THAN, operand, value1));
        criterions.add(LogicalOperation.and(ComparisonOperation.op(
            ComparisonOperator.GREATER_THAN, operand, value2)));
        
        return criterions;
    }
            
    /**
     * Not like comparison operation.
     * 
     * @param  operand  the operand.
     * @param  value    the value.
     * 
     * @return  the resulting criterion.
     */
    public static Criterion notLike(String operand,
                                    Object value) {
        return ComparisonOperation.op(
            ComparisonOperator.NOT_LIKE, operand, value);
    }
          
    /**
     * Not member of comparison operation.
     * 
     * @param operand  the operand.
     * @param value    the value.
     * 
     * @return  the resulting criterion.
     */
    public static Criterion notMemberOf(String operand,
                                        Object value) {
        return ComparisonOperation.op(
            ComparisonOperator.NOT_MEMBER_OF, operand, value);
    }
       
    /**
     * Comparison operation.
     * 
     * @param  comparisonOperator  the comparison operator.
     * @param  operand             the operand.
     * 
     * @return  the resulting criterion.
     */
    private static Criterion op(ComparisonOperator comparisonOperator,
                                String operand) {
        
        // Declare.
        Criterion criterion;

        // Set the criterion.
        criterion = new Criterion();
        criterion.setComparisonOperator(comparisonOperator);
        criterion.setOperand(operand);
        
        return criterion;
    }
    
    /**
     * Comparison operation.
     * 
     * @param  comparisonOperator  the comparison operator.
     * @param  operand             the operand.
     * @param  value               the value.
     * 
     * @return  the resulting criterion.
     */
    private static Criterion op(ComparisonOperator comparisonOperator,
                                String operand,
                                Object value) {
        
        // Declare.
        Criterion criterion;

        // Set the criterion.
        criterion = new Criterion();
        criterion.setComparisonOperator(comparisonOperator);
        criterion.setOperand(operand);
        criterion.setValue(value);
        
        return criterion;
    }
}
