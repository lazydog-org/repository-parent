package org.lazydog.addressbook.model;

import java.util.List;
import javax.persistence.Entity;
import javax.persistence.Column;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;


/**
 * Address.
 *
 * @author  Ron Rickard
 */
@Entity
public class Address {

    @Column(nullable=false)
    private String city;
    @ManyToMany(fetch=FetchType.EAGER)
    @JoinTable(name="EMPLOYEE_ADDRESS",
        joinColumns=@JoinColumn(name="ADDRESS_ID", referencedColumnName="ID", columnDefinition="INTEGER"),
        inverseJoinColumns=@JoinColumn(name="EMPLOYEE_ID", referencedColumnName="ID", columnDefinition="INTEGER"))
    private List<Employee> employees;
    @Id
    private Integer id;
    @ManyToOne(fetch=FetchType.EAGER)
    @JoinColumn(name="STATE_ID", nullable=false)
    private State state;
    @Column(name="STREET_ADDRESS", nullable=false)
    private String streetAddress;
    @Column(nullable=false)
    private String zipcode;

    /**
     * Get the city.
     *
     * @return  the city.
     */
    public String getCity() {
        return this.city;
    }

    /**
     * Get the employees.
     * 
     * @return  the employees.
     */
    public List<Employee> getEmployees() {
        return this.employees;
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
    public State getState() {
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
     * Set the city.
     *
     * @param  city  the city.
     */
    public void setCity(String city) {
        this.city = city;
    }

    /**
     * Set the employees.
     * 
     * @param  employees  the employees.
     */
    public void setEmployees(List<Employee> employees) {
        this.employees = employees;
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
     * Set the state.
     *
     * @param  state  the state.
     */
    public void setState(State state) {
        this.state = state;
    }

    /**
     * Set the street address.
     *
     * @param  streetAddress  the street address.
     */
    public void setStreetAddress(String streetAddress) {
        this.streetAddress = streetAddress;
    }

    /**
     * Set the zipcode.
     *
     * @param  zipcode  the zipcode.
     */
    public void setZipcode(String zipcode) {
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
        buffer.append("Address = [");
        buffer.append("id = " + this.id).append(",");
        buffer.append("employees = " + this.employees).append(",");
        buffer.append("streetAddress = " + this.streetAddress).append(",");
        buffer.append("city = " + this.city).append(",");
        buffer.append("state = " + this.state).append(",");
        buffer.append("zipcode = " + this.zipcode);
        buffer.append("]");

        return buffer.toString();
    }
}
