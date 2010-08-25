package org.lazydog.addressbook;

import javax.persistence.Persistence;
import org.lazydog.repository.AbstractRepository;


/**
 * Address book repository.
 *
 * @author  Ron Rickard
 */
public class AddressBookRepository extends AbstractRepository {

    public AddressBookRepository () {
        this.setEntityManager(Persistence.createEntityManagerFactory("AddressBookPU").createEntityManager());
    }
}
