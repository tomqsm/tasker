package biz.letsweb.tasker.database;

import java.sql.SQLException;
import javax.sql.PooledConnection;
import org.apache.derby.jdbc.ClientConnectionPoolDataSource;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.fest.assertions.Assertions.assertThat;

/**
 *
 * @author toks
 */
public class ConnectionMakerTest {

    public ConnectionMakerTest() {
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
     * Test of getPooledConnection method, of class
     * DerbyPooledConnectionProducer.
     */
    @Test
    public void testGetPooledConnection() throws SQLException {
        final DerbyPooledDataSourceMaker derbyPooledDataSourceMaker = new DerbyPooledDataSourceMaker();
        final ClientConnectionPoolDataSource clientConnectionPoolDataSource = derbyPooledDataSourceMaker.getClientConnectionPoolDataSource();
        final PooledConnection pooledConnection = clientConnectionPoolDataSource.getPooledConnection();

        assertThat(pooledConnection).isNotNull();
    }
    
    @Test
    public void findsStringOfDigits(){
        
    }

}
