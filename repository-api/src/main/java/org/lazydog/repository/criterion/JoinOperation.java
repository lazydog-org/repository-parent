package org.lazydog.repository.criterion;


/**
 * Join operation.
 * 
 * @author  Ron Rickard
 */
public final class JoinOperation {
 
    /**
     * Private constructor.
     */
    private JoinOperation() {    
    }
    
    /**
     * Join operation.
     * 
     * @param  operand  the operand.
     * 
     * @return  the resulting criterion.
     */
    public static Criterion join(final String operand) {
        return JoinOperation.op(JoinOperator.JOIN, operand);
    }

    /**
     * Join fetch operation.
     *
     * @param  operand  the operand.
     *
     * @return  the resulting criterion.
     */
    public static Criterion joinFetch(final String operand) {
        return JoinOperation.op(JoinOperator.JOIN_FETCH, operand);
    }

    /**
     * Left join operation.
     *
     * @param  operand  the operand.
     *
     * @return  the resulting criterion.
     */
    public static Criterion leftJoin(final String operand) {
        return JoinOperation.op(JoinOperator.LEFT_JOIN, operand);
    }

    /**
     * Left join fetch operation.
     *
     * @param  operand  the operand.
     *
     * @return  the resulting criterion.
     */
    public static Criterion leftJoinFetch(final String operand) {
        return JoinOperation.op(JoinOperator.LEFT_JOIN_FETCH, operand);
    }

    /**
     * Join operation.
     * 
     * @param  joinOperator  the join operator.
     * @param  operand       the operand.
     * 
     * @return  the resulting criterion.
     */
    private static Criterion op(final JoinOperator joinOperator, final String operand) {

        // Set the criterion.
        Criterion criterion = new Criterion();
        criterion.setJoinOperator(joinOperator);
        criterion.setOperand(operand);
        
        return criterion;
    }
}
