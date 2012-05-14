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
public class Address implements Serializable {

    private static final long serialVersionUID = 1L;
    @Column(nullable=false)
    private String city;
    @Id
    @GeneratedValue(strategy=GenerationType.IDENTITY)
    private Integer id;
    @Column(nullable=false)
    private String state;
    @Column(name="STREET_ADDRESS", nullable=false)
    private String streetAddress;
    @Column(nullable=false)
    private String zipcode;

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
        if (object instanceof Address) {
            Address that = (Address)object;
            
            // Check if the IDs are both null or they are both equal.
            if ((this.id == null && that.id == null) || (this.id != null && this.id.equals(that.id))) {
               
                // The reference object is the same as this object.
                equals = true;
            }
        }
        
        return equals;
    }

    /**
     * Get the city.
     *
     * @return  the city.
     */
    public String getCity() {
        return this.city;
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
     * Get the state.
     *
     * @return  the state.
     */
    public String getState() {
        return this.state;
    }

    /**
     * Get the street address.
     *
     * @return  the street address.
     */
    public String getStreetAddress() {
        return this.streetAddress;
    }

    /**
     * Get the zipcode.
     *
     * @return  the zipcode.
     */
    public String getZipcode() {
        return this.zipcode;
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
     * Set the city.
     *
     * @param  city  the city.
     */
    public void setCity(final String city) {
        this.city = city;
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
     * Set the state.
     *
     * @param  state  the state.
     */
    public void setState(final String state) {
        this.state = state;
    }

    /**
     * Set the street address.
     *
     * @param  streetAddress  the street address.
     */
    public void setStreetAddress(final String streetAddress) {
        this.streetAddress = streetAddress;
    }

    /**
     * Set the zipcode.
     *
     * @param  zipcode  the zipcode.
     */
    public void setZipcode(final String zipcode) {
        this.zipcode = zipcode;
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
        stringBuilder.append("city = ").append(this.city);
        stringBuilder.append(", id = ").append(this.id);
        stringBuilder.append(", state = ").append(this.state);
        stringBuilder.append(", streetAddress = ").append(this.streetAddress);
        stringBuilder.append(", zipcode = ").append(this.zipcode);
        stringBuilder.append("]");

        return stringBuilder.toString();
    }
}
