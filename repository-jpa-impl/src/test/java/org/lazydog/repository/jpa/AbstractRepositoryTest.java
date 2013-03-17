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
package org.lazydog.repository.jpa;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLNonTransientConnectionException;
import java.sql.Statement;
import java.util.Arrays;
import java.util.Collection;
import javax.persistence.EntityNotFoundException;
import org.dbunit.database.DatabaseConnection;
import org.dbunit.database.IDatabaseConnection;
import org.dbunit.dataset.IDataSet;
import org.dbunit.dataset.xml.FlatXmlDataSetBuilder;
import org.dbunit.operation.DatabaseOperation;
import org.junit.AfterClass;
import static org.junit.Assert.assertNull;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.junit.runners.Parameterized.Parameters;
import org.lazydog.addressbook.AddressBookRepository;
import org.lazydog.addressbook.model.Address;
import org.lazydog.addressbook.model.NonEntityAddress;
import org.lazydog.repository.Criteria;
import org.lazydog.repository.criterion.Comparison;
import static org.unitils.reflectionassert.ReflectionAssert.assertReflectionEquals;

/**
 * Abstract repository test.
 * 
 * @author  Ron Rickard
 */
@RunWith(Parameterized.class)
public class AbstractRepositoryTest {

    private static final String ECLIPSE_LINK = "EclipseLink";
    private static final String HIBERNATE = "Hibernate";
    private static final String OPEN_JPA = "OpenJPA";
    private static final String TEST_FILE = "dataset.xml";
    private static IDatabaseConnection databaseConnection;
    private static Address expectedAddress1;
    private static Address expectedAddress2;
    private static Address expectedAddress3;
    private static NonEntityAddress expectedAddress4;
    private static String persistenceUnitName;
    private static AddressBookRepository repository;
    
    /**
     * Initialize the abstract repository test.
     * 
     * @param  newPersistenceUnitName  the new persistence unit name.
     */
    public AbstractRepositoryTest(String newPersistenceUnitName) throws Exception {

        // Check if this is a new persistence unit name.
        if (persistenceUnitName == null || !persistenceUnitName.equals(newPersistenceUnitName)) {
            
                 
            // Check if the repository exists.
            if (repository != null) {
                
                // Close the database connection.
                databaseConnection.close();

                // Close the repository.
                repository.close();
                
                // Drop the address book database.
                try {
                    DriverManager.getConnection("jdbc:derby:memory:./target/addressbook;drop=true");
                } catch (SQLNonTransientConnectionException e) {
                    // Ignore.
                }
            }

            // Create the address book database.
            DriverManager.getConnection("jdbc:derby:memory:./target/addressbook;create=true");
        
            // Open the repository.
            repository = AddressBookRepository.newInstance(newPersistenceUnitName);
            
            // Check if the new persistence unit is for EclipseLink.
            if (newPersistenceUnitName.contains(ECLIPSE_LINK)) {
                databaseConnection = new DatabaseConnection(ConnectionFactory.newInstance(repository.getEntityManager()).newConnection(ConnectionFactory.Type.ECLIPSE_LINK));
                
            // Check if the new persistence unit is for Hibernate.
            } else if (newPersistenceUnitName.contains(HIBERNATE)) {
                databaseConnection = new DatabaseConnection(ConnectionFactory.newInstance(repository.getEntityManager()).newConnection(ConnectionFactory.Type.HIBERNATE));
                
            // Check if the new persistence unit is for OpenJPA.
            } else if (newPersistenceUnitName.contains(OPEN_JPA)) {
                databaseConnection = new DatabaseConnection(ConnectionFactory.newInstance(repository.getEntityManager()).newConnection(ConnectionFactory.Type.OPEN_JPA));
            }
            
            // Save the persistence unit name.
            persistenceUnitName = newPersistenceUnitName;
        }
    }
    
    @Parameters
    public static Collection<Object[]> testData() {
        return Arrays.asList(new Object[][] {{"AddressBookEclipseLink"}, {"AddressBookHibernate"}, {"AddressBookOpenJPA"}});
    }

    @AfterClass
    public static void afterClass() throws Exception {
               
        // Close the database connection.
        databaseConnection.close();
        
        // Close the repository.
        repository.close();
        
        // Drop the address book database.
        try {
            DriverManager.getConnection("jdbc:derby:memory:./target/addressbook;drop=true");
        } catch (SQLNonTransientConnectionException e) {
            // Ignore.
        }
    }

    @BeforeClass
    public static void beforeClass() throws Exception {

        // Ensure the derby.log file is in the target directory.
        System.setProperty("derby.system.home", "./target");

        expectedAddress1 = new Address();
        expectedAddress1.setCity("Los Angeles");
        expectedAddress1.setId(1);
        expectedAddress1.setState("California");
        expectedAddress1.setStreetAddress("111 Street Avenue");
        expectedAddress1.setZipcode("11111");

        expectedAddress2 = new Address();
        expectedAddress2.setCity("Phoenix");
        expectedAddress2.setId(2);
        expectedAddress2.setState("Arizona");
        expectedAddress2.setStreetAddress("222 Street Avenue");
        expectedAddress2.setZipcode("22222");

        expectedAddress3 = new Address();
        expectedAddress3.setCity("Denver");
        expectedAddress3.setId(3);
        expectedAddress3.setState("Colorado");
        expectedAddress3.setStreetAddress("333 Street Avenue");
        expectedAddress3.setZipcode("33333");
        
        expectedAddress4 = new NonEntityAddress();
        expectedAddress4.setCity("Baltimore");
        expectedAddress4.setId(4);
        expectedAddress4.setState("Maryland");
        expectedAddress4.setStreetAddress("444 Street Avenue");
        expectedAddress4.setZipcode("44444");
    }

    @Before
    public void beforeTest() throws Exception {

        // Refresh the database.
        this.refreshDatabase();
        
        // Since the database was modified outside of the entity manager control, clear the cache.
        repository.clearCache();
    }

    @Test
    public void testFind() {
        assertReflectionEquals(expectedAddress1, repository.find(Address.class, expectedAddress1.getId()));
    }

    @Test
    public void testFindNot() {
        assertNull(repository.find(Address.class, new Integer(3)));
    }

    @Test(expected=IllegalArgumentException.class)
    public void testFindNullClass() {
        repository.find(null, expectedAddress1.getId());
    }

    @Test(expected=IllegalArgumentException.class)
    public void testFindNonEntity() {
        repository.find(NonEntityAddress.class, expectedAddress1.getId());
    }

    @Test
    public void testFindByCritera() {
        Criteria<Address> criteria = repository.getCriteria(Address.class);
        criteria.add(Comparison.eq("id", expectedAddress1.getId()));
        assertReflectionEquals(expectedAddress1, repository.find(Address.class, criteria));
    }

    @Test
    public void testFindByCriteriaNot() {
        Criteria<Address> criteria = repository.getCriteria(Address.class);
        criteria.add(Comparison.eq("id", new Integer(3)));
        assertNull(repository.find(Address.class, criteria));
    }

    @Test(expected=IllegalArgumentException.class)
    public void testFindByCriteriaNullClass() {
        Criteria<Address> criteria = repository.getCriteria(Address.class);
        criteria.add(Comparison.eq("id", expectedAddress1.getId()));
        repository.find(null, criteria);
    }

    @Test
    public void testGetCriteria() {
        repository.getCriteria(Address.class);
    }

    @Test(expected=IllegalArgumentException.class)
    public void testGetCriteriaNull() {
        repository.getCriteria(null);
    }

    @Test
    public void testPersist() {
        try {
            repository.beginTransaction();
            Integer id = null;
            if (persistenceUnitName.contains(OPEN_JPA)) {
                id = expectedAddress3.getId();
                expectedAddress3.setId(null);
            }
            Address actualAddress3 = repository.persist(expectedAddress3);
            if (persistenceUnitName.contains(OPEN_JPA)) {
                expectedAddress3.setId(id);
            }
            expectedAddress3.setId(actualAddress3.getId());
            assertReflectionEquals(expectedAddress3, actualAddress3);
            repository.commitTransaction();
        } finally {
            repository.rollbackTransaction();
        }
    }

    @Test(expected=IllegalArgumentException.class)
    public void testPersistNull() {
        try {
            repository.beginTransaction();
            repository.persist(null);
            repository.commitTransaction();
        } finally {
            repository.rollbackTransaction();
        }
    }
    
    @Test(expected=IllegalArgumentException.class)
    public void testPersistNonEntity() {
        try {
            repository.persist(expectedAddress4);
        } finally {
            repository.rollbackTransaction();
        }
    }

    @Test
    public void testRemove() {
        try {
            repository.beginTransaction();
            repository.remove(Address.class, expectedAddress1.getId());
            assertNull(repository.find(Address.class, expectedAddress1.getId()));
            repository.commitTransaction();
        } finally {
            repository.rollbackTransaction();
        }
    }

    @Test(expected=EntityNotFoundException.class)
    public void testRemoveNot() {
        try {
            repository.beginTransaction();
            repository.remove(Address.class, expectedAddress1.getId());
            assertNull(repository.find(Address.class, expectedAddress1.getId()));
            repository.remove(Address.class, expectedAddress1.getId());
            repository.commitTransaction();
        } finally {
            repository.rollbackTransaction();
        }
    }

    @Test(expected=IllegalArgumentException.class)
    public void testRemoveNullClass() {
        try {
            repository.beginTransaction();
            repository.remove(null, expectedAddress1.getId());
            repository.commitTransaction();
        } finally {
            repository.rollbackTransaction();
        }
    }

    @Test(expected=IllegalArgumentException.class)
    public void testRemoveNullId() {
        try {
            repository.beginTransaction();
            repository.remove(Address.class, null);
            repository.commitTransaction();
        } finally {
            repository.rollbackTransaction();
        }
    }

    @Test(expected=IllegalArgumentException.class)
    public void testRemoveNonEntity() {
        try {
            repository.beginTransaction();
            repository.remove(NonEntityAddress.class, expectedAddress1.getId());
            repository.commitTransaction();
        } finally {
            repository.rollbackTransaction();
        }
    }

    private static IDataSet getDataSet() throws Exception {
        return new FlatXmlDataSetBuilder().build(Thread.currentThread().getContextClassLoader().getResourceAsStream(TEST_FILE));
    }
    
    private void refreshDatabase() throws Exception {
        
        // Refresh the dataset in the database.
        DatabaseOperation.CLEAN_INSERT.execute(databaseConnection, getDataSet());
        
        // Get the maximum ID from the address table.
        Statement statement = databaseConnection.getConnection().createStatement();
        ResultSet resultSet = statement.executeQuery("SELECT MAX(id) as max_id FROM address");
        resultSet.next();
        int max = resultSet.getInt("max_id");
        resultSet.close();
        statement.close();
        
        // Set the address table next generated Id value to the maximum ID plus 1.
        statement =  databaseConnection.getConnection().createStatement();
        statement.executeUpdate("ALTER TABLE address ALTER COLUMN id RESTART WITH " + (max + 1));
        statement.close();
    }
}
