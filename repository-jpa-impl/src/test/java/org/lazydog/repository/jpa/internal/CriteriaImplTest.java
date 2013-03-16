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

import static org.junit.Assert.assertEquals;
import org.junit.Test;
import org.lazydog.addressbook.model.Address;
import org.lazydog.addressbook.model.Company;
import org.lazydog.repository.criterion.Comparison;
import org.lazydog.repository.criterion.Logical;

/**
 * Criteria implemented using the Java Persistence API test.
 * 
 * @author  Ron Rickard
 */
public class CriteriaImplTest {

    @Test
    public void testAutoJoin() {
        String expectedQueryLanguageString = "SELECT DISTINCT company FROM Company company JOIN company.departments departments WHERE departments.name LIKE :param1";
        CriteriaImpl<Company> criteriaImpl = new CriteriaImpl<Company>(Company.class);
        criteriaImpl.add(Comparison.like("departments.name", "%Name 10%"));
        assertEquals(expectedQueryLanguageString, criteriaImpl.getQueryLanguageString());
    }
   
    @Test
    public void testLogical() {
        String expectedQueryLanguageString = "SELECT address FROM Address address WHERE address.city = :param1 AND address.state = :param2";
        CriteriaImpl<Address> criteriaImpl = new CriteriaImpl<Address>(Address.class);
        criteriaImpl.add(Comparison.eq("city", "Phoenix"));
        criteriaImpl.add(Logical.and(Comparison.eq("state", "AZ")));

        assertEquals(expectedQueryLanguageString, criteriaImpl.getQueryLanguageString());
    }
   
    @Test
    public void testNoAutoJoin() {
        String expectedQueryLanguageString = "SELECT company FROM Company company WHERE company.name LIKE :param1";
        CriteriaImpl<Company> criteriaImpl = new CriteriaImpl<Company>(Company.class);
        criteriaImpl.add(Comparison.like("name", "%Name 10%"));
        assertEquals(expectedQueryLanguageString, criteriaImpl.getQueryLanguageString());
    }
}
