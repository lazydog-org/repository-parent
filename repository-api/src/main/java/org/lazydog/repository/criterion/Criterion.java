package org.lazydog.repository.criterion;

import java.io.Serializable;
import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;


/**
 * A criterion.
 * 
 * @author  Ron Rickard
 */
public final class Criterion implements Serializable {

    private static final long serialVersionUID = 1L;
    
    private Comparison.Operator comparisonOperator = Comparison.Operator.UNDEFINED;
    private Enclosure.Operator enclosureOperator = Enclosure.Operator.UNDEFINED;
    private Logical.Operator logicalOperator = Logical.Operator.UNDEFINED;
    private String operand;
    private Order.Direction orderDirection = Order.Direction.UNDEFINED;
    private Object value;
    
     /**
     * Compare this object to the specified object.
     *
     * @param  object  the object to compare this object against.
     *
     * @return  true if the objects are equal; false otherwise.
     */
    @Override
    public boolean equals(Object object) {
        return EqualsBuilder.reflectionEquals(this, object);
    }
    
    /**
     * Get the comparison operator.
     * 
     * @return  the comparison operator.
     */
    public Comparison.Operator getComparisonOperator() {
        return this.comparisonOperator;
    }

    /**
     * Get the enclosure operator.
     * 
     * @return  the enclosure operator.
     */
    public Enclosure.Operator getEnclosureOperator() {
        return this.enclosureOperator;
    }

    /**
     * Get the logical operator.
     * 
     * @return  the the logical operator.
     */
    public Logical.Operator getLogicalOperator() {
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
    public Order.Direction getOrderDirection() {
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
     * Returns a hash code for this object.
     * 
     * @return  a hash code for this object.
     */
    @Override
    public int hashCode() {
        return HashCodeBuilder.reflectionHashCode(this);
    }
    
    /**
     * Replace the original object with the replacement object if the original object is null.
     *
     * @param  original     the original object.
     * @param  replacement  the replacement object.
     *
     * @return  the original object if it is not null, otherwise the replacement object.
     *
     * @throws  IllegalArgumentException  if the replacement object is null.
     */
    private static <U, V extends U> U replaceNull(U original, V replacement) {

        // Check if the replacement object is null.
        if (replacement == null) {
            throw new IllegalArgumentException(
                    "The replacement object cannot be null.");
        }

        return (original == null) ? replacement : original;
    }

    /**
     * Set the comparison operator.
     * 
     * @param  comparisonOperator  the comparison operator.
     */
    void setComparisonOperator(final Comparison.Operator comparisonOperator) {
        this.comparisonOperator = replaceNull(comparisonOperator, Comparison.Operator.UNDEFINED);
    }

    /**
     * Set the enclosure operator.
     * 
     * @param  enclosureOperator  the enclosure operator.
     */
    void setEnclosureOperator(final Enclosure.Operator enclosureOperator) {
        this.enclosureOperator = replaceNull(enclosureOperator, Enclosure.Operator.UNDEFINED);
    }

    /**
     * Set the logical operator.
     * 
     * @param  logicalOperator  the logical operator.
     */
    void setLogicalOperator(final Logical.Operator logicalOperator) {
        this.logicalOperator = replaceNull(logicalOperator, Logical.Operator.UNDEFINED);
    }
      
    /**
     * Set the operand.
     * 
     * @param  operand  the operand.
     */
    void setOperand(final String operand) {
        this.operand = operand;
    }
          
    /**
     * Set the order direction.
     * 
     * @param  orderDirection  the order direction.
     */
    void setOrderDirection(final Order.Direction orderDirection) {
        this.orderDirection = replaceNull(orderDirection, Order.Direction.UNDEFINED);
    }
    
    /**
     * Set the value.
     * 
     * @param  value  the value.
     */
    void setValue(final Object value) {
        this.value = value;
    }

    /**
     * Get this object as a String.
     *
     * @return  this object as a String.
     */
    @Override
    public String toString() {
            
        // Declare.
        StringBuilder toString;
        
        // Initialize.
        toString = new StringBuilder();
        
        toString.append("Criterion [");
        toString.append("operand = ").append(this.operand);
        toString.append(", value = ").append(this.value);
        toString.append(", comparisonOperator = ").append(this.comparisonOperator);
        toString.append(", enclosureOperator = ").append(this.enclosureOperator);
        toString.append(", logicalOperator = ").append(this.logicalOperator);
        toString.append(", orderDirection = ").append(this.orderDirection);
        toString.append("]");
        
        return toString.toString();
    }
}
