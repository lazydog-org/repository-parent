package org.lazydog.repository.jpa;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLNonTransientConnectionException;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.dbunit.database.DatabaseConnection;
import org.dbunit.database.IDatabaseConnection;
import org.dbunit.dataset.IDataSet;
import org.dbunit.dataset.xml.FlatXmlDataSetBuilder;
import org.dbunit.operation.DatabaseOperation;
import org.junit.After;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.lazydog.addressbook.AddressBookRepository;
import org.lazydog.addressbook.model.Address;
import org.lazydog.addressbook.model.Company;
import org.lazydog.addressbook.model.Department;
import org.lazydog.addressbook.model.Employee;
import org.lazydog.addressbook.model.Phone;
import org.lazydog.repository.Criteria;
import org.lazydog.repository.criterion.Comparison;


/**
 * Performance test.
 * 
 * @author  Ron Rickard
 */
public class PerformanceTest {

    private static final String TEST_FILE = "bigdataset.xml";
    private static IDatabaseConnection connection;
    private static AddressBookRepository repository;

    @BeforeClass
    public static void beforeClass() throws Exception {

        // Ensure the derby.log file is in the target directory.
        System.setProperty("derby.system.home", "./target");
    }

    @Before
    public void beforeTest() throws Exception {

        repository = new AddressBookRepository();
        
        // Get the database connection.
        beginTransaction();
        connection = new DatabaseConnection(repository.getEntityManager().unwrap(Connection.class));
        commitTransaction();
        
        // Refresh the database with the dataset.
        DatabaseOperation.CLEAN_INSERT.execute(connection, getDataSet());
    }
    
    @After
    public void afterTest() throws Exception {

        // Close the database connection.
        connection.close();
        
        // Close the entity manager factory.
        repository.getEntityManager().getEntityManagerFactory().close();
        
        try {
            
            // Drop the addressbook database.
            DriverManager.getConnection("jdbc:derby:memory:./target/addressbook;drop=true");
        }
        catch(SQLNonTransientConnectionException e) {
            // Ignore.
        }
    }

   //@Test
    public void findListAddress() {
        Date startDate = new Date();
        List<Address> addresses = repository.findList(Address.class);
        Date endDate = new Date();
        System.out.println("fetched " + addresses.size() + " addresses in " + (endDate.getTime() - startDate.getTime()) + "ms");
    }
     
    //@Test
    public void findListCompany() {
        Date startDate = new Date();
        List<Company> companies = repository.findList(Company.class);
        Date endDate = new Date();
        System.out.println("fetched " + companies.size() + " companies in " + (endDate.getTime() - startDate.getTime()) + "ms");
    }
              
    //@Test
    public void findListDepartment() {
        Date startDate = new Date();
        List<Department> departments = repository.findList(Department.class);
        Date endDate = new Date();
        System.out.println("fetched " + departments.size() + " departments in " + (endDate.getTime() - startDate.getTime()) + "ms");
    }
     
    //@Test
    public void findListEmployee() {
        Date startDate = new Date();
        List<Employee> employees = repository.findList(Employee.class);
        Date endDate = new Date();
        System.out.println("fetched " + employees.size() + " employees in " + (endDate.getTime() - startDate.getTime()) + "ms");
    }
          
    //@Test
    public void findListPhone() {
        Date startDate = new Date();
        List<Phone> phones = repository.findList(Phone.class);
        Date endDate = new Date();
        System.out.println("fetched " + phones.size() + " phones in " + (endDate.getTime() - startDate.getTime()) + "ms");
    }
         
    @Test
    public void findCompanyCriteria() {
        Date startDate = new Date();
        Criteria<Company> criteria = repository.getCriteria(Company.class);
        criteria.add(Comparison.like("departments.employees.phones.number", "%Number 10%"));
        List<Company> companies = repository.findList(Company.class, criteria);
        Date endDate = new Date();
        System.out.println("fetched " + companies.size() + " companies in " + (endDate.getTime() - startDate.getTime()) + "ms");
    }
    
    @Test
    public void findCompanyQuery() {
        Date startDate = new Date();
        String queryLanguageString = "SELECT DISTINCT company FROM Company company JOIN company.departments departments JOIN departments.employees employees JOIN employees.phones phones WHERE phones.number LIKE :param1";
        Map<String,Object> queryParameters = new HashMap<String,Object>();
        queryParameters.put("param1", "%Number 10%");
        List<Company> companies = ((AbstractRepository)repository).findList(Company.class, queryLanguageString, queryParameters, new HashMap<Object,String>());
        Date endDate = new Date();
        System.out.println("fetched " + companies.size() + " companies in " + (endDate.getTime() - startDate.getTime()) + "ms");
    }

    private static void beginTransaction() {
        repository.getEntityManager().getTransaction().begin();
    }
    
    private static void commitTransaction() {
        repository.getEntityManager().getTransaction().commit();
    }
    
    private static IDataSet getDataSet() throws Exception {
        return new FlatXmlDataSetBuilder().build(Thread.currentThread().getContextClassLoader().getResourceAsStream(TEST_FILE));
    }
}
