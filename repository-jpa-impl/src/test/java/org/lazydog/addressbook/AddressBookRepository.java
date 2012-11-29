package org.lazydog.addressbook;

import javax.annotation.PostConstruct;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import org.lazydog.repository.jpa.AbstractRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;


/**
 * Address book repository.
 *
 * @author  Ron Rickard
 */
@Repository
@Transactional(propagation = Propagation.MANDATORY)
public class AddressBookRepository extends AbstractRepository {

    @PersistenceContext
    private EntityManager entityManager;

    @PostConstruct
    protected void initialize() {
        this.setEntityManager(this.entityManager);
    }
}
