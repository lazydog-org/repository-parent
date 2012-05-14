package org.lazydog.addressbook.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import javax.persistence.*;


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
        if (object instanceof Company) {
            Company that = (Company)object;
            
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

        StringBuilder stringBuilder = new StringBuilder();
        
        stringBuilder.append("Company = [");
        stringBuilder.append("addresses = ").append(this.addresses);
        stringBuilder.append(", departments = ").append(this.departments);
        stringBuilder.append(", id = ").append(this.id);
        stringBuilder.append(", name = ").append(this.name);
        stringBuilder.append(", phones = ").append(this.phones);
        stringBuilder.append("]");

        return stringBuilder.toString();
    }
}
