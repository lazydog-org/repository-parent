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
import org.jboss.weld.environment.se.Weld;
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
import org.lazydog.repository.jpa.bootstrap.Configuration;
import org.lazydog.repository.jpa.internal.ConnectionFactory;
import static org.unitils.reflectionassert.ReflectionAssert.assertReflectionEquals;

/**
 * Abstract repository test.
 * 
 * @author  Ron Rickard
 */
@RunWith(Parameterized.class)
public class AbstractRepositoryTest {

    private static final String ECLIPSE_LINK_PERSISTENT_UNIT_NAME = "AddressBookEclipseLink";
    private static final String HIBERNATE_PERSISTENT_UNIT_NAME = "AddressBookHibernate";
    private static final String TEST_FILE = "META-INF/dataset.xml";
    private static Address expectedAddress1;
    private static Address expectedAddress2;
    private static Address expectedAddress3;
    private static AddressBookRepository addressBookRepository;
    private static NonEntityAddress expectedAddress4;
    private static String persistenceUnitName;
    private static Weld weld;
    
    /**
     * Initialize the abstract repository test.
     * 
     * @param  newPersistenceUnitName  the new persistence unit name.
     */
    public AbstractRepositoryTest(String newPersistenceUnitName) throws Exception {

        // Check if this is a new persistence unit name.
        if (persistenceUnitName == null || !persistenceUnitName.equals(newPersistenceUnitName)) {

            // Check if the address book repository exists.
            if (addressBookRepository != null) {

                // Drop the address book database.
                try {
                    DriverManager.getConnection("jdbc:derby:memory:./target/addressbook;drop=true");
                } catch (SQLNonTransientConnectionException e) {
                    // Ignore.
                }
            }
            
            // Create the address book database.
            DriverManager.getConnection("jdbc:derby:memory:./target/addressbook;create=true");

            // (Re)Start the weld container.
            if (weld != null) {
                weld.shutdown();
            }
            weld = new Weld();
            
            // Initialize the entity manager.
            Configuration.createEntityManager(newPersistenceUnitName);

            // Get the address book repository.
            addressBookRepository = weld.initialize().instance().select(AddressBookRepository.class).get();

            // Save the persistence unit name.
            persistenceUnitName = newPersistenceUnitName;
        }
    }
    
    @Parameters
    public static Collection<Object[]> testData() {
        return Arrays.asList(new Object[][] {{ECLIPSE_LINK_PERSISTENT_UNIT_NAME}, {HIBERNATE_PERSISTENT_UNIT_NAME}});
    }

    @AfterClass
    public static void afterClass() throws Exception {

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
        addressBookRepository.getEntityManager().clear();
        addressBookRepository.getEntityManager().getEntityManagerFactory().getCache().evictAll();
    }

    @Test
    public void testFind() {
        assertReflectionEquals(expectedAddress1, addressBookRepository.find(Address.class, expectedAddress1.getId()));
    }

    @Test
    public void testFindNot() {
        assertNull(addressBookRepository.find(Address.class, 3));
    }

    @Test(expected=IllegalArgumentException.class)
    public void testFindNullClass() {
        addressBookRepository.find(null, expectedAddress1.getId());
    }

    @Test(expected=IllegalArgumentException.class)
    public void testFindNonEntity() {
        addressBookRepository.find(NonEntityAddress.class, expectedAddress1.getId());
    }

    @Test
    public void testFindByCritera() {
        Criteria<Address> criteria = addressBookRepository.getCriteria(Address.class);
        criteria.add(Comparison.eq("id", expectedAddress1.getId()));
        assertReflectionEquals(expectedAddress1, addressBookRepository.find(Address.class, criteria));
    }

    @Test
    public void testFindByCriteriaNot() {
        Criteria<Address> criteria = addressBookRepository.getCriteria(Address.class);
        criteria.add(Comparison.eq("id", 3));
        assertNull(addressBookRepository.find(Address.class, criteria));
    }

    @Test(expected=IllegalArgumentException.class)
    public void testFindByCriteriaNullClass() {
        Criteria<Address> criteria = addressBookRepository.getCriteria(Address.class);
        criteria.add(Comparison.eq("id", expectedAddress1.getId()));
        addressBookRepository.find(null, criteria);
    }

    @Test
    public void testGetCriteria() {
        addressBookRepository.getCriteria(Address.class);
    }

    @Test(expected=IllegalArgumentException.class)
    public void testGetCriteriaNull() {
        addressBookRepository.getCriteria(null);
    }

    @Test
    public void testPersist() {
        Address actualAddress3 = addressBookRepository.persist(expectedAddress3);
        expectedAddress3.setId(actualAddress3.getId());
        assertReflectionEquals(expectedAddress3, actualAddress3);
    }

    @Test(expected=IllegalArgumentException.class)
    public void testPersistNull() {
        addressBookRepository.persist(null);
    }
    
    @Test(expected=IllegalArgumentException.class)
    public void testPersistNonEntity() {
        addressBookRepository.persist(expectedAddress4);
    }

    @Test
    public void testRemove() {
        addressBookRepository.remove(Address.class, expectedAddress1.getId());
        assertNull(addressBookRepository.find(Address.class, expectedAddress1.getId()));
    }

    @Test(expected=EntityNotFoundException.class)
    public void testRemoveNot() {
        addressBookRepository.remove(Address.class, expectedAddress1.getId());
        assertNull(addressBookRepository.find(Address.class, expectedAddress1.getId()));
        addressBookRepository.remove(Address.class, expectedAddress1.getId());
    }

    @Test(expected=IllegalArgumentException.class)
    public void testRemoveNullClass() {
        addressBookRepository.remove(null, expectedAddress1.getId());
    }

    @Test(expected=IllegalArgumentException.class)
    public void testRemoveNullId() {
        addressBookRepository.remove(Address.class, null);
    }

    @Test(expected=IllegalArgumentException.class)
    public void testRemoveNonEntity() {
        addressBookRepository.remove(NonEntityAddress.class, expectedAddress1.getId());
    }

    private IDatabaseConnection getDatabaseConnection() throws Exception {
        
        ConnectionFactory.Type type = null;
        
        // Check if the new persistence unit is for EclipseLink.
        if (persistenceUnitName.equals(ECLIPSE_LINK_PERSISTENT_UNIT_NAME)) {
            type = ConnectionFactory.Type.ECLIPSE_LINK;

        // Check if the new persistence unit is for Hibernate.
        } else if (persistenceUnitName.equals(HIBERNATE_PERSISTENT_UNIT_NAME)) {
            type = ConnectionFactory.Type.HIBERNATE;
        }

        return new DatabaseConnection(ConnectionFactory.newInstance(addressBookRepository.getEntityManager()).getConnection(type));
    }
    
    private static IDataSet getDataSet() throws Exception {
        return new FlatXmlDataSetBuilder().build(Thread.currentThread().getContextClassLoader().getResourceAsStream(TEST_FILE));
    }
    
    private void refreshDatabase() throws Exception {
        
        // Get the database connection.
        IDatabaseConnection databaseConnection = this.getDatabaseConnection();
        
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
