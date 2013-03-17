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
package org.lazydog.addressbook;

import javax.persistence.Persistence;
import org.lazydog.repository.jpa.AbstractRepository;

/**
 * Address book repository.
 *
 * @author  Ron Rickard
 */
public class AddressBookRepository extends AbstractRepository {

    /**
     * Hide the constructor.
     * 
     * @param  persistenceUnitName  the persistence unit name.
     */
    private AddressBookRepository(String persistenceUnitName) {
        this.setEntityManager(Persistence.createEntityManagerFactory(persistenceUnitName).createEntityManager());
    }
    
    /**
     * Begin the transaction.
     */
    public void beginTransaction() {
        this.getEntityManager().getTransaction().begin();
    }
    
    /**
     * Clear the cache.
     */
    public void clearCache() {
        this.getEntityManager().clear();
        this.getEntityManager().getEntityManagerFactory().getCache().evictAll();
    }
    
    /**
     * Close the repository.
     */
    public void close() {
        this.getEntityManager().getEntityManagerFactory().close();
    }
    
    /**
     * Commit the transaction.
     */
    public void commitTransaction() {
        this.getEntityManager().getTransaction().commit();
    }

    /**
     * Get a new instance of the address book repository.
     * 
     * @param  persistenceUnitName  the persistence unit name.
     * 
     * @return  a new instance of the address book repository.
     */
    public static AddressBookRepository newInstance(String persistenceUnitName) {
        return new AddressBookRepository(persistenceUnitName);
    }
        
    /**
     * Rollback the transaction.
     */
    public void rollbackTransaction() {
        if (this.getEntityManager().getTransaction().isActive()) {
            this.getEntityManager().getTransaction().rollback();
        }
    }

}
