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
import java.util.ArrayList;
import java.util.List;
import javax.persistence.*;
import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.apache.commons.lang3.builder.ToStringBuilder;

/**
 * Person.
 *
 * @author  Ron Rickard
 */
@Entity
public class Employee implements Serializable {

    private static final long serialVersionUID = 1L;
    @OneToMany(fetch=FetchType.LAZY)
    @JoinTable(name="EMPLOYEE_ADDRESS", joinColumns=@JoinColumn(name="EMPLOYEE_ID"), inverseJoinColumns=@JoinColumn(name="ADDRESS_ID"))
    private List<Address> addresses = new ArrayList<Address>();
    @ManyToOne(fetch=FetchType.LAZY)
    private Department department;
    @Column(name="FIRST_NAME", nullable=false)
    private String firstName;
    @Id
    @GeneratedValue(strategy=GenerationType.IDENTITY)
    private Integer id;
    @Column(name="LAST_NAME", nullable=false)
    private String lastName;
    @OneToMany(fetch=FetchType.LAZY)
    @JoinTable(name="EMPLOYEE_PHONE", joinColumns=@JoinColumn(name="EMPLOYEE_ID"), inverseJoinColumns=@JoinColumn(name="PHONE_ID"))
    private List<Phone> phones = new ArrayList<Phone>();
    
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
     * Get the addresses.
     *
     * @return  the addresses.
     */
    public List<Address> getAddresses() {
        return this.addresses;
    }

    /**
     * Get the department.
     * 
     * @return  the department.
     */
    public Department getDepartment() {
        return this.department;
    }
    
    /**
     * Get the first name.
     *
     * @return  the first name.
     */
    public String getFirstName() {
        return this.firstName;
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
     * Get the last name.
     *
     * @return  the last name.
     */
    public String getLastName() {
        return this.lastName;
    }

    /**
     * Get the phones.
     * 
     * @return  the phones. 
     */
    public List<Phone> getPhones() {
        return this.phones;
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
            throw new IllegalArgumentException("The replacement object cannot be null.");
        }
        
        return (original == null) ? replacement : original;
    }
    
    /**
     * Set the addresses.
     *
     * @param  addresses  the addresses.
     */
    public void setAddresses(final List<Address> addresses) {
        this.addresses = replaceNull(addresses, new ArrayList<Address>());
    }

    /**
     * Set the department.
     * 
     * @param  department  the department.
     */
    public void setDepartment(final Department department) {
        this.department = department;
    }
    
    /**
     * Set the first name.
     *
     * @param  firstName  the first name.
     */
    public void setFirstName(final String firstName) {
        this.firstName = firstName;
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
     * Set the last name.
     *
     * @param  lastName  the last name.
     */
    public void setLastName(final String lastName) {
        this.lastName = lastName;
    }

    /**
     * Set the phones.
     * 
     * @param  phones  the phones.
     */
    public void setPhones(final List<Phone> phones) {
        this.phones = replaceNull(phones, new ArrayList<Phone>());
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
