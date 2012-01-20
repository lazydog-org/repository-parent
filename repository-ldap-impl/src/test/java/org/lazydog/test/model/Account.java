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
    private String canonicalName;
    private String displayName;
    private String lastName;
    private Account manager;
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
            String thatCanonicalName = replaceNull(that.getCanonicalName(), "");
            String thatDisplayName = replaceNull(that.getDisplayName(), "");
            String thatId = replaceNull(that.getId(), "");
            String thatLastName = replaceNull(that.getLastName(), "");
            String thatName = replaceNull(that.getName(), "");
            String thisCanonicalName = replaceNull(this.getCanonicalName(), "");
            String thisDisplayName = replaceNull(this.getDisplayName(), "");
            String thisId = replaceNull(this.getId(), "");
            String thisLastName = replaceNull(this.getLastName(), "");
            String thisName = replaceNull(this.getName(), "");
            
            // Compare this object to that object.
            equals = thisId.equals(thatId);
            equals = (equals == false) ? equals : thisName.equals(thatName);
            equals = (equals == false) ? equals : thisCanonicalName.equals(thatCanonicalName);
            equals = (equals == false) ? equals : thisLastName.equals(thatLastName);
            equals = (equals == false) ? equals : thisDisplayName.equals(thatDisplayName);
        }

        return equals;
    }
    
    /**
     * Get the canonical name.
     * 
     * @return  the canonical name.
     */
    public String getCanonicalName() {
        return this.canonicalName;
    }

    /**
     * Get the display name.
     * 
     * @return  the display name.
     */
    public String getDisplayName() {
        return this.displayName;
    }

    /**
     * Get the last name.
     * 
     * @return  the last name.
     */
    public String getLastName() {
        return this.lastName;
    }

    /**
     * Get the manager.
     * 
     * @return  the manager.
     */
    public Account getManager() {
        return this.manager;
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
    	String thisCanonicalName = replaceNull(this.getCanonicalName(), "");
        String thisDisplayName = replaceNull(this.getDisplayName(), "");
        String thisId = replaceNull(this.getId(), "");
        String thisLastName = replaceNull(this.getLastName(), "");
        String thisName = replaceNull(this.getName(), "");
        
        return thisId.hashCode()*7^4
             + thisName.hashCode()*7^3
             + thisCanonicalName.hashCode()*7^2
             + thisLastName.hashCode()*7
             + thisDisplayName.hashCode();
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
     * Set the canonical name.
     * 
     * @param  canonicalName  the canonical name.
     */
    public void setCanonicalName(final String canonicalName) {
        this.canonicalName = canonicalName;
    }
 
    /**
     * Set the display name.
     * 
     * @param  displayName  the display name.
     */
    public void setDisplayName(final String displayName) {
        this.displayName = displayName;
    }

    /**
     * Set the last name.
     * 
     * @param  lastName  the last name.
     */
    public void setLastName(final String lastName) {
        this.lastName = lastName;
    }
	
    /**
     * Set the manager.
     * 
     * @param  manager  the manager.
     */
    public void setManager(final Account manager) {
        this.manager = manager;
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
        return new StringBuffer()
        	.append("Account [")
        	.append("canonicalName = ").append(this.getCanonicalName())
        	.append(", displayName = ").append(this.getDisplayName())
        	.append(", id = ").append(this.getId())
        	.append(", lastName = ").append(this.getLastName())
        	.append(", manager = ").append(this.getManager())
        	.append(", name = ").append(this.getName())
        	.append("]")
        	.toString();
    }
}
