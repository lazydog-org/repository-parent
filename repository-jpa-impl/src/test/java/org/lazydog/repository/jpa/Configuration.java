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

import javax.enterprise.context.ApplicationScoped;
import javax.enterprise.inject.Produces;

/**
 * Configuration.
 * 
 * @author  Ron Rickard
 */
@ApplicationScoped
public class Configuration {

    private static String persistenceUnitName;
    
    public static void setPersistenceUnitName(final String newPersistenceUnitName) {
        persistenceUnitName = newPersistenceUnitName;
    }
    
    @Produces
    @PersistenceUnitName
    public static String getPersistenceUnitName() {
        return persistenceUnitName;
    }
}
