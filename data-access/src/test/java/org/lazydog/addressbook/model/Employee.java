package org.lazydog.addressbook.model;

import java.util.ArrayList;
import java.util.List;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;


/**
 * Employee.
 *
 * @author  Ron Rickard
 */
@Entity
public class Employee {

    @ManyToMany
    @JoinTable(name="EMPLOYEE_ADDRESS",
        joinColumns=@JoinColumn(name="EMPLOYEE_ID", referencedColumnName="ID", columnDefinition="INTEGER"),
        inverseJoinColumns=@JoinColumn(name="ADDRESS_ID", referencedColumnName="ID", columnDefinition="INTEGER"))
    private List<Address> addresses = new ArrayList<Address>();
    @Column(name="FIRST_NAME", nullable=false)
    private String firstName;
    @Id
    private Integer id;
    @Column(name="LAST_NAME", nullable=false)
    private String lastName;

    /**
     * Get the addresses.
     *
     * @return  the addresses.
     */
    public List<Address> getAddresses() {
        return this.addresses;
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
     * Set the addresses.
     *
     * @param  addresses  the addresses.
     */
    public void setAddresses(List<Address> addresses) {
        this.addresses = addresses;
    }

    /**
     * Set the first name.
     *
     * @param  firstName  the first name.
     */
    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    /**
     * Set the ID.
     *
     * @param  id  the ID.
     */
    public void setId(Integer id) {
        this.id = id;
    }

    /**
     * Set the last name.
     *
     * @param  lastName  the last name.
     */
    public void setLastName(String lastName) {
        this.lastName = lastName;
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
        buffer.append("Employee = [");
        buffer.append("id = " + this.id).append(",");
        buffer.append("firstName = " + this.firstName).append(",");
        buffer.append("lastName = " + this.lastName).append(",");
        buffer.append("addresses = " + this.addresses);
        buffer.append("]");

        return buffer.toString();
    }
}
