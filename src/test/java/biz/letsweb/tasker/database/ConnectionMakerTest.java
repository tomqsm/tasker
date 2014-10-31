package biz.letsweb.tasker.database;

import biz.letsweb.tasker.PooledConnectionProduceable;
import javax.sql.PooledConnection;
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
     * Test of getPooledConnection method, of class DerbyPooledConnectionProducer.
     */
    @Test
    public void testGetPooledConnection() {
        PooledConnectionProduceable instance = new DerbyPooledConnectionProducer(new DerbyPooledDataSourceMaker().getClientConnectionPoolDataSource());
        PooledConnection result = instance.getPooledConnection();
        assertThat(result).isNotNull();
    }
    
}
