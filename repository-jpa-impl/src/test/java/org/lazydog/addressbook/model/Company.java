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
 * Company.
 *
 * @author  Ron Rickard
 */
@Entity
public class Company implements Serializable {

    private static final long serialVersionUID = 1L;
    @OneToMany(fetch=FetchType.EAGER)
    @JoinTable(name="COMPANY_ADDRESS", joinColumns=@JoinColumn(name="COMPANY_ID"), inverseJoinColumns=@JoinColumn(name="ADDRESS_ID"))
    private List<Address> addresses = new ArrayList<Address>();
    @OneToMany(mappedBy="company", fetch=FetchType.EAGER)
    private List<Department> departments = new ArrayList<Department>();
    @Id
    @GeneratedValue(strategy=GenerationType.IDENTITY)
    private Integer id;
    @Column(nullable=false)
    private String name;
    @OneToMany(fetch=FetchType.EAGER)
    @JoinTable(name="COMPANY_PHONE", joinColumns=@JoinColumn(name="COMPANY_ID"), inverseJoinColumns=@JoinColumn(name="PHONE_ID"))
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
     * Get the departments.
     *
     * @return  the departments.
     */
    public List<Department> getDepartments() {
        return this.departments;
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
     * Get the name.
     *
     * @return  the name.
     */
    public String getName() {
        return this.name;
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
     * Set the departments.
     *
     * @param  departments  the departments.
     */
    public void setDepartments(final List<Department> departments) {
        this.departments = replaceNull(departments, new ArrayList<Department>());
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
     * Set the name.
     *
     * @param  lastName  the last name.
     */
    public void setName(final String name) {
        this.name = name;
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
