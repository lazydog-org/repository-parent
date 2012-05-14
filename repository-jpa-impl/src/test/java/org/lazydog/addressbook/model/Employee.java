package org.lazydog.addressbook.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import javax.persistence.*;


/**
 * Person.
 *
 * @author  Ron Rickard
 */
@Entity
public class Employee implements Serializable {

    private static final long serialVersionUID = 1L;
    @OneToMany(fetch=FetchType.EAGER)
    @JoinTable(name="EMPLOYEE_ADDRESS", joinColumns=@JoinColumn(name="EMPLOYEE_ID"), inverseJoinColumns=@JoinColumn(name="ADDRESS_ID"))
    private List<Address> addresses = new ArrayList<Address>();
    @ManyToOne(fetch=FetchType.EAGER)
    private Department department;
    @Column(name="FIRST_NAME", nullable=false)
    private String firstName;
    @Id
    @GeneratedValue(strategy=GenerationType.IDENTITY)
    private Integer id;
    @Column(name="LAST_NAME", nullable=false)
    private String lastName;
    @OneToMany(fetch=FetchType.EAGER)
    @JoinTable(name="EMPLOYEE_PHONE", joinColumns=@JoinColumn(name="EMPLOYEE_ID"), inverseJoinColumns=@JoinColumn(name="PHONE_ID"))
    private List<Phone> phones = new ArrayList<Phone>();

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
        if (object instanceof Employee) {
            Employee that = (Employee)object;
            
            // Check if the IDs are both null or they are both equal.
            if ((this.id == null && that.id == null) || (this.id != null && this.id.equals(that.id))) {
               
                // The reference object is the same as this object.
                equals = true;
            }
        }
        
        return equals;
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
     * Returns a hash code value for this object.
     * 
     * @return  a hash code value for this object.
     */
    @Override
    public int hashCode() {
        return (id != null ? id.hashCode() : 0);
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

        StringBuilder stringBuilder = new StringBuilder();
        
        stringBuilder.append("Employee = [");
        stringBuilder.append("addresses = ").append(this.addresses);
        stringBuilder.append(", department = ").append(this.department);
        stringBuilder.append(", firstName = ").append(this.firstName);
        stringBuilder.append(", id = ").append(this.id);
        stringBuilder.append(", lastName = ").append(this.lastName);
        stringBuilder.append(", phones = ").append(this.phones);
        stringBuilder.append("]");

        return stringBuilder.toString();
    }
}
