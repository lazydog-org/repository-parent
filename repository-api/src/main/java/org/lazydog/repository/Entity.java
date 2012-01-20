package org.lazydog.repository;

import java.io.Serializable;


/**
 * Entity.
 * 
 * @author  Ron Rickard
 */
public abstract class Entity<T extends Entity<T,U>, U> implements Serializable {

    private static final long serialVersionUID = 1L;
    private U id;

    /**
     * Get the ID.
     * 
     * @return  the ID.
     */
    public U getId() {
        return id;
    }

    /**
     * Set the ID.
     * 
     * @param  id  the ID.
     */
    public  void setId(final U id) {
        this.id = id;
    }
}

