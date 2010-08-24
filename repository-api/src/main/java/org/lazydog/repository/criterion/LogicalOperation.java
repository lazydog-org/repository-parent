package org.lazydog.repository.criterion;


/**
 * Logical operation.
 * 
 * @author  Ron Rickard
 */
public class LogicalOperation {

    /**
     * And logical operation.
     * 
     * @param  criterion  the criterion.
     * 
     * @return  the resulting criterion.
     */
    public static Criterion and(Criterion criterion) {
        return LogicalOperation.op(LogicalOperator.AND, criterion);
    }
    
    /**
     * Logical operation.
     * 
     * @param  logicalOperator  the logical operator.
     * @param  criterion        the criterion.
     * 
     * @return  the resulting criterion.
     */
    private static Criterion op(LogicalOperator logicalOperator,
                                Criterion criterion) {
        
        // Set the criterion logical operator.
        criterion.setLogicalOperator(logicalOperator);

        return criterion;
    }
    
    /**
     * Or logical operation.
     * 
     * @param  criterion  the criterion.
     * 
     * @return  the resulting criterion.
     */
    public static Criterion or(Criterion criterion) {
        return LogicalOperation.op(LogicalOperator.OR, criterion);
    }
}
