package biz.letsweb.tasker.timecalculator;

import biz.letsweb.tasker.databaseconnectivity.DerbyPooledDataSourceFactory;
import biz.letsweb.tasker.persistence.model.ChronicleRecordLine;
import biz.letsweb.tasker.persistence.model.ConsoleViewModel;
import biz.letsweb.tasker.services.ChronicleLineDao;
import java.util.List;
import javax.sql.PooledConnection;
import org.junit.After;
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
public class TimeCalaculatorTest {

  public static final Logger log = LoggerFactory.getLogger(TimeCalaculatorTest.class);

  private ChronicleLineDao chronicleDao;
  private final DerbyPooledDataSourceFactory pooledDataSourceFactory =
      new DerbyPooledDataSourceFactory();
  private final PooledConnection pooledConnection = pooledDataSourceFactory.getPooledConnection();

  public TimeCalaculatorTest() {}

  @BeforeClass
  public static void setUpClass() {}

  @AfterClass
  public static void tearDownClass() {}

  @Before
  public void setUp() {
    chronicleDao = new ChronicleLineDao(pooledConnection);
  }

  @After
  public void tearDown() {}

  /**
   * Test of getDurationsPerTag method, of class TimeCalaculator.
   */
  @Test
  public void testGetDurationsPerTag() {
    List<ChronicleRecordLine> dailyPool = chronicleDao.findTodaysRecords();
    TimeCalaculator timeCalaculator = new TimeCalaculator();
    final List<ConsoleViewModel> durationsPerTag = timeCalaculator.getDurationsPerTag(dailyPool, 3);
    // Assertions.assertThat(durationsPerTag).hasSize(3);
    log.info("{}", durationsPerTag);
  }

}
