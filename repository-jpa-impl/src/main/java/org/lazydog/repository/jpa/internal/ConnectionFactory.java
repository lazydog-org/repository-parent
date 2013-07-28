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
package org.lazydog.repository.jpa.internal;

import java.lang.reflect.Method;
import java.sql.Connection;
import javax.persistence.EntityManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Connection factory.
 * 
 * @author  Ron Rickard
 */
public class ConnectionFactory {
    
    private static final Logger logger = LoggerFactory.getLogger(ConnectionFactory.class);
    
    public enum Type {
        ECLIPSE_LINK ("org.eclipse.persistence.jpa.JpaEntityManager"),
        HIBERNATE    ("org.hibernate.ejb.HibernateEntityManager"),
        OPEN_JPA     ("org.apache.openjpa.persistence.OpenJPAEntityManager");
        
        private String entityManagerClassName;
        
        private Type(String entityManagerClassName) {
            this.entityManagerClassName = entityManagerClassName;
        }
        
        protected String getEntityManagerClassName() {
            return this.entityManagerClassName;
        }
    }
    
    private EntityManager entityManager;
         
    /**
     * Hide the constructor.
     * 
     * @param  entityManager  the entity manager.
     */
    private ConnectionFactory(EntityManager entityManager) {
        this.entityManager = entityManager;
    }
    
    /**
     * Does the package for the class name exist?
     * 
     * @param  className  the class name.
     * 
     * @return  true if the package exists, otherwise false.
     */
    private boolean doesPackageExist(String className) {
        
        boolean exist = true;
        
        try {
            Class.forName(className);
        } catch (ClassNotFoundException e) {
            exist = false;
        }
        
        return exist;
    }
      
    /**
     * Does the EclipseLink package exist.
     * 
     * @return  true if the package exists, otherwise false.
     */
    private boolean doesEclipseLinkPackageExist() {
        return doesPackageExist(Type.ECLIPSE_LINK.getEntityManagerClassName());
    }
      
    /**
     * Does the Hibernate package exist.
     * 
     * @return  true if the package exists, otherwise false.
     */
    private boolean doesHibernatePackageExist() {
        return doesPackageExist(Type.HIBERNATE.getEntityManagerClassName());
    }

    /**
     * Does the OpenJPA package exist.
     * 
     * @return  true if the package exists, otherwise false.
     */
    private boolean doesOpenJPAPackageExist() {
        return doesPackageExist(Type.OPEN_JPA.getEntityManagerClassName());
    }
    
    /**
     * Get the connection using EclipseLink.
     * 
     * @return  the connection.
     */
    private Connection getEclipseLinkConnection() {
        
        // Initialize the connection.
        Connection connection = null;

        try {
            
            // Begin the transaction.
            entityManager.getTransaction().begin();
            
            // Get the connection.
            connection = entityManager.unwrap(Connection.class);
            
            // Commit the transaction.
            entityManager.getTransaction().commit();
        } catch(Exception e) {
            logger.error("Unable to get the EclipseLink connection.", e);
        } finally {
            
            // Rollback the transaction if necessary.
            if (entityManager.getTransaction().isActive()) {
                entityManager.getTransaction().rollback();
            }
        }
        
        return connection;
    }
    
    /**
     * Get the connection using Hibernate.
     * 
     * @return  the connection.
     */
    private Connection getHibernateConnection() {
        
        // Initialize the connection.
        Connection connection = null;
        
        try {
            
            // Get the connection using reflection.
            Class hibernateEntityManagerClass = Class.forName("org.hibernate.ejb.HibernateEntityManager");
            Object hibernateEntityManagerObject = hibernateEntityManagerClass.cast(entityManager);
            Method getSessionMethod = hibernateEntityManagerObject.getClass().getMethod("getSession");
            Object sessionObject = getSessionMethod.invoke(hibernateEntityManagerObject);
            Class sessionImplClass = Class.forName("org.hibernate.internal.SessionImpl");
            Object sessionImplObject = sessionImplClass.cast(sessionObject);
            Method connectionMethod = sessionImplObject.getClass().getMethod("connection");
            connection = (Connection)connectionMethod.invoke(sessionImplObject);       
        } catch (Exception e) {
            logger.error("Unable to get the Hibernate connection.", e);
        }
        
        return connection;
    }
        
    /**
     * Get the connection using OpenJPA.
     * 
     * @return  the connection.
     */
    private Connection getOpenJPAConnection() {
        
        // Initialize the connection.
        Connection connection = null;

        try {
            
            // Begin the transaction.
            entityManager.getTransaction().begin();
            
            // Get the connection using reflection.
            Class openJPAEntityManagerClass = Class.forName("org.apache.openjpa.persistence.OpenJPAEntityManager");
            Object openJPAEntityManagerObject = openJPAEntityManagerClass.cast(entityManager);
            Method getConnectionMethod = openJPAEntityManagerObject.getClass().getMethod("getConnection");
            connection = (Connection)getConnectionMethod.invoke(openJPAEntityManagerObject);
            
            // Commit the transaction.
            entityManager.getTransaction().commit();
        } catch (Exception e) {
            logger.error("Unable to get the OpenJPA connection.", e);
        } finally {
            
            // Rollback the transaction if necessary.
            if (entityManager.getTransaction().isActive()) {
                entityManager.getTransaction().rollback();
            }
        }
        
        return connection;
    }
    
    /**
     * Get the connection.
     * 
     * @return  the connection.
     */
    public Connection getConnection() {
        
        Connection connection = null;

        // Get the connection determined by the package.
        if (this.doesEclipseLinkPackageExist()) {
            connection = this.getEclipseLinkConnection();
        } else if (this.doesHibernatePackageExist()) {
            connection = this.getHibernateConnection();
        } else if (this.doesOpenJPAPackageExist()) {
            connection = this.getOpenJPAConnection();
        }

        return connection;
    }
    
    /**
     * Get the connection for the type.
     * 
     * @param  type  the type.
     * 
     * @return  the connection.
     */
    public Connection getConnection(Type type) {
        
        Connection connection = null;
        
        // Get the connection determined by the type.
        switch (type) {
            
            case ECLIPSE_LINK:
                if (this.doesEclipseLinkPackageExist()) {
                    connection = this.getEclipseLinkConnection();
                }
                break;
            case HIBERNATE:
                if (this.doesHibernatePackageExist()) {
                    connection = this.getHibernateConnection();
                }
                break;
            case OPEN_JPA:
                if (this.doesOpenJPAPackageExist()) {
                    connection = this.getOpenJPAConnection();
                }
                break;
        }
        
        return connection;
    }
    
    /**
     * Get a new instance of the connection factory.
     * 
     * @param  entityManager  the entity manager.
     * 
     * @return  a new instance of the connection factory.
     */
    public static ConnectionFactory newInstance(EntityManager entityManager) {
        return new ConnectionFactory(entityManager);
    }
}
