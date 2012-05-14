package org.lazydog.addressbook.model;

import java.io.Serializable;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;


/**
 * Address.
 *
 * @author  Ron Rickard
 */
@Entity
public class Phone implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy=GenerationType.IDENTITY)
    private Integer id;
    @Column(nullable=false)
    private String number;

    /**
     * Indicates whether some other object is "equal to" this one.
     * 
     * @param  object  the reference object with which to compare.
     * 
     * @return  true if this object is the same as the object argument; false otherwise.
     */
    @Override
    public boolean equals(Object object) {
 
        // Assume the reference object is not equal to this object.
        boolean equals = false;
        
        // Check if the object is an instance of this class.
        if (object instanceof Phone) {
            Phone that = (Phone)object;
            
            // Check if the IDs are both null or they are both equal.
            if ((this.id == null && that.id == null) || (this.id != null && this.id.equals(that.id))) {
               
                // The reference object is the same as this object.
                equals = true;
            }
        }
        
        return equals;
    }

    /**
     * Get the ID.
     *
     * @return  the ID.
     */
    public Integer getId() {
        return this.id;
    }

    /**
     * Get the number.
     *
     * @return  the number.
     */
    public String getNumber() {
        return this.number;
    }

    /**
     * Returns a hash code value for this object.
     * 
     * @return  a hash code value for this object.
     */
    @Override
    public int hashCode() {
        return (id != null ? id.hashCode() : 0);
    }

    /**
     * Set the ID.
     *
     * @param  id  the ID.
     */
    public void setId(final Integer id) {
        this.id = id;
    }

    /**
     * Set the number.
     *
     * @param  number  the number.
     */
    public void setNumber(final String number) {
        this.number = number;
    }

    /**
     * Get this object as a string.
     *
     * @return  this object as a string.
     */
    @Override
    public String toString() {

        StringBuilder stringBuilder = new StringBuilder();
        
        stringBuilder.append("Address = [");
        stringBuilder.append("id = ").append(this.id);
        stringBuilder.append(", number = ").append(this.number);
        stringBuilder.append("]");

        return stringBuilder.toString();
    }
}
