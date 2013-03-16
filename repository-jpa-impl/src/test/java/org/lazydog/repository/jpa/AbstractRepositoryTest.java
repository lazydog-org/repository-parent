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

import javax.persistence.EntityNotFoundException;
import javax.sql.DataSource;
import org.dbunit.database.DatabaseConnection;
import org.dbunit.database.IDatabaseConnection;
import org.dbunit.dataset.IDataSet;
import org.dbunit.dataset.xml.FlatXmlDataSetBuilder;
import org.dbunit.operation.DatabaseOperation;
import static org.junit.Assert.assertNull;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.lazydog.addressbook.AddressBookRepository;
import org.lazydog.addressbook.model.Address;
import org.lazydog.addressbook.model.NonEntityAddress;
import org.lazydog.repository.Criteria;
import org.lazydog.repository.criterion.Comparison;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import static org.unitils.reflectionassert.ReflectionAssert.assertReflectionEquals;

/**
 * Abstract repository test.
 * 
 * @author  Ron Rickard
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations={"classpath:/applicationContext.xml"})
@Transactional(propagation = Propagation.REQUIRED)
public class AbstractRepositoryTest {

    private static final String TEST_FILE = "dataset.xml";
    private static IDatabaseConnection databaseConnection;
    private static Address expectedAddress1;
    private static Address expectedAddress2;
    private static Address expectedAddress3;
    private static NonEntityAddress expectedAddress4;
    @Autowired private AddressBookRepository repository;
    @Autowired private DataSource dataSource;

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
        expectedAddress3.setState("Colorado");
        expectedAddress3.setStreetAddress("333 Street Avenue");
        expectedAddress3.setZipcode("33333");
        
        expectedAddress4 = new NonEntityAddress();
        expectedAddress4.setCity("Baltimore");
        expectedAddress4.setState("Maryland");
        expectedAddress4.setStreetAddress("444 Street Avenue");
        expectedAddress4.setZipcode("44444");
    }

    @Before
    public void beforeTest() throws Exception {

        // Since the database is being modified outside of the entity manager control, 
        // clear the entities and the cache.
        repository.getEntityManager().clear();
        repository.getEntityManager().getEntityManagerFactory().getCache().evictAll();

        // Refresh the dataset in the database.
        DatabaseOperation.CLEAN_INSERT.execute(getDatabaseConnection(), getDataSet());
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
        Address actualAddress3 = repository.persist(expectedAddress3);
        expectedAddress3.setId(actualAddress3.getId());
        assertReflectionEquals(expectedAddress3, actualAddress3);
    }

    @Test(expected=IllegalArgumentException.class)
    public void testPersistNull() {
        repository.persist(null);
    }
    
    @Test(expected=IllegalArgumentException.class)
    public void testPersistNonEntity() {
        repository.persist(expectedAddress4);
    }

    @Test
    public void testRemove() {
        repository.remove(Address.class, expectedAddress1.getId());
        assertNull(repository.find(Address.class, expectedAddress1.getId()));
    }

    @Test(expected=EntityNotFoundException.class)
    public void testRemoveNot() {
        repository.remove(Address.class, expectedAddress1.getId());
        assertNull(repository.find(Address.class, expectedAddress1.getId()));
        repository.remove(Address.class, expectedAddress1.getId());
    }

    @Test(expected=IllegalArgumentException.class)
    public void testRemoveNullClass() {
        repository.remove(null, expectedAddress1.getId());
    }

    @Test(expected=IllegalArgumentException.class)
    public void testRemoveNullId() {
        repository.remove(Address.class, null);
    }

    @Test(expected=IllegalArgumentException.class)
    public void testRemoveNonEntity() {
        repository.remove(NonEntityAddress.class, expectedAddress1.getId());   
    }

    private IDatabaseConnection getDatabaseConnection() throws Exception {
        return (databaseConnection == null) ? new DatabaseConnection(dataSource.getConnection()) : databaseConnection;
    }
    
    private static IDataSet getDataSet() throws Exception {
        return new FlatXmlDataSetBuilder().build(Thread.currentThread().getContextClassLoader().getResourceAsStream(TEST_FILE));
    }
}
