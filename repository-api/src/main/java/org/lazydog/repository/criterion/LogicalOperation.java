package org.lazydog.repository.criterion;


/**
 * Logical operation.
 * 
 * @author  Ron Rickard
 */
public final class LogicalOperation {

    /**
     * Private constructor.
     */
    private LogicalOperation() {    
    }
    
    /**
     * And logical operation.
     * 
     * @param  criterion  the criterion.
     * 
     * @return  the resulting criterion.
     */
    public static Criterion and(final Criterion criterion) {
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
    private static Criterion op(final LogicalOperator logicalOperator,
                                final Criterion criterion) {
        
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
    public static Criterion or(final Criterion criterion) {
        return LogicalOperation.op(LogicalOperator.OR, criterion);
    }
}
