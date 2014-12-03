package biz.letsweb.tasker;

import biz.letsweb.tasker.configuration.ConfigurationProvider;
import biz.letsweb.tasker.databaseconnectivity.DataSourceFactory;
import biz.letsweb.tasker.databaseconnectivity.InitializeDb;
import biz.letsweb.tasker.persistence.dao.ChronicleLineDao;
import biz.letsweb.tasker.persistence.model.ChronicleLine;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.List;
import java.util.Map;
import org.apache.commons.configuration.XMLConfiguration;
import static org.fest.assertions.Assertions.assertThat;
import org.joda.time.DateTime;
import org.joda.time.Duration;
import org.junit.After;
import org.junit.Before;
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
    private InitializeDb initializeDb;

    @Before
    public void setUp() throws SQLException {
        final XMLConfiguration configuration = new ConfigurationProvider("src/test/resources/configuration.xml").getXMLConfiguration();
        final DataSourceFactory dataSourceFactory = new DataSourceFactory(configuration);
        chronicleDao = new ChronicleLineDao(dataSourceFactory.getDataSource());
        initializeDb = new InitializeDb(dataSourceFactory.getDataSource());
        final InitializeDb.Feedback createTables = initializeDb.createTables();
        if (createTables == InitializeDb.Feedback.TABLES_EXISTED) {
            initializeDb.clearTables();
        }
        presenter = new ConsolePresenter();
    }

    @After
    public void tearDown() {
    }

    @Test
    public void addsThreeRecordsAndFindsThenAsLastThree() throws NoRecordsInPoolException, UnsetIdException {
        int rowsAtStart = chronicleDao.findRecordsCount();
        ChronicleLine line0 = new ChronicleLine();
        line0.setTag("work0");
        ChronicleLine line1 = new ChronicleLine();
        line1.setTag("work1");
        ChronicleLine line2 = new ChronicleLine();
        line2.setTag("work2");
        chronicleDao.insertNewRecord(line0);
        chronicleDao.insertNewRecord(line1);
        chronicleDao.insertNewRecord(line2);

        final List<ChronicleLine> last3Lines = chronicleDao.findLastNRecordsUpwards(3);
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
        final InitializeDb.Feedback createTables = initializeDb.createTables();
        if (createTables == InitializeDb.Feedback.TABLES_EXISTED) {
            initializeDb.clearTables();
        }
        thrown.expect(NoRecordsInPoolException.class);
        presenter.displayDurationOfNRecord(chronicleDao.findLastNRecordsDownwards(5));
    }

    @Test
    public void testThatItOrdersRecentSummative() throws NoRecordsInPoolException, SQLException {
        thrown.expect(NoRecordsInPoolException.class);
        final Map<String, Duration> sums = presenter.displayDurationSummativePerTag(chronicleDao.findAllRecords());
    }

    @Test
    public void testThatItOrdersRecentSummativeDaily() throws NoRecordsInPoolException, SQLException {
        ChronicleLine line0 = new ChronicleLine();
        line0.setTag("work0");
    }

    @Test
    public void whenThereAreNRecordsForTodayItReturnsNRecords() throws NoRecordsInPoolException, UnsetIdException {
        DateTime dateTime = new DateTime();
        int rowsAtStart = chronicleDao.findRecordsCount();
        ChronicleLine line0 = new ChronicleLine();
        line0.setTag("work0");
        line0.setDescription("line0 description");
        dateTime = dateTime.withDate(2014, 11, 24);
        dateTime = dateTime.withHourOfDay(0);
        dateTime = dateTime.withMinuteOfHour(0);
        dateTime = dateTime.withSecondOfMinute(0);
        dateTime = dateTime.withMillisOfSecond(1);
        line0.setTimestamp(new Timestamp(dateTime.getMillis()));
        ChronicleLine line1 = new ChronicleLine();
        line1.setTag("work1");
        ChronicleLine line2 = new ChronicleLine();
        line2.setTag("work2");
        chronicleDao.insertNewRecord(line0);
        chronicleDao.insertNewRecord(line1);
        chronicleDao.insertNewRecord(line2);
        int rowsAfterAdds = chronicleDao.findRecordsCount();
        assertThat(rowsAfterAdds).isEqualTo(rowsAtStart + 3);

        final List<ChronicleLine> last3Lines = chronicleDao.findLastNRecordsUpwards(3);
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
