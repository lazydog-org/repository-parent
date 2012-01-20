package org.lazydog.repository.criterion;

import java.util.ArrayList;
import java.util.List;


/**
 * Comparison operation.
 * 
 * @author  Ron Rickard
 */
public final class ComparisonOperation {

    /**
     * Private constructor.
     */
    private ComparisonOperation() {     
    }
   
    /**
     * Between comparison operation.
     * 
     * @param  operand  the operand.
     * @param  value1   the first value.
     * @param  value2   the second value.
     * 
     * @return  the resulting criterions.
     */
    public static List<Criterion> between(final String operand, final Object value1, final Object value2) {

        // Initialize.
        List<Criterion> criterions = new ArrayList<Criterion>();
        
        // x BETWEEN y AND z is equivalent to x >= y AND x <= z
        criterions.add(ComparisonOperation.op(ComparisonOperator.GREATER_THAN_OR_EQUAL, operand, value1));
        criterions.add(LogicalOperation.and(ComparisonOperation.op(ComparisonOperator.LESS_THAN_OR_EQUAL, operand, value2)));
        
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
    public static Criterion eq(final String operand, final Object value) {
        return ComparisonOperation.op(ComparisonOperator.EQUAL, operand, value);
    }
              
    /**
     * Greater than or equal comparison operation.
     * 
     * @param  operand  the operand.
     * @param  value    the value.
     * 
     * @return  the resulting criterion.
     */
    public static Criterion ge(final String operand, final Object value) {
        return ComparisonOperation.op(ComparisonOperator.GREATER_THAN_OR_EQUAL, operand, value);
    }
      
    /**
     * Greater than comparison operation.
     * 
     * @param  operand  the operand.
     * @param  value    the value.
     * 
     * @return  the resulting criterion.
     */
    public static Criterion gt(final String operand, final Object value) {
        return ComparisonOperation.op(ComparisonOperator.GREATER_THAN, operand, value);
    }
      
    /**
     * Is empty comparison operation.
     * 
     * @param  operand  the operand.
     * 
     * @return  the resulting criterion.
     */
    public static Criterion isEmpty(final String operand) {
        return ComparisonOperation.op(ComparisonOperator.IS_EMPTY, operand);
    }
               
    /**
     * Is not empty comparison operation.
     * 
     * @param  operand  the operand.
     * 
     * @return  the resulting criterion.
     */
    public static Criterion isNotEmpty(final String operand) {
        return ComparisonOperation.op(ComparisonOperator.IS_NOT_EMPTY, operand);
    }
    
    /**
     * Is not null comparison operation.
     * 
     * @param  operand  the operand.
     * 
     * @return  the resulting criterion.
     */
    public static Criterion isNotNull(final String operand) {
        return ComparisonOperation.op(ComparisonOperator.IS_NOT_NULL, operand);
    }
    
    /**
     * Is null comparison operation.
     * 
     * @param  operand  the operand.
     * 
     * @return  the resulting criterion.
     */
    public static Criterion isNull(final String operand) {
        return ComparisonOperation.op(ComparisonOperator.IS_NULL, operand);
    }
    
    /**
     * Less than or equal comparison operation.
     * 
     * @param  operand  the operand.
     * @param  value    the value.
     * 
     * @return  the resulting criterion.
     */
    public static Criterion le(final String operand, final Object value) {
        return ComparisonOperation.op(ComparisonOperator.LESS_THAN_OR_EQUAL, operand, value);
    }
      
    /**
     * Like comparison operation.
     * 
     * @param  operand  the operand.
     * @param  value    the value.
     * 
     * @return  the resulting criterion.
     */
    public static Criterion like(final String operand, final Object value) {
        return ComparisonOperation.op(ComparisonOperator.LIKE, operand, value);
    }
          
    /**
     * Less than comparison operation.
     * 
     * @param  operand  the operand.
     * @param  value    the value.
     * 
     * @return  the resulting criterion.
     */
    public static Criterion lt(final String operand, final Object value) {
        return ComparisonOperation.op(ComparisonOperator.LESS_THAN, operand, value);
    }
     
    /**
     * Member of comparison operation.
     * 
     * @param operand  the operand.
     * @param value    the value.
     * 
     * @return  the resulting criterion.
     */
    public static Criterion memberOf(final String operand, final Object value) {
        return ComparisonOperation.op(ComparisonOperator.MEMBER_OF, operand, value);
    }
    
    /**
     * Not equal comparison operation.
     * 
     * @param  operand  the operand.
     * @param  value    the value.
     * 
     * @return  the resulting criterion.
     */
    public static Criterion ne(final String operand, final Object value) {
        return ComparisonOperation.op(ComparisonOperator.NOT_EQUAL, operand, value);
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
    public static List<Criterion> notBetween(final String operand, final Object value1, final Object value2) {

        // Initialize.
        List<Criterion> criterions = new ArrayList<Criterion>();
        
        // x NOT BETWEEN y AND z is equivalent to x < y AND x > z
        criterions.add(ComparisonOperation.op(ComparisonOperator.LESS_THAN, operand, value1));
        criterions.add(LogicalOperation.and(ComparisonOperation.op(ComparisonOperator.GREATER_THAN, operand, value2)));
        
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
    public static Criterion notLike(final String operand, final Object value) {
        return ComparisonOperation.op(ComparisonOperator.NOT_LIKE, operand, value);
    }
          
    /**
     * Not member of comparison operation.
     * 
     * @param operand  the operand.
     * @param value    the value.
     * 
     * @return  the resulting criterion.
     */
    public static Criterion notMemberOf(final String operand, final Object value) {
        return ComparisonOperation.op(ComparisonOperator.NOT_MEMBER_OF, operand, value);
    }
       
    /**
     * Comparison operation.
     * 
     * @param  comparisonOperator  the comparison operator.
     * @param  operand             the operand.
     * 
     * @return  the resulting criterion.
     */
    private static Criterion op(final ComparisonOperator comparisonOperator, final String operand) {

        // Set the criterion.
        Criterion criterion = new Criterion();
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
    private static Criterion op(final ComparisonOperator comparisonOperator, final String operand, final Object value) {
 
        // Set the criterion.
        Criterion criterion = new Criterion();
        criterion.setComparisonOperator(comparisonOperator);
        criterion.setOperand(operand);
        criterion.setValue(value);
        
        return criterion;
    }
}
