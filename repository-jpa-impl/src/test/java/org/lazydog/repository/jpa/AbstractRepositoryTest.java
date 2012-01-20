package org.lazydog.repository.jpa;

import javax.persistence.EntityNotFoundException;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.lazydog.addressbook.AddressBookRepository;
import org.lazydog.addressbook.model.Address1;
import org.lazydog.addressbook.model.Address2;
import org.lazydog.repository.Criteria;
import org.lazydog.repository.criterion.ComparisonOperation;
import org.lazydog.repository.criterion.LogicalOperation;

import static org.junit.Assert.*;


/**
 * Abstract repository test.
 * 
 * @author  Ron Rickard
 */
public class AbstractRepositoryTest {

    private static AddressBookRepository repository;
    private static Address1 address1;
    private static Address1 address2;
    private static Address2 address3;
    private static Address1 persistedAddress1;
    private static Address1 persistedAddress2;
    
    @BeforeClass
    public static void initialize() throws Exception {

        // Ensure the derby.log file is in the target directory.
        System.setProperty("derby.system.home", "./target");
        repository = new AddressBookRepository();

        address1 = new Address1();
        address1.setCity("Los Angeles");
        address1.setState("California");
        address1.setStreetAddress("623 Sepulveda Blvd.");
        address1.setZipcode("11111");

        address2 = new Address1();
        address2.setCity("Phoenix");
        address2.setState("Arizona");
        address2.setStreetAddress("690 E. Main St.");
        address2.setZipcode("33333");

        address3 = new Address2();
        address3.setCity("Denver");
        address3.setState("Colorado");
        address3.setStreetAddress("3342 N. Ridgemonth Rd.");
        address3.setZipcode("55555");
    }

    @Before
    public void beforeTest() {
        try {          
            Criteria<Address1> criteria = repository.getCriteria(Address1.class);
            criteria.add(ComparisonOperation.eq("city", address1.getCity()));
            criteria.add(LogicalOperation.and(ComparisonOperation.eq("state", address1.getState())));
            criteria.add(LogicalOperation.and(ComparisonOperation.eq("streetAddress", address1.getStreetAddress())));
            criteria.add(LogicalOperation.and(ComparisonOperation.eq("zipcode", address1.getZipcode())));
            persistedAddress1 = repository.find(Address1.class, criteria);
            if (persistedAddress1 != null) {
                repository.getEntityManager().getTransaction().begin();
                repository.remove(Address1.class, persistedAddress1.getId());
                repository.getEntityManager().getTransaction().commit();
            }
        }
        catch(Exception e) { 
            repository.getEntityManager().getTransaction().rollback();
        }
        
        try {
            
            Criteria<Address1> criteria = repository.getCriteria(Address1.class);
            criteria.add(ComparisonOperation.eq("city", address2.getCity()));
            criteria.add(LogicalOperation.and(ComparisonOperation.eq("state", address2.getState())));
            criteria.add(LogicalOperation.and(ComparisonOperation.eq("streetAddress", address2.getStreetAddress())));
            criteria.add(LogicalOperation.and(ComparisonOperation.eq("zipcode", address2.getZipcode())));
            persistedAddress2 = repository.find(Address1.class, criteria);
            if (persistedAddress2 != null) {
                repository.getEntityManager().getTransaction().begin();
                repository.remove(Address1.class, persistedAddress2.getId());
                repository.getEntityManager().getTransaction().commit();
            }
        }
        catch(Exception e) { 
            repository.getEntityManager().getTransaction().rollback();
        }
    }

    @Test
    public void testFind() {
        repository.getEntityManager().getTransaction().begin();
        persistedAddress1 = repository.persist(address1);
        repository.getEntityManager().getTransaction().commit();
        assertEquals(persistedAddress1, repository.find(Address1.class, persistedAddress1.getId()));
    }

    @Test
    public void testFindNot() {
        assertNull(repository.find(Address1.class, new Integer(1)));
    }

    @Test(expected=IllegalArgumentException.class)
    public void testFindNullClass() {
        repository.getEntityManager().getTransaction().begin();
        persistedAddress1 = repository.persist(address1);
        repository.getEntityManager().getTransaction().commit();
        repository.find(null, persistedAddress1.getId());
    }

    @Test(expected=IllegalArgumentException.class)
    public void testFindNonEntity() {
        repository.getEntityManager().getTransaction().begin();
        persistedAddress1 = repository.persist(address1);
        repository.getEntityManager().getTransaction().commit();
        repository.find(Address2.class, persistedAddress1.getId());
    }

    @Test
    public void testFindByCritera() {
        repository.getEntityManager().getTransaction().begin();
        persistedAddress1 = repository.persist(address1);
        repository.getEntityManager().getTransaction().commit();
        Criteria<Address1> criteria = repository.getCriteria(Address1.class);
        criteria.add(ComparisonOperation.eq("id", persistedAddress1.getId()));
        assertEquals(persistedAddress1, repository.find(Address1.class, criteria));
    }

    @Test
    public void testFindByCriteriaNot() {
        Criteria<Address1> criteria = repository.getCriteria(Address1.class);
        criteria.add(ComparisonOperation.eq("id", new Integer(1)));
        assertNull(repository.find(Address1.class, criteria));
    }

    @Test(expected=IllegalArgumentException.class)
    public void testFindByCriteriaNullClass() {
        repository.getEntityManager().getTransaction().begin();
        persistedAddress1 = repository.persist(address1);
        repository.getEntityManager().getTransaction().commit();
        Criteria<Address1> criteria = repository.getCriteria(Address1.class);
        criteria.add(ComparisonOperation.eq("id", persistedAddress1.getId()));
        repository.find(null, criteria);
    }

    @Test
    public void testGetCriteria() {
        repository.getCriteria(Address1.class);
    }

    @Test(expected=IllegalArgumentException.class)
    public void testGetCriteriaNull() {
        repository.getCriteria(null);
    }

    @Test(expected=IllegalArgumentException.class)
    public void testGetCriteriaNonEntity() {
        repository.getCriteria(Address2.class);
    }

    @Test
    public void testPersist() {
        repository.getEntityManager().getTransaction().begin();
        persistedAddress1 = repository.persist(address1);
        repository.getEntityManager().getTransaction().commit();
        assertEquals(persistedAddress1, address1);
    }

    @Test(expected=IllegalArgumentException.class)
    public void testPersistNull() {
        repository.persist(null);
    }
    
    @Test(expected=IllegalArgumentException.class)
    public void testPersistNonEntity() {
        repository.persist(address3);
    }

    @Test
    public void testRemove() {
        repository.getEntityManager().getTransaction().begin();
        persistedAddress1 = repository.persist(address1);
        repository.getEntityManager().getTransaction().commit();
        assertEquals(persistedAddress1, address1);
        repository.getEntityManager().getTransaction().begin();
        repository.remove(Address1.class, persistedAddress1.getId());
        repository.getEntityManager().getTransaction().commit();
        assertNull(repository.find(Address1.class, persistedAddress1.getId()));
    }

    @Test(expected=EntityNotFoundException.class)
    public void testRemoveNot() {
        repository.getEntityManager().getTransaction().begin();
        persistedAddress1 = repository.persist(address1);
        repository.getEntityManager().getTransaction().commit();
        assertEquals(persistedAddress1, address1);
        repository.getEntityManager().getTransaction().begin();
        repository.remove(Address1.class, persistedAddress1.getId());
        repository.getEntityManager().getTransaction().commit();
        assertNull(repository.find(Address1.class, persistedAddress1.getId()));
        repository.remove(Address1.class, persistedAddress1.getId());
    }

    @Test(expected=IllegalArgumentException.class)
    public void testRemoveNullClass() {
        repository.getEntityManager().getTransaction().begin();
        persistedAddress1 = repository.persist(address1);
        repository.getEntityManager().getTransaction().commit();
        repository.remove(null, persistedAddress1.getId());
    }

    @Test(expected=IllegalArgumentException.class)
    public void testRemoveNullId() {
        repository.remove(Address1.class, null);
    }

    @Test(expected=IllegalArgumentException.class)
    public void testRemoveNonEntity() {
        repository.getEntityManager().getTransaction().begin();
        persistedAddress1 = repository.persist(address1);
        repository.getEntityManager().getTransaction().commit();
        repository.remove(Address2.class, persistedAddress1.getId());    
    }
}
