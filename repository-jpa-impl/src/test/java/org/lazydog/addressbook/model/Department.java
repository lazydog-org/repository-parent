package org.lazydog.addressbook.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import javax.persistence.*;


/**
 * Department.
 *
 * @author  Ron Rickard
 */
@Entity
public class Department implements Serializable {

    private static final long serialVersionUID = 1L;
    @ManyToOne(fetch=FetchType.EAGER)
    private Company company;
    @OneToMany(mappedBy="department", fetch=FetchType.EAGER)
    private List<Employee> employees = new ArrayList<Employee>();
    @Id
    @GeneratedValue(strategy=GenerationType.IDENTITY)
    private Integer id;
    @Column(nullable=false)
    private String name;

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
        if (object instanceof Department) {
            Department that = (Department)object;
            
            // Check if the IDs are both null or they are both equal.
            if ((this.id == null && that.id == null) || (this.id != null && this.id.equals(that.id))) {
               
                // The reference object is the same as this object.
                equals = true;
            }
        }
        
        return equals;
    }

    /**
     * Get the company.
     * 
     * @return  the company.
     */
    public Company getCompany() {
        return this.company;
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
     * Get the name.
     *
     * @return  the name.
     */
    public String getName() {
        return this.name;
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
     * Set the company.
     * 
     * @param  company  the company.
     */
    public void setCompany(final Company company) {
        this.company = company;
    }
    
    /**
     * Set the employees.
     *
     * @param  employees  the employees.
     */
    public void setEmployees(final List<Employee> employees) {
        this.employees = replaceNull(employees, new ArrayList<Employee>());
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
     * Get this object as a string.
     *
     * @return  this object as a string.
     */
    @Override
    public String toString() {

        StringBuilder stringBuilder = new StringBuilder();
        
        stringBuilder.append("Department = [");
        stringBuilder.append("company = ").append(this.company);
        stringBuilder.append(", employees = ").append(this.employees);
        stringBuilder.append(", id = ").append(this.id);
        stringBuilder.append(", name = ").append(this.name);
        stringBuilder.append("]");

        return stringBuilder.toString();
    }
}
