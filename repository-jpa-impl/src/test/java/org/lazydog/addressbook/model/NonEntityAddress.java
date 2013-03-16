/**
 * Copyright 2010-2013 lazydog.org.
 *
 * This file is part of repository.
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
package org.lazydog.addressbook.model;

import java.io.Serializable;
import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.apache.commons.lang3.builder.ToStringBuilder;

/**
 * Non-entity address.
 *
 * @author  Ron Rickard
 */
public class NonEntityAddress implements Serializable {

    private static final long serialVersionUID = 1L;
    private String city;
    private Integer id;
    private String state;
    private String streetAddress;
    private String zipcode;
    
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
        return HashCodeBuilder.reflectionHashCode(this);
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
        return ToStringBuilder.reflectionToString(this);
    }
}
