package org.lazydog.data.access;

import java.sql.DriverManager;
import java.sql.SQLException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import org.dbunit.database.DatabaseConnection;
import org.dbunit.database.IDatabaseConnection;
import org.dbunit.dataset.DataSetException;
import org.dbunit.dataset.IDataSet;
import org.dbunit.dataset.xml.XmlDataSet;
import org.dbunit.operation.DatabaseOperation;
import org.dbunit.DatabaseUnitException;
import org.junit.BeforeClass;
import org.junit.Ignore;
import org.junit.Test;
import org.lazydog.addressbook.AddressBookDataAccessObject;
import org.lazydog.addressbook.model.Address;
import org.lazydog.addressbook.model.Employee;
import org.lazydog.addressbook.model.State;


/**
 *
 * @author  Ron Rickard
 */
public class AbstractDataAccessObjectTest {

    private static final String ADDRESS_BOOK_SEED = "org/lazydog/addressbook/data/addressbook-seed.xml";
    private static final String JDBC_DRIVER = "org.apache.derby.jdbc.EmbeddedDriver";
    private static final String JDBC_URL = "jdbc:derby:addressbook;user=addressbookuser";

    private static AddressBookDataAccessObject dao;
    private static DateFormat df = new SimpleDateFormat("MM/dd/yyyy HH:mm:ss.SSS");

    @BeforeClass
    public static void initialize() throws Exception {

        dao = new AddressBookDataAccessObject();
        setupDatabase();
    }

    private static double duration(Date startTime, Date endTime) {
        return (endTime.getTime() - startTime.getTime()) / 1000d;
    }

    @Test
    public void findListAddress() {

        Date endTime;
        Date startTime;
        List<Address> addresses;

        startTime = new Date();
        addresses = dao.findList(Address.class);
        endTime = new Date();
        System.out.println(addresses.size() + " addresses retrieved in " + duration(startTime, endTime) + " seconds");
        for (Address address : addresses) {
            System.out.println(address);
        }
    }

    @Test
    public void findListEmployee() {

        Date endTime;
        Date startTime;
        List<Employee> employees;

        startTime = new Date();
        employees = dao.findList(Employee.class);
        endTime = new Date();
        System.out.println(employees.size() + " employees retrieved in " + duration(startTime, endTime) + " seconds");
        //for (Employee employee : employees) {
        //    System.out.println(employee);
        //}
    }

    @Test
    public void findListState() {

        Date endTime;
        Date startTime;
        List<State> states;

        startTime = new Date();
        states = dao.findList(State.class);
        endTime = new Date();
        System.out.println(states.size() + " states retrieved in " + duration(startTime, endTime) + " seconds");
    }

    private static IDatabaseConnection getConnection()
            throws ClassNotFoundException, SQLException {

        Class.forName(JDBC_DRIVER);

        return new DatabaseConnection(DriverManager.getConnection(JDBC_URL));
    }

    private static IDataSet getDataSet()
            throws DataSetException {
        return new XmlDataSet(AbstractDataAccessObject.class.getClassLoader().getResourceAsStream(ADDRESS_BOOK_SEED));
    }

    private static void setupDatabase()
            throws ClassNotFoundException, DatabaseUnitException, DataSetException, SQLException {

        // Declare.
        IDatabaseConnection connection;
        IDataSet dataSet;

        // Get the database connection.
        connection = getConnection();

        // Get the data set.
        dataSet = getDataSet();

        try {

            DatabaseOperation.CLEAN_INSERT.execute(connection, dataSet);
         }
        finally {
            connection.close();
         }
    }
}
