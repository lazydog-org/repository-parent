package org.lazydog.repository;

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
import org.junit.Test;
import org.lazydog.addressbook.AddressBookDataAccessObject;
import org.lazydog.addressbook.model.Address;
import org.lazydog.addressbook.model.Employee;
import org.lazydog.addressbook.model.State;


/**
 * Abstract repository test.
 * 
 * @author  Ron Rickard
 */
public class AbstractRepositoryTest {

    private static final String ADDRESS_BOOK_SEED = "org/lazydog/addressbook/data/addressbook-seed.xml";
    //private static final String JDBC_DRIVER = "com.mysql.jdbc.Driver";
    //private static final String JDBC_PASSWORD = "tek.book";
    //private static final String JDBC_URL = "jdbc:mysql://localhost:3306/addressbook";
    //private static final String JDBC_USER = "rjrjr";
    private static final String JDBC_DRIVER = "org.apache.derby.jdbc.EmbeddedDriver";
    private static final String JDBC_URL = "jdbc:derby:./target/addressbook";
    private static final String JDBC_USER = "addressbookuser";
    private static final String JDBC_PASSWORD = "addressbookuser";

    private static AddressBookDataAccessObject dao;
    private static DateFormat df = new SimpleDateFormat("MM/dd/yyyy HH:mm:ss.SSS");

    @BeforeClass
    public static void initialize() throws Exception {

        // Ensure the derby.log file is in the target directory.
        System.setProperty("derby.system.home", "./target");
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

        return new DatabaseConnection(DriverManager.getConnection(JDBC_URL, JDBC_USER, JDBC_PASSWORD));
    }

    private static IDataSet getDataSet()
            throws DataSetException {
        return new XmlDataSet(AbstractRepository.class.getClassLoader().getResourceAsStream(ADDRESS_BOOK_SEED));
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
