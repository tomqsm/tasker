package biz.letsweb.tasker.timing;

import biz.letsweb.tasker.NoRecordsInPoolException;
import biz.letsweb.tasker.UnexpectedOrderingException;
import biz.letsweb.tasker.UninitialisedTablesException;
import biz.letsweb.tasker.configuration.ConfigurationProvider;
import biz.letsweb.tasker.db.DataSourceFactory;
import biz.letsweb.tasker.db.InitializeDb;
import biz.letsweb.tasker.chronicle.dao.ChronicleLineDao;
import biz.letsweb.tasker.chronicle.model.ChronicleLine;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import javax.sql.DataSource;
import org.apache.commons.configuration.XMLConfiguration;
import static org.fest.assertions.Assertions.assertThat;
import org.joda.time.DateTime;
import org.joda.time.Duration;
import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 * @author toks
 */
public class CalculatorTest {

    public static final Logger log = LoggerFactory.getLogger(CalculatorTest.class);

    private Calculator calculator;
    private ChronicleLineDao chronicleDao;
    private InitializeDb initializeDb;

    public CalculatorTest() {
        calculator = new Calculator();
    }

    @Before
    public void setUp() throws SQLException {
        final XMLConfiguration configuration = new ConfigurationProvider("src/test/resources/configuration.xml").getXMLConfiguration();
        final DataSourceFactory dataSourceFactory = new DataSourceFactory(configuration);
        chronicleDao = new ChronicleLineDao(dataSourceFactory.getDataSource());
        setupDatabase(dataSourceFactory.getDataSource(), configuration);
        calculator = new Calculator();
    }

    private void setupDatabase(DataSource ds, XMLConfiguration configuration) throws SQLException {
        initializeDb = new InitializeDb(ds, configuration);
        final InitializeDb.Feedback createTables = initializeDb.createTables();
        if (createTables == InitializeDb.Feedback.TABLES_EXISTED) {
            initializeDb.clearTables();
        }
    }

    @After
    public void tearDown() {
    }

    /**
     * Test of calculateDurations method, of class Calculator.
     */
    @Test
    public void calculatesDurationsAcrossVaryingRecords() throws NoRecordsInPoolException, UnexpectedOrderingException, UninitialisedTablesException {
        int rowsAtStart = chronicleDao.findRecordsCount();
        assertThat(rowsAtStart).isEqualTo(0);
        // line 0
        ChronicleLine line_1 = new ChronicleLine();
        line_1.setTag("work_1");
        line_1.setDescription("line0 description");
        DateTime dateTime = new DateTime(2014, 11, 24, 0, 0, 0, 0);
        line_1.setTimestamp(new Timestamp(dateTime.getMillis()));
        chronicleDao.insertNewRecord(line_1);

        ChronicleLine line0 = new ChronicleLine();
        line0.setTag("work0");
        line0.setDescription("line0 description");
        dateTime = new DateTime(2014, 11, 24, 0, 0, 0, 0);
        line0.setTimestamp(new Timestamp(dateTime.getMillis()));
        chronicleDao.insertNewRecord(line0);

        ChronicleLine line1 = new ChronicleLine();
        line1.setTag("work1");
        line1.setDescription("line1 description");
        dateTime = new DateTime(2014, 11, 24, 0, 6, 0, 0);
        line1.setTimestamp(new Timestamp(dateTime.getMillis()));
        chronicleDao.insertNewRecord(line1);
        int work0DurationMinutes = 6;

        ChronicleLine line2 = new ChronicleLine();
        line2.setTag("work2");
        line2.setDescription("line2 description");
        dateTime = new DateTime(2014, 11, 24, 0, 10, 0, 0);
        line2.setTimestamp(new Timestamp(dateTime.getMillis()));
        chronicleDao.insertNewRecord(line2);
        int work1DurationMinutes = 4;

        int rowsAfterAdds = chronicleDao.findRecordsCount();
        assertThat(rowsAfterAdds).isEqualTo(rowsAtStart + 4);

        final List<ChronicleLine> last3Lines = chronicleDao.findLastNRecordsUpwards(3);
        assertThat(last3Lines).hasSize(3);
        assertThat(line2).isEqualTo(last3Lines.get(0));

        final List<ChronicleLine> durations = calculator.calculateDurations(last3Lines);
        assertThat(durations.get(0).getTotalDuration().getStandardMinutes()).isEqualTo(work0DurationMinutes);
        assertThat(durations.get(1).getTotalDuration().getStandardMinutes()).isEqualTo(work1DurationMinutes);
    }

    /**
     * When records' tags repeat in the record pool then duration is taken and
     * summed up per each of them.
     */
    @Test
    public void calculatesSumDurationsAccrossRepeatingRecords() throws NoRecordsInPoolException, UnexpectedOrderingException, UninitialisedTablesException {
        int rowsAtStart = chronicleDao.findRecordsCount();
        assertThat(rowsAtStart).isEqualTo(0);
        // line 0
        ChronicleLine line_1 = new ChronicleLine();
        line_1.setTag("work0");
        line_1.setDescription("line0 description");
        DateTime dateTime = new DateTime(2014, 11, 24, 0, 0, 0, 0);
        line_1.setTimestamp(new Timestamp(dateTime.getMillis()));
        chronicleDao.insertNewRecord(line_1);

        ChronicleLine line0 = new ChronicleLine();
        line0.setTag("work0");
        line0.setDescription("line0 description");
        dateTime = new DateTime(2014, 11, 24, 0, 0, 0, 0);
        line0.setTimestamp(new Timestamp(dateTime.getMillis()));
        chronicleDao.insertNewRecord(line0);

        ChronicleLine line1 = new ChronicleLine();
        line1.setTag("work1");
        line1.setDescription("line1 description");
        dateTime = new DateTime(2014, 11, 24, 0, 6, 0, 0);
        line1.setTimestamp(new Timestamp(dateTime.getMillis()));
        chronicleDao.insertNewRecord(line1);
        int work0DurationMinutes = 6;

        ChronicleLine line2 = new ChronicleLine();
        line2.setTag("work2");
        line2.setDescription("line2 description");
        dateTime = new DateTime(2014, 11, 24, 0, 10, 0, 0);
        line2.setTimestamp(new Timestamp(dateTime.getMillis()));
        chronicleDao.insertNewRecord(line2);
        int work1DurationMinutes = 4;

        ChronicleLine line0a = new ChronicleLine();
        line0a.setTag("work0");
        line0a.setDescription("line2 description");
        dateTime = new DateTime(2014, 11, 24, 0, 20, 0, 0);
        line0a.setTimestamp(new Timestamp(dateTime.getMillis()));
        chronicleDao.insertNewRecord(line0a);
        int work0aDurationMinutes = work0DurationMinutes + 11;

        ChronicleLine line1a = new ChronicleLine();
        line1a.setTag("work1");
        line1a.setDescription("line2 description");
        dateTime = new DateTime(2014, 11, 24, 0, 31, 0, 0);
        line1a.setTimestamp(new Timestamp(dateTime.getMillis()));
        chronicleDao.insertNewRecord(line1a);
        int work1aDurationMinutes = work1DurationMinutes + 11;

        int rowsAfterAdds = chronicleDao.findRecordsCount();
        assertThat(rowsAfterAdds).isEqualTo(rowsAtStart + 6);

        final List<ChronicleLine> last3Lines = chronicleDao.findLastNRecordsUpwards(5);
        assertThat(last3Lines).hasSize(5);

        final List<ChronicleLine> durations = calculator.calculateDurations(last3Lines);
        log.info("{}", durations.get(3));
        assertThat(durations.get(3).getTotalDuration().getStandardMinutes()).isEqualTo(work0aDurationMinutes);
    }
    @Rule
    public ExpectedException thrown = ExpectedException.none();

    /**
     * Check that when record pool is less than all records today, calculation
     * is done disregarding remaining records from today. That wouldn't make
     * much sense though.
     */
    @Test
    public void calculatesDurationOnlyWithinReceivedCollection() throws NoRecordsInPoolException, UninitialisedTablesException {
        int rowsAtStart = chronicleDao.findRecordsCount();
        assertThat(rowsAtStart).isEqualTo(0);
        // line 0
        ChronicleLine line_1 = new ChronicleLine();
        line_1.setTag("work_1");
        line_1.setDescription("line0 description");
        DateTime dateTime = new DateTime(2014, 11, 23, 23, 59, 0, 0);
        line_1.setTimestamp(new Timestamp(dateTime.getMillis()));
        chronicleDao.insertNewRecord(line_1);

        ChronicleLine line0 = new ChronicleLine();
        line0.setTag("work0");
        line0.setDescription("line0 description");
        dateTime = new DateTime(2014, 11, 24, 0, 0, 0, 0);
        line0.setTimestamp(new Timestamp(dateTime.getMillis()));
        chronicleDao.insertNewRecord(line0);

        ChronicleLine line1 = new ChronicleLine();
        line1.setTag("work1");
        line1.setDescription("line1 description");
        dateTime = new DateTime(2014, 11, 24, 0, 6, 0, 0);
        line1.setTimestamp(new Timestamp(dateTime.getMillis()));
        chronicleDao.insertNewRecord(line1);
        int work0DurationMinutes = 6;

        ChronicleLine line2 = new ChronicleLine();
        line2.setTag("work2");
        line2.setDescription("line2 description");
        dateTime = new DateTime(2014, 11, 24, 0, 10, 0, 0);
        line2.setTimestamp(new Timestamp(dateTime.getMillis()));
        chronicleDao.insertNewRecord(line2);
        int work1DurationMinutes = 4;

        int rowsAfterAdds = chronicleDao.findRecordsCount();
        assertThat(rowsAfterAdds).isEqualTo(rowsAtStart + 4);

        final List<ChronicleLine> last3Lines = chronicleDao.findLastNRecordsUpwards(3);
        assertThat(last3Lines).hasSize(3);
        assertThat(line2).isEqualTo(last3Lines.get(0));

        final List<ChronicleLine> durations = calculator.calculateDurations(last3Lines);
//        thrown.expect(NullPointerException.class);
        assertThat(durations.get(0).getTotalDuration().getStandardMinutes()).isEqualTo(work0DurationMinutes);

    }

    @Test
    public void calaculateDurationHandlesDescAndAscIdOrderedLists() throws NoRecordsInPoolException, UninitialisedTablesException {
        int rowsAtStart = chronicleDao.findRecordsCount();
        assertThat(rowsAtStart).isEqualTo(0);
        // line 0
        ChronicleLine line0 = new ChronicleLine();
        line0.setTag("work0");
        line0.setDescription("line0 description");
        DateTime dateTime = new DateTime(2014, 11, 24, 0, 0, 0, 0);
        line0.setTimestamp(new Timestamp(dateTime.getMillis()));
        chronicleDao.insertNewRecord(line0);

        ChronicleLine line1 = new ChronicleLine();
        line1.setTag("work1");
        line1.setDescription("line1 description");
        dateTime = new DateTime(2014, 11, 24, 0, 5, 0, 0);
        line1.setTimestamp(new Timestamp(dateTime.getMillis()));
        chronicleDao.insertNewRecord(line1);
        int work0Duration = 5;

        ChronicleLine line2 = new ChronicleLine();
        line2.setTag("work2");
        line2.setDescription("line2 description");
        dateTime = new DateTime(2014, 11, 24, 0, 10, 0, 0);
        line2.setTimestamp(new Timestamp(dateTime.getMillis()));
        chronicleDao.insertNewRecord(line2);

        int rowsAfterAdds = chronicleDao.findRecordsCount();
        assertThat(rowsAfterAdds).isEqualTo(rowsAtStart + 3);

        List<ChronicleLine> last3Lines = chronicleDao.findLastNRecordsUpwards(3);
        assertThat(last3Lines).hasSize(3);
        List<ChronicleLine> durations = calculator.calculateDurations(last3Lines);
        assertThat(durations.get(0).getTotalDuration().getStandardMinutes()).isEqualTo(work0Duration);
        last3Lines = chronicleDao.findLastNRecordsUpwards(3);
        Collections.reverse(last3Lines);
        durations = calculator.calculateDurations(last3Lines);
        assertThat(durations.get(0).getTotalDuration().getStandardMinutes()).isEqualTo(work0Duration);
    }

}
