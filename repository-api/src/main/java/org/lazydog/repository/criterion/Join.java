package org.lazydog.repository.criterion;


/**
 * Join.
 * 
 * @author  Ron Rickard
 */
public final class Join {

    /**
     * Join operator.
     */
    public enum Operator {
        JOIN,
        JOIN_FETCH,
        LEFT_JOIN,
        LEFT_JOIN_FETCH,
        UNDEFINED;
    };
    
    /**
     * Hide the constructor.
     */
    private Join() {    
    }
       
    /**
     *Join operation.
     * 
     * @param  operand  the operand.
     * 
     * @return  the resulting criterion.
     */
    public static Criterion join(final String operand) {
        return Join.operation(Join.Operator.JOIN, operand);
    }
       
    /**
     *Join fetch operation.
     * 
     * @param  operand  the operand.
     * 
     * @return  the resulting criterion.
     */
    public static Criterion joinFetch(final String operand) {
        return Join.operation(Join.Operator.JOIN_FETCH, operand);
    }
           
    /**
     * Left join operation.
     * 
     * @param  operand  the operand.
     * 
     * @return  the resulting criterion.
     */
    public static Criterion leftJoin(final String operand) {
        return Join.operation(Join.Operator.LEFT_JOIN, operand);
    }
               
    /**
     * Left join fetch operation.
     * 
     * @param  operand  the operand.
     * 
     * @return  the resulting criterion.
     */
    public static Criterion leftJoinFetch(final String operand) {
        return Join.operation(Join.Operator.LEFT_JOIN_FETCH, operand);
    }
    
    /**
     * Join operation.
     * 
     * @param  joinOperator  the join operator.
     * @param  operand       the operand.
     * 
     * @return  the resulting criterion.
     */
    private static Criterion operation(final Join.Operator joinOperator, final String operand) {
        
        // Set the criterion.
        Criterion criterion = new Criterion();
        criterion.setJoinOperator(joinOperator);
        criterion.setOperand(operand);
        
        return criterion;
    }
}
