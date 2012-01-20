package org.lazydog.addressbook.model;

import java.io.Serializable;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;


/**
 * Address 2.
 *
 * @author  Ron Rickard
 */
@Entity
public class Address2 implements Comparable<Address2>, Serializable {

    private static final long serialVersionUID = 1L;
    @Column(nullable=false)
    private String city;
    @Id
    @GeneratedValue(strategy=GenerationType.IDENTITY)
    private Integer id;
    @Column(name="STATE", nullable=false)
    private String state;
    @Column(name="STREET_ADDRESS", nullable=false)
    private String streetAddress;
    @Column(nullable=false)
    private String zipcode;

    /**
     * Compare this object to the specified object.
     *
     * @param  that  the object to compare this object against.
     *
     * @return  the value 0 if this object is equal to the object;
     *          a value less than 0 if this object is less than the object;
     *          and a value greater than 0 if this object is greater than the
     *          object.
     */
    @Override
    public int compareTo(final Address2 that) {

        // Declare.
        int lastCompare;
        String thatCity;
        String thatState;
        String thatStreetAddress;
        String thatZipcode;
        String thisCity;
        String thisState;
        String thisStreetAddress;
        String thisZipcode;

        // Initialize.
        lastCompare = 0;
        thatCity = replaceNull(that.getCity(), "");
        thatState = replaceNull(that.getState(), "");
        thatStreetAddress = replaceNull(that.getStreetAddress(), "");
        thatZipcode = replaceNull(that.getZipcode(), "");
        thisCity = replaceNull(this.getCity(), "");
        thisState = replaceNull(this.getState(), "");
        thisStreetAddress = replaceNull(this.getStreetAddress(), "");
        thisZipcode = replaceNull(this.getZipcode(), "");

        // Compare this object to the object.
        lastCompare = thisState.compareTo(thatState);
        lastCompare = (lastCompare != 0) ? lastCompare : thisCity.compareTo(thatCity);
        lastCompare = (lastCompare != 0) ? lastCompare : thisZipcode.compareTo(thatZipcode);
        lastCompare = (lastCompare != 0) ? lastCompare : thisStreetAddress.compareTo(thatStreetAddress);

        return lastCompare;
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

        // Declare.
        boolean equals;

        // Initialize.
        equals = false;

        // Check if the object is an instance of this class
        // and is equal to this object.
        if (object instanceof Address2 &&
            this.compareTo((Address2)object) == 0) {
            equals = true;
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
     * Returns a hash code for this object.
     *
     * @return  a hash code for this object.
     */
    @Override
    public int hashCode() {

        // Declare.
        String thisCity;
        String thisState;
        String thisStreetAddress;
        String thisZipcode;

        // Initialize.
        thisCity = replaceNull(this.getCity(), "");
        thisState = replaceNull(this.getState(), "");
        thisStreetAddress = replaceNull(this.getStreetAddress(), "");
        thisZipcode = replaceNull(this.getZipcode(), "");

        return thisState.hashCode()*7^3
             + thisCity.hashCode()*7^2
             + thisZipcode.hashCode()*7
             + thisStreetAddress.hashCode();
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

        StringBuffer buffer;

        buffer = new StringBuffer();
        buffer.append("Address2 = [");
        buffer.append("city = ").append(this.getCity());
        buffer.append(", id = ").append(this.getId());
        buffer.append(", state = ").append(this.getState());
        buffer.append(", streetAddress = ").append(this.getStreetAddress());
        buffer.append(", zipcode = ").append(this.getZipcode());
        buffer.append("]");

        return buffer.toString();
    }
}
