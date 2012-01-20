package org.lazydog.repository.criterion;


/**
 * Order.
 * 
 * @author  Ron Rickard
 */
public final class Order {

    /**
     * Private constructor.
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
        return Order.order(OrderDirection.ASC, operand);
    }
    
    /**
     * Descending order.
     * 
     * @param  operand  the operand.
     * 
     * @return  the resulting criterion.
     */
    public static Criterion desc(final String operand) {
        return Order.order(OrderDirection.DESC, operand);
    }
           
    /**
     * Order.
     * 
     * @param  orderDirection  the order direction.
     * @param  operand         the operand.
     * 
     * @return  the resulting criterion.
     */
    private static Criterion order(final OrderDirection orderDirection, final String operand) {

        // Set the criterion.
        Criterion criterion = new Criterion();
        criterion.setOrderDirection(orderDirection);
        criterion.setOperand(operand);
        
        return criterion;
    }
}
