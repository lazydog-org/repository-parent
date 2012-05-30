package org.lazydog.repository.criterion;

import java.util.ArrayList;
import java.util.List;


/**
 * Comparison.
 * 
 * @author  Ron Rickard
 */
public final class Comparison {

    /**
     * Comparison operator.
     */
    public enum Operator {
        EQUAL,
        GREATER_THAN,
        GREATER_THAN_OR_EQUAL,
        IN,
        IS_EMPTY,
        IS_NOT_EMPTY,
        IS_NULL,
        IS_NOT_NULL,
        LESS_THAN,
        LESS_THAN_OR_EQUAL,
        LIKE,
        MEMBER_OF,
        NOT_EQUAL,
        NOT_IN,
        NOT_LIKE,
        NOT_MEMBER_OF,
        UNDEFINED;
    };
    
    /**
     * Hide the constructor.
     */
    private Comparison() {     
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
        criterions.add(Comparison.operation(Comparison.Operator.GREATER_THAN_OR_EQUAL, operand, value1));
        criterions.add(Logical.and(Comparison.operation(Comparison.Operator.LESS_THAN_OR_EQUAL, operand, value2)));
        
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
        return Comparison.operation(Comparison.Operator.EQUAL, operand, value);
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
        return Comparison.operation(Comparison.Operator.GREATER_THAN_OR_EQUAL, operand, value);
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
        return Comparison.operation(Comparison.Operator.GREATER_THAN, operand, value);
    }
      
    /**
     * Is empty comparison operation.
     * 
     * @param  operand  the operand.
     * 
     * @return  the resulting criterion.
     */
    public static Criterion isEmpty(final String operand) {
        return Comparison.operation(Comparison.Operator.IS_EMPTY, operand);
    }
               
    /**
     * Is not empty comparison operation.
     * 
     * @param  operand  the operand.
     * 
     * @return  the resulting criterion.
     */
    public static Criterion isNotEmpty(final String operand) {
        return Comparison.operation(Comparison.Operator.IS_NOT_EMPTY, operand);
    }
    
    /**
     * Is not null comparison operation.
     * 
     * @param  operand  the operand.
     * 
     * @return  the resulting criterion.
     */
    public static Criterion isNotNull(final String operand) {
        return Comparison.operation(Comparison.Operator.IS_NOT_NULL, operand);
    }
    
    /**
     * Is null comparison operation.
     * 
     * @param  operand  the operand.
     * 
     * @return  the resulting criterion.
     */
    public static Criterion isNull(final String operand) {
        return Comparison.operation(Comparison.Operator.IS_NULL, operand);
    }
    
    /**
     * In comparison operation.
     * 
     * @param  operand  the operand.
     * @param  values   the values.
     * 
     * @return  the resulting criterions.
     */
    public static List<Criterion> in(final String operand, final Object... values) {
        
        // Initialize.
        List<Criterion> criterions = new ArrayList<Criterion>();
        
        // Check if there is exactly one value.
        if (values.length == 1) {
            criterions.add(Comparison.eq(operand, values[0]));
        }
        
        // Check if there is more than one value.
        if (values.length > 1) {
            
            criterions.add(Enclosure.begin());
            boolean firstValue = true;
            
            // Loop through the values.
            for (Object value : values) {
                
                // Check if this is the first value.
                if (firstValue) {
                    criterions.add(Comparison.eq(operand, value));
                    firstValue = false;
                }
                else {
                    criterions.add(Logical.or(Comparison.eq(operand, value)));
                }
            }
            
            criterions.add(Enclosure.end());
        }
        
        return criterions;
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
        return Comparison.operation(Comparison.Operator.LESS_THAN_OR_EQUAL, operand, value);
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
        return Comparison.operation(Comparison.Operator.LIKE, operand, value);
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
        return Comparison.operation(Comparison.Operator.LESS_THAN, operand, value);
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
        return Comparison.operation(Comparison.Operator.MEMBER_OF, operand, value);
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
        return Comparison.operation(Comparison.Operator.NOT_EQUAL, operand, value);
    }
                
    /**
     * Not between comparison operation.
     * 
     * @param  operand  the operand.
     * @param  value1   the first value.
     * @param  value2   the second value.
     * 
     * @return  the resulting criterions.
     */
    public static List<Criterion> notBetween(final String operand, final Object value1, final Object value2) {

        // Initialize.
        List<Criterion> criterions = new ArrayList<Criterion>();
        
        // x NOT BETWEEN y AND z is equivalent to x < y OR x > z
        criterions.add(Comparison.operation(Comparison.Operator.LESS_THAN, operand, value1));
        criterions.add(Logical.or(Comparison.operation(Comparison.Operator.GREATER_THAN, operand, value2)));
        
        return criterions;
    }
     
    /**
     * Not in comparison operation.
     * 
     * @param  operand  the operand.
     * @param  values   the values.
     * 
     * @return  the resulting criterions.
     */
    public static List<Criterion> notIn(final String operand, final Object... values) {
        
        // Initialize.
        List<Criterion> criterions = new ArrayList<Criterion>();
        
        // Check if there is exactly one value.
        if (values.length == 1) {
            criterions.add(Comparison.ne(operand, values[0]));
        }
        
        // Check if there is more than one value.
        if (values.length > 1) {
            
            criterions.add(Enclosure.begin());
            boolean firstValue = true;
            
            // Loop through the values.
            for (Object value : values) {
                
                // Check if this is the first value.
                if (firstValue) {
                    criterions.add(Comparison.ne(operand, value));
                    firstValue = false;
                }
                else {
                    criterions.add(Logical.and(Comparison.ne(operand, value)));
                }
            }
            
            criterions.add(Enclosure.end());
        }
        
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
        return Comparison.operation(Comparison.Operator.NOT_LIKE, operand, value);
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
        return Comparison.operation(Comparison.Operator.NOT_MEMBER_OF, operand, value);
    }
       
    /**
     * Comparison operation.
     * 
     * @param  comparisonOperator  the comparison operator.
     * @param  operand             the operand.
     * 
     * @return  the resulting criterion.
     */
    private static Criterion operation(final Comparison.Operator comparisonOperator, final String operand) {
        return Comparison.operation(comparisonOperator, operand, null);
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
    private static Criterion operation(final Comparison.Operator comparisonOperator, final String operand, final Object value) {
 
        // Set the criterion.
        Criterion criterion = Criterion.newInstance();
        criterion.setComparisonOperator(comparisonOperator);
        criterion.setOperand(operand);
        criterion.setValue(value);
        
        return criterion;
    }
}
