package biz.letsweb.tasker.services;

import biz.letsweb.tasker.databaseconnectivity.DerbyPooledDataSourceFactory;
import java.util.Map;
import javax.sql.PooledConnection;
import org.joda.time.Duration;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 * @author Tomasz
 */
public class ChronicleLineDaoTest {

  public ChronicleLineDaoTest() {}

  public static final Logger log = LoggerFactory.getLogger(ChronicleLineDaoTest.class);

  private ChronicleLineDao chronicleDao;
  private final DerbyPooledDataSourceFactory pooledDataSourceFactory =
      new DerbyPooledDataSourceFactory();
  private final PooledConnection pooledConnection = pooledDataSourceFactory.getPooledConnection();

  @BeforeClass
  public static void setUpClass() {}

  @AfterClass
  public static void tearDownClass() {}

  @Before
  public void setUp() {
    chronicleDao = new ChronicleLineDao(pooledConnection);
  }

  /**
   * Test of findLastRecord method, of class ChronicleLineDao.
   */
  @Test
  public void testFindLastRecord() {
    final Map<String, Duration> durations = chronicleDao.findDurationsOfTodaysRecords();
    // assertThat(durations).isEmpty();
    System.out.println(durations.get("breakCoffe").getStandardMinutes());

  }
}
