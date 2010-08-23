package org.lazydog.addressbook.model;

import java.util.ArrayList;
import java.util.List;
import javax.persistence.Entity;
import javax.persistence.Column;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
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

    @ManyToMany(fetch=FetchType.EAGER)
    @JoinTable(name="employee_address",
        joinColumns=@JoinColumn(name="employee_id", referencedColumnName="id", columnDefinition="INTEGER"),
        inverseJoinColumns=@JoinColumn(name="address_id", referencedColumnName="id", columnDefinition="INTEGER"))
    private List<Address> addresses = new ArrayList<Address>();
    @Column(nullable=false)
    private String firstName;
    @Id
    @GeneratedValue(strategy=GenerationType.IDENTITY)
    private Integer id;
    @Column(nullable=false)
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
}
