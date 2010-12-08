package org.lazydog.repository.criterion;

import java.util.ArrayList;
import java.util.List;


/**
 * Join operation.
 * 
 * @author  Ron Rickard
 */
public class JoinOperation {
 
    /**
     * Join operation.
     * 
     * @param  operand  the operand.
     * 
     * @return  the resulting criterion.
     */
    public static Criterion join(String operand) {
        return JoinOperation.op(
            JoinOperator.JOIN, operand);
    }

    /**
     * Join fetch operation.
     *
     * @param  operand  the operand.
     *
     * @return  the resulting criterion.
     */
    public static Criterion joinFetch(String operand) {
        return JoinOperation.op(
            JoinOperator.JOIN_FETCH, operand);
    }

    /**
     * Left join operation.
     *
     * @param  operand  the operand.
     *
     * @return  the resulting criterion.
     */
    public static Criterion leftJoin(String operand) {
        return JoinOperation.op(
            JoinOperator.LEFT_JOIN, operand);
    }

    /**
     * Left join fetch operation.
     *
     * @param  operand  the operand.
     *
     * @return  the resulting criterion.
     */
    public static Criterion leftJoinFetch(String operand) {
        return JoinOperation.op(
            JoinOperator.LEFT_JOIN_FETCH, operand);
    }

    /**
     * Join operation.
     * 
     * @param  joinOperator  the join operator.
     * @param  operand       the operand.
     * 
     * @return  the resulting criterion.
     */
    private static Criterion op(JoinOperator joinOperator,
                                String operand) {
        
        // Declare.
        Criterion criterion;

        // Set the criterion.
        criterion = new Criterion();
        criterion.setJoinOperator(joinOperator);
        criterion.setOperand(operand);
        
        return criterion;
    }
}
