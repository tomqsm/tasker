package biz.letsweb.tasker.timing;

import biz.letsweb.tasker.NoRecordsInPoolException;
import biz.letsweb.tasker.UnexpectedOrderingException;
import biz.letsweb.tasker.UnsetIdException;
import biz.letsweb.tasker.configuration.ConfigurationProvider;
import biz.letsweb.tasker.databaseconnectivity.DataSourceFactory;
import biz.letsweb.tasker.databaseconnectivity.InitializeDb;
import biz.letsweb.tasker.persistence.dao.ChronicleLineDao;
import biz.letsweb.tasker.persistence.model.ChronicleRecordLine;
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
import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author toks
 */
public class CalculatorTest {

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
        setupDatabase(dataSourceFactory.getDataSource());
        calculator = new Calculator();
    }

    private void setupDatabase(DataSource ds) throws SQLException {
        initializeDb = new InitializeDb(ds);
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
    public void testCalculateDurations() throws NoRecordsInPoolException, UnexpectedOrderingException {
        int rowsAtStart = chronicleDao.findRecordsCount();
        assertThat(rowsAtStart).isEqualTo(0);
        // line 0
        ChronicleRecordLine line0 = new ChronicleRecordLine();
        line0.setTag("work0");
        line0.setDescription("line0 description");
        DateTime dateTime = new DateTime(2014, 11, 24, 0, 0, 0, 0);
        line0.setTimestamp(new Timestamp(dateTime.getMillis()));
        chronicleDao.insertNewRecord(line0);
        int work0Duration = 5;

        ChronicleRecordLine line1 = new ChronicleRecordLine();
        line1.setTag("work1");
        line1.setDescription("line1 description");
        dateTime = new DateTime(2014, 11, 24, 0, 5, 0, 0);
        line1.setTimestamp(new Timestamp(dateTime.getMillis()));
        chronicleDao.insertNewRecord(line1);

        ChronicleRecordLine line2 = new ChronicleRecordLine();
        line2.setTag("work2");
        line2.setDescription("line2 description");
        dateTime = new DateTime(2014, 11, 24, 0, 10, 0, 0);
        line2.setTimestamp(new Timestamp(dateTime.getMillis()));
        chronicleDao.insertNewRecord(line2);

        int rowsAfterAdds = chronicleDao.findRecordsCount();
        assertThat(rowsAfterAdds).isEqualTo(rowsAtStart + 3);

        final List<ChronicleRecordLine> last3Lines = chronicleDao.findNRecordsDescending(3);
        assertThat(last3Lines).hasSize(3);
        final Map<String, Duration> durations = calculator.calculateDurations(last3Lines);
        assertThat(durations.get("work0").getStandardMinutes()).isEqualTo(5);
    }
    
    @Test
    public void calaculateDurationHandlesDescAndAscIdOrderedLists() throws NoRecordsInPoolException{
        int rowsAtStart = chronicleDao.findRecordsCount();
        assertThat(rowsAtStart).isEqualTo(0);
        // line 0
        ChronicleRecordLine line0 = new ChronicleRecordLine();
        line0.setTag("work0");
        line0.setDescription("line0 description");
        DateTime dateTime = new DateTime(2014, 11, 24, 0, 0, 0, 0);
        line0.setTimestamp(new Timestamp(dateTime.getMillis()));
        chronicleDao.insertNewRecord(line0);

        ChronicleRecordLine line1 = new ChronicleRecordLine();
        line1.setTag("work1");
        line1.setDescription("line1 description");
        dateTime = new DateTime(2014, 11, 24, 0, 5, 0, 0);
        line1.setTimestamp(new Timestamp(dateTime.getMillis()));
        chronicleDao.insertNewRecord(line1);
        int work0Duration = 5;

        ChronicleRecordLine line2 = new ChronicleRecordLine();
        line2.setTag("work2");
        line2.setDescription("line2 description");
        dateTime = new DateTime(2014, 11, 24, 0, 10, 0, 0);
        line2.setTimestamp(new Timestamp(dateTime.getMillis()));
        chronicleDao.insertNewRecord(line2);

        int rowsAfterAdds = chronicleDao.findRecordsCount();
        assertThat(rowsAfterAdds).isEqualTo(rowsAtStart + 3);

        List<ChronicleRecordLine> last3Lines = chronicleDao.findNRecordsDescending(3);
        assertThat(last3Lines).hasSize(3);
        Map<String, Duration> durations = calculator.calculateDurations(last3Lines);
        assertThat(durations.get("work0").getStandardMinutes()).isEqualTo(work0Duration);
        last3Lines = chronicleDao.findNRecordsDescending(3);
        Collections.reverse(last3Lines);
        durations = calculator.calculateDurations(last3Lines);
        assertThat(durations.get("work0").getStandardMinutes()).isEqualTo(work0Duration);
    }

}
