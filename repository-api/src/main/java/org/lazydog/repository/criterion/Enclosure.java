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
     * @return  the resulting criterion.
     */
    public static Criterion begin() {
        return Enclosure.operation(Enclosure.Operator.BEGIN);
    }
        
    /**
     * End enclosure operation.
     * 
     * @return  the resulting criterion.
     */
    public static Criterion end() {
        return Enclosure.operation(Enclosure.Operator.END);
    }
    
    /**
     * Enclosure operation.
     * 
     * @param  enclosureOperator  the enclosure operator.
     * 
     * @return  the resulting criterion.
     */
    private static Criterion operation(final Enclosure.Operator enclosureOperator) {
        
        // Set the criterion.
        Criterion criterion = Criterion.newInstance();
        criterion.setEnclosureOperator(enclosureOperator);

        return criterion;
    }
}
