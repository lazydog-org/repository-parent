package org.lazydog.repository.criterion;


/**
 * Order.
 * 
 * @author  Ron Rickard
 */
public final class Order {

    /**
    * Order direction.
    */
    public enum Direction {
        ASC,
        DESC,
        UNDEFINED;
    };

    /**
     * Hide the constructor.
     */
    private Order() {    
    }
    
    /**
     * Ascending order.
     * 
     * @param  operand  the operand.
     * 
     * @return  the resulting criterion.
     */
    public static Criterion asc(final String operand) {
        return Order.order(Order.Direction.ASC, operand);
    }
    
    /**
     * Descending order.
     * 
     * @param  operand  the operand.
     * 
     * @return  the resulting criterion.
     */
    public static Criterion desc(final String operand) {
        return Order.order(Order.Direction.DESC, operand);
    }
           
    /**
     * Order.
     * 
     * @param  orderDirection  the order direction.
     * @param  operand         the operand.
     * 
     * @return  the resulting criterion.
     */
    private static Criterion order(final Order.Direction orderDirection, final String operand) {

        // Set the criterion.
        Criterion criterion = new Criterion();
        criterion.setOrderDirection(orderDirection);
        criterion.setOperand(operand);
        
        return criterion;
    }
}
