package org.lazydog.test.model;

import java.io.Serializable;
import org.lazydog.repository.Entity;


/**
 * Account.
 * 
 * @author  Ron Rickard
 */
public class Account extends Entity<Account,String> implements Comparable<Account>,Serializable {

    private static final long serialVersionUID = 1L;
    private String name;

    /**
     * Compare this object to the specified object.
     * 
     * @param  that  the object to compare this object against.
     * 
     * @return  the value 0 if this object is equal to the object;
     * 		a value less than 0 if this object is less than the object;
     * 		and a value greater than 0 if this object is greater than the object.
     */
    @Override
    public int compareTo(final Account that) {

        // Initialize.
        String thatName = replaceNull(that.getName(), "");
        String thisName = replaceNull(this.getName(), "");

        return thisName.compareTo(thatName);
    }
	
    /**
     * Compare this object to the specified object.
     *
     * @param  object  the object to compare this object against.
     *
     * @return  true if the objects are equal; false otherwise.
     */
    @Override
    public boolean equals(final Object object) {

        // Initialize.
        boolean equals = false;
        
        // Check if the object is an instance of this class.
        if (object instanceof Account) {

            // Get the property values in this and that object, giving them a value if the
            // value is null.
            Account that = (Account)object;
            String thatId = replaceNull(that.getId(), "");
            String thatName = replaceNull(that.getName(), "");
            String thisId = replaceNull(this.getId(), "");
            String thisName = replaceNull(this.getName(), "");
            
            // Compare this object to that object.
            equals = thisId.equals(thatId);
            equals = (equals == false) ? equals : thisName.equals(thatName);
        }

        return equals;
    }

    /**
     * Get the name.
     * 
     * @return  the name.
     */
    public String getName() {
        return this.name;
    }

    /**
     * Returns a hash code for this object.
     *
     * @return  a hash code for this object.
     */
    @Override
    public int hashCode() {

        // Initialize.
        String thisId = replaceNull(this.getId(), "");
        String thisName = replaceNull(this.getName(), "");
        
        return thisId.hashCode()*7
             + thisName.hashCode();
    }

    /**
     * Replace the original object with the replacement object
     * if the original object is null.
     *
     * @param  original     the original object.
     * @param  replacement  the replacement object.
     *
     * @return  the original object if it is not null, otherwise the replacement
     *          object.
     *
     * @throws  IllegalArgumentException  if the replacement object is null.
     */
    private static <U, V extends U> U replaceNull(final U original, final V replacement) {

        // Check if the replacement object is null.
        if (replacement == null) {
            throw new IllegalArgumentException(
                    "The replacement object cannot be null.");
        }

        return (original == null) ? replacement : original;
    }

    /**
     * Set the name.
     * 
     * @param  name  the name.
     */
    public void setName(final String name) {
        this.name = name;
    }

    /**
     * Get this object as a String.
     *
     * @return  this object as a String.
     */
    @Override
    public String toString() {
        return new StringBuilder()
        	.append("Account [")
        	.append(", id = ").append(this.getId())
        	.append(", name = ").append(this.getName())
        	.append("]")
        	.toString();
    }
}
