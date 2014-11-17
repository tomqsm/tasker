package biz.letsweb.tasker.databaseconnectivity;

import biz.letsweb.tasker.databaseconnectivity.DerbyPooledDataSourceMaker;
import org.apache.derby.jdbc.ClientConnectionPoolDataSource;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.fest.assertions.Assertions.assertThat;
//import static org.fest.assertions.api.Assertions.atIndex; // for List assertion
//import static org.fest.assertions.api.Assertions.entry;  // for Map assertion
//import static org.fest.assertions.api.Assertions.extractProperty; // for Iterable/Array assertion
//import static org.fest.assertions.api.Assertions.fail; // use when making exception tests
//import static org.fest.assertions.api.Assertions.failBecauseExceptionWasNotThrown; // idem
//import static org.fest.assertions.api.Assertions.filter; // for Iterable/Array assertion
//import static org.fest.assertions.api.Assertions.offset; // for floating number assertion
//import static org.fest.assertions.api.Assertions.anyOf; // use with Condition

/**
 *
 * @author toks
 */
public class DataSourceMakerTest {
    
    public DataSourceMakerTest() {
    }
    
    @BeforeClass
    public static void setUpClass() {
    }
    
    @AfterClass
    public static void tearDownClass() {
    }
    
    @Before
    public void setUp() {
    }
    
    @After
    public void tearDown() {
    }

    /**
     * Test of getClientConnectionPoolDataSource method, of class DerbyPooledDataSourceMaker.
     */
    @Test
    public void testGetClientConnectionPoolDataSource() {
        DerbyPooledDataSourceMaker instance = new DerbyPooledDataSourceMaker();
        ClientConnectionPoolDataSource result = instance.getClientConnectionPoolDataSource();
        assertThat(result).isNotNull();
    }
    
}
