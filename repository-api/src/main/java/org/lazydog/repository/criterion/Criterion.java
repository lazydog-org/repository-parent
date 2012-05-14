package org.lazydog.repository.criterion;

import java.io.Serializable;


/**
 * A criterion.
 * 
 * @author  Ron Rickard
 */
public final class Criterion implements Serializable {

    private static final long serialVersionUID = 1L;
    
    private ComparisonOperator comparisonOperator;
    private EnclosureOperator enclosureOperator;
    private LogicalOperator logicalOperator;
    private String operand;
    private OrderDirection orderDirection;
    private Object value;
        
    /**
     * Get the comparison operator.
     * 
     * @return  the comparison operator.
     */
    public ComparisonOperator getComparisonOperator() {
        return this.comparisonOperator;
    }

    /**
     * Get the enclosure operator.
     * 
     * @return  the enclosure operator.
     */
    public EnclosureOperator getEnclosureOperator() {
        return this.enclosureOperator;
    }

    /**
     * Get the logical operator.
     * 
     * @return  the the logical operator.
     */
    public LogicalOperator getLogicalOperator() {
        return this.logicalOperator;
    }
    
    /**
     * Get the operand.
     * 
     * @return  the operand.
     */
    public String getOperand() {
        return this.operand;
    }
        
    /**
     * Get the order direction.
     * 
     * @return  the order direction.
     */
    public OrderDirection getOrderDirection() {
        return this.orderDirection;
    }
    
    /**
     * Get the value.
     * 
     * @return  the value.
     */
    public Object getValue() {
        return this.value;
    }
              
    /**
     * Set the comparison operator.
     * 
     * @param  comparisonOperator  the comparison operator.
     */
    public void setComparisonOperator(final ComparisonOperator comparisonOperator) {
        this.comparisonOperator = comparisonOperator;
    }

    /**
     * Set the enclosure operator.
     * 
     * @param  enclosureOperator  the enclosure operator.
     */
    public void setEnclosureOperator(final EnclosureOperator enclosureOperator) {
        this.enclosureOperator = enclosureOperator;
    }

    /**
     * Set the logical operator.
     * 
     * @param  logicalOperator  the logical operator.
     */
    public void setLogicalOperator(final LogicalOperator logicalOperator) {
        this.logicalOperator = logicalOperator;
    }
      
    /**
     * Set the operand.
     * 
     * @param  operand  the operand.
     */
    public void setOperand(final String operand) {
        this.operand = operand;
    }
          
    /**
     * Set the order direction.
     * 
     * @param  orderDirection  the order direction.
     */
    public void setOrderDirection(final OrderDirection orderDirection) {
        this.orderDirection = orderDirection;
    }
    
    /**
     * Set the value.
     * 
     * @param  value  the value.
     */
    public void setValue(final Object value) {
        this.value = value;
    }
}
