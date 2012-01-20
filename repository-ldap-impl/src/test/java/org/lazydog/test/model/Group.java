package org.lazydog.test.model;

import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;
import org.lazydog.repository.Entity;


/**
 * Group.
 * 
 * @author  Ron Rickard
 */
public class Group extends Entity<Group,String> implements Comparable<Group>,Serializable {

    private static final long serialVersionUID = 1L;
    private Set<Account> accounts = new HashSet<Account>();
    private String description;
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
    public int compareTo(final Group that) {

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
        if (object instanceof Group) {

            // Get the property values in this and that object, giving them a value if the
            // value is null.
            Group that = (Group)object;
            Set<Account> thatAccounts = that.getAccounts();
            String thatDescription = replaceNull(that.getDescription(), "");
            String thatId = replaceNull(that.getId(), "");
            String thatName = replaceNull(that.getName(), "");
            Set<Account> thisAccounts = this.getAccounts();
            String thisDescription = replaceNull(this.getDescription(), "");
            String thisId = replaceNull(this.getId(), "");
            String thisName = replaceNull(this.getName(), "");
            
            // Compare this object to that object.
            equals = thisId.equals(thatId);
            equals = (equals == false) ? equals : thisName.equals(thatName);
            equals = (equals == false) ? equals : thisDescription.equals(thatDescription);
            equals = (equals == false) ? equals : thisAccounts.equals(thatAccounts);
        }

        return equals;
    }
    
    /**
     * Get the accounts.
     * 
     * @return  the accounts.
     */
    public Set<Account> getAccounts() {
        return this.accounts;
    }
	
    /**
     * Get the description.
     * 
     * @return  the description.
     */
    public String getDescription() {
        return this.description;
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
        Set<Account> thisAccounts = this.getAccounts();
        String thisDescription = replaceNull(this.getDescription(), "");
        String thisId = replaceNull(this.getId(), "");
        String thisName = replaceNull(this.getName(), "");
        
        return thisId.hashCode()*7^3
             + thisName.hashCode()*7^2
             + thisDescription.hashCode()*7
             + thisAccounts.hashCode();
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
     * Set the accounts.
     * 
     * @param  accounts  the accounts.
     */ 
    public void setAccounts(final Set<Account> accounts) {
        this.accounts = replaceNull(accounts, new HashSet<Account>());
    }

    /**
     * Set the description.
     * 
     * @param  description  the description.
     */
    public void setDescription(final String description) {
        this.description = description;
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
        	.append("Group [")
        	.append("accounts = ").append(this.getAccounts())
        	.append(", description = ").append(this.getDescription())
        	.append(", id = ").append(this.getId())
        	.append(", name = ").append(this.getName())
        	.append("]")
        	.toString();
    }
}
