package org.lazydog.repository.criterion;

/**
 * Logical operation.
 * 
 * @author rrickard
 */
public final class EnclosureOperation {

    /**
     * Private constructor.
     */
    private EnclosureOperation() {    
    }
    
    /**
     * Begin enclosure operation.
     * 
     * @param  criterion  the criterion.
     * 
     * @return  the resulting criterion.
     */
    public static Criterion begin(final Criterion criterion) {
        return EnclosureOperation.op(EnclosureOperator.BEGIN, criterion);
    }
        
    /**
     * End enclosure operation.
     * 
     * @param  criterion  the criterion.
     * 
     * @return  the resulting criterion.
     */
    public static Criterion end(final Criterion criterion) {
        return EnclosureOperation.op(EnclosureOperator.END, criterion);
    }
    
    /**
     * Enclosure operation.
     * 
     * @param  enclosureOperator  the enclosure operator.
     * @param  criterion        the criterion.
     * 
     * @return  the resulting criterion.
     */
    private static Criterion op(final EnclosureOperator enclosureOperator,
                                final Criterion criterion) {
        
        // Set the criterion enclosure operator.
        criterion.setEnclosureOperator(enclosureOperator);

        return criterion;
    }
}
