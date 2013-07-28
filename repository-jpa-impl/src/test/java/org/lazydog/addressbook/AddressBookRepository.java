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

import javax.annotation.PostConstruct;
import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.Persistence;
import javax.persistence.PersistenceContext;
import org.apache.myfaces.extensions.cdi.jpa.api.Transactional;
import org.lazydog.repository.jpa.AbstractRepository;
import org.lazydog.repository.jpa.annotation.PersistenceUnitName;

/**
 * Address book repository.
 *
 * @author  Ron Rickard
 */
@ApplicationScoped
public class AddressBookRepository extends AbstractRepository {

    private String persistenceUnitName;
    @PersistenceContext
    private EntityManager entityManager;

    @Transactional
    @Override
    public <T> T persist(final T entity) {
        return super.persist(entity);
    }
    
    @Transactional
    @Override
    public <T,U> void remove(final Class<T> entityClass, final U id) {
        super.remove(entityClass, id);
    }

    /**
     * Set the persistence unit name.
     * 
     * @param  persistenceUnitName  the persistence unit name.
     */
    @Inject 
    public void setPersistenceUnitName(@PersistenceUnitName final String persistenceUnitName) {
        this.persistenceUnitName = persistenceUnitName;
    }

    /**
     * Startup the JDNSaaS repository.
     */
    @PostConstruct
    public void startup() {

        // Check if the entity manager was not injected.
        // This will occur if this is a standalone application or Tomcat.
        if (this.entityManager == null) {
            
            // Set the entity manager using the persistence unit name.
            this.entityManager = Persistence.createEntityManagerFactory(this.persistenceUnitName).createEntityManager();
        }
        this.setEntityManager(this.entityManager);
    }
}
