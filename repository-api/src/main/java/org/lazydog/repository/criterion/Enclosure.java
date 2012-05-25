package org.lazydog.repository.criterion;


/**
 * Enclosure.
 * 
 * @author rrickard
 */
public final class Enclosure {

    /**
     * Enclosure operator.
     */
    public enum Operator {
        BEGIN,
        END,
        UNDEFINED;
    };
    
    /**
     * Hide the constructor.
     */
    private Enclosure() {    
    }
    
    /**
     * Begin enclosure operation.
     * 
     * @param  criterion  the criterion.
     * 
     * @return  the resulting criterion.
     */
    public static Criterion begin(final Criterion criterion) {
        return Enclosure.operation(Enclosure.Operator.BEGIN, criterion);
    }
        
    /**
     * End enclosure operation.
     * 
     * @param  criterion  the criterion.
     * 
     * @return  the resulting criterion.
     */
    public static Criterion end(final Criterion criterion) {
        return Enclosure.operation(Enclosure.Operator.END, criterion);
    }
    
    /**
     * Enclosure operation.
     * 
     * @param  enclosureOperator  the enclosure operator.
     * @param  criterion        the criterion.
     * 
     * @return  the resulting criterion.
     */
    private static Criterion operation(final Enclosure.Operator enclosureOperator, final Criterion criterion) {
        
        // Set the criterion enclosure operator.
        criterion.setEnclosureOperator(enclosureOperator);

        return criterion;
    }
}
