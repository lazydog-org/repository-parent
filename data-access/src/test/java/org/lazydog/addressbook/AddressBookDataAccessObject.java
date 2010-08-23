package org.lazydog.addressbook;

import javax.persistence.Persistence;
import org.lazydog.data.access.AbstractDataAccessObject;


/**
 * Address book data access object.
 *
 * @author  Ron Rickard
 */
public class AddressBookDataAccessObject extends AbstractDataAccessObject {

    public AddressBookDataAccessObject () {
        this.setEntityManager(Persistence.createEntityManagerFactory("AddressBookPU").createEntityManager());
    }
}
