package biz.letsweb.tasker.timecalculator;

import biz.letsweb.tasker.databaseconnectivity.DataSourceFactory;
import biz.letsweb.tasker.persistence.model.ChronicleRecordLine;
import biz.letsweb.tasker.model.ConsoleViewModel;
import biz.letsweb.tasker.persistence.dao.ChronicleLineDao;
import java.util.List;
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

  public TimeCalaculatorTest() {}

  @BeforeClass
  public static void setUpClass() {}

  @AfterClass
  public static void tearDownClass() {}

  @Before
  public void setUp() {
    chronicleDao = new ChronicleLineDao(new DataSourceFactory(DataSourceFactory.Type.CLIENT).getDataSource());
  }

  @After
  public void tearDown() {}

  /**
   * Test of getDurationsPerTag method, of class TimeCalaculator.
   */
//  @Test
  public void testGetDurationsPerTag() {
    List<ChronicleRecordLine> dailyPool = chronicleDao.findTodaysRecords();
    TimeCalaculator timeCalaculator = new TimeCalaculator();
    final List<ConsoleViewModel> durationsPerTag = timeCalaculator.getDurationsPerTag(dailyPool, 3);
    // Assertions.assertThat(durationsPerTag).hasSize(3);
    log.info("{}", durationsPerTag);
  }

}
