package biz.letsweb.tasker;

import biz.letsweb.tasker.databaseconnectivity.DataSourcePrepare;
import biz.letsweb.tasker.databaseconnectivity.InitializeDb;
import biz.letsweb.tasker.persistence.dao.ChronicleLineDao;
import biz.letsweb.tasker.persistence.model.ChronicleRecordLine;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.List;
import java.util.Map;
import static org.fest.assertions.Assertions.assertThat;
import org.joda.time.DateTime;
import org.joda.time.Duration;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

/**
 *
 * @author Tomasz
 */
public class ConsolePresenterTest {

  private ConsolePresenter presenter;
  private ChronicleLineDao chronicleDao;

  @BeforeClass
  public static void setUpClass() {}

  @AfterClass
  public static void tearDownClass() {}

  @Before
  public void setUp() {
    presenter = new ConsolePresenter();
    chronicleDao =
        new ChronicleLineDao(new DataSourcePrepare(DataSourcePrepare.Type.CLIENT).getDataSource());
  }

  @After
  public void tearDown() {}

  @Test
  public void addsThreeRecordsAndFindsThenAsLastThree() throws NoRecordsInPoolException {
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

  @Rule
  public ExpectedException thrown = ExpectedException.none();

  @Test
  public void whenRecordPoolIsEmptyExceptionsIsThrown() throws NoRecordsInPoolException,
      SQLException {
    InitializeDb initializeDb = new InitializeDb();
    initializeDb.createTables();
    initializeDb.clearTables();
    thrown.expect(NoRecordsInPoolException.class);
    presenter.displayDurationOfNRecord(chronicleDao.findLastNRecords(5));
  }

  @Test
  public void testThatItOrdersRecentSummative() throws NoRecordsInPoolException, SQLException {
    InitializeDb initializeDb = new InitializeDb();
    initializeDb.createTables();
    initializeDb.clearTables();
    thrown.expect(NoRecordsInPoolException.class);
    final Map<String, Duration> sums =
        presenter.displayDurationSummativePerTag(chronicleDao.findAllRecords());
  }

  @Test
  public void testThatItOrdersRecentSummativeDaily() throws NoRecordsInPoolException, SQLException {
    ChronicleRecordLine line0 = new ChronicleRecordLine();
    line0.setTag("work0");
  }

  @Test
  public void whenThereAreNRecordsForTodayItReturnsNRecords() throws NoRecordsInPoolException {
    DateTime dateTime = new DateTime();
    int rowsAtStart = chronicleDao.findRecordsCount();
    ChronicleRecordLine line0 = new ChronicleRecordLine();
    line0.setTag("work0");
    line0.setDescription("line0 description");
    dateTime = dateTime.withDate(2014, 11, 24);
    dateTime = dateTime.withHourOfDay(0);
    dateTime = dateTime.withMinuteOfHour(0);
    dateTime = dateTime.withSecondOfMinute(0);
    dateTime = dateTime.withMillisOfSecond(1);
    line0.setTimestamp(new Timestamp(dateTime.getMillis()));
    ChronicleRecordLine line1 = new ChronicleRecordLine();
    line1.setTag("work1");
    ChronicleRecordLine line2 = new ChronicleRecordLine();
    line2.setTag("work2");
    chronicleDao.insertNewRecord(line0);
    chronicleDao.insertNewRecord(line1);
    chronicleDao.insertNewRecord(line2);
    int rowsAfterAdds = chronicleDao.findRecordsCount();
    assertThat(rowsAfterAdds).isEqualTo(rowsAtStart + 3);



    final List<ChronicleRecordLine> last3Lines = chronicleDao.findLastFirstNRecords(3);
    line2 = last3Lines.get(0);
    line1 = last3Lines.get(1);
    line0 = last3Lines.get(2);
    System.out.println(line0);
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

}
