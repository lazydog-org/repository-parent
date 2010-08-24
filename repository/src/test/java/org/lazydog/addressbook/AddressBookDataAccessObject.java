package org.lazydog.addressbook;

import javax.persistence.Persistence;
import org.lazydog.repository.AbstractRepository;


/**
 * Address book data access object.
 *
 * @author  Ron Rickard
 */
public class AddressBookDataAccessObject extends AbstractRepository {

    public AddressBookDataAccessObject () {
        this.setEntityManager(Persistence.createEntityManagerFactory("AddressBookPU").createEntityManager());
    }
}
