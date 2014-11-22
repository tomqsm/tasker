package biz.letsweb.tasker;

import biz.letsweb.tasker.databaseconnectivity.DerbyPooledDataSourceFactory;
import biz.letsweb.tasker.persistence.model.ChronicleRecordLine;
import biz.letsweb.tasker.services.ChronicleLineDao;
import java.util.List;
import java.util.Map;
import javax.sql.PooledConnection;
import static org.fest.assertions.Assertions.assertThat;
import org.joda.time.Duration;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

/**
 *
 * @author Tomasz
 */
public class ConsolePresenterTest {

  private ConsolePresenter presenter;
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
    presenter = new ConsolePresenter();
    chronicleDao = new ChronicleLineDao(pooledConnection);
  }

  @After
  public void tearDown() {}

  @Test
  public void testSomeMethod() {
    int rowsAtStart = chronicleDao.findRecordsCount();
    ChronicleRecordLine line0 = new ChronicleRecordLine();
    line0.setTag("work0");
    ChronicleRecordLine line1 = new ChronicleRecordLine();
    line1.setTag("work1");
    ChronicleRecordLine line2 = new ChronicleRecordLine();
    line2.setTag("work2");
    chronicleDao.insertNewRecord(line0);
    chronicleDao.insertNewRecord(line1);
    chronicleDao.insertNewRecord(line2);

    final List<ChronicleRecordLine> last3Lines = chronicleDao.findLastFirstNRecords(3);
    line2 = last3Lines.get(0);
    line1 = last3Lines.get(1);
    line0 = last3Lines.get(2);
    assertThat(line2).isNotNull();
    assertThat(line2.getTag()).isEqualTo("work2");

    presenter.displayDurationOfNRecord(last3Lines);

    System.out.println(line2);
    chronicleDao.deleteRecord(line0);
    chronicleDao.deleteRecord(line1);
    chronicleDao.deleteRecord(line2);
    int rowsAtEnd = chronicleDao.findRecordsCount();
    assertThat(rowsAtStart).isEqualTo(rowsAtEnd);
  }

  @Test
  public void testThatItOrdersRecentAtBottom() {
    int rowsAtStart = chronicleDao.findRecordsCount();

    presenter.displayDurationOfNRecord(chronicleDao.findLastNRecords(5));

    int rowsAtEnd = chronicleDao.findRecordsCount();
    assertThat(rowsAtStart).isEqualTo(rowsAtEnd);
  }

  @Test
  public void testThatItOrdersRecentSummative() {
    final Map<String, Duration> sums =
        presenter.displayDurationSummativePerTag(chronicleDao.findAllRecords());
    System.out.println(sums.get("work").getStandardMinutes());
  }

}
