package org.lazydog.addressbook.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;


/**
 * State.
 *
 * @author  Ron Rickard
 */
@Entity
public class State {

    @Column(nullable=false)
    private String code;
    @Id
    private Integer id;
    @Column(nullable=false)
    private String name;

    /**
     * Get the code.
     *
     * @return  the code.
     */
    public String getCode() {
        return this.code;
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
     * Set the code.
     *
     * @param  code  the code.
     */
    public void setCode(String code) {
        this.code = code;
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
     * Set the name.
     *
     * @param  name  the name.
     */
    public void setName(String name) {
        this.name = name;
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
        buffer.append("State = [");
        buffer.append("id = " + this.id).append(",");
        buffer.append("name = " + this.name).append(",");
        buffer.append("code = " + this.code);
        buffer.append("]");

        return buffer.toString();
    }
}
