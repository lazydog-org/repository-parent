package org.lazydog.repository.criterion;


/**
 * Logical.
 * 
 * @author  Ron Rickard
 */
public final class Logical {

    /**
     * Logical operator.
     */
    public enum Operator {
        AND,
        OR,
        UNDEFINED;
    };

    /**
     * Hide the constructor.
     */
    private Logical() {    
    }
    
    /**
     * And logical operation.
     * 
     * @param  criterion  the criterion.
     * 
     * @return  the resulting criterion.
     */
    public static Criterion and(final Criterion criterion) {
        return Logical.operation(Logical.Operator.AND, criterion);
    }
    
    /**
     * Logical operation.
     * 
     * @param  logicalOperator  the logical operator.
     * @param  criterion        the criterion.
     * 
     * @return  the resulting criterion.
     */
    private static Criterion operation(final Logical.Operator logicalOperator,
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
        return Logical.operation(Logical.Operator.OR, criterion);
    }
}
