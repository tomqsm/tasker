package biz.letsweb.tasker.dao;

import biz.letsweb.tasker.NoRecordsInPoolException;
import biz.letsweb.tasker.UnsetIdException;
import biz.letsweb.tasker.configuration.ConfigurationProvider;
import biz.letsweb.tasker.databaseconnectivity.DataSourceFactory;
import biz.letsweb.tasker.databaseconnectivity.InitializeDb;
import biz.letsweb.tasker.model.DependencyModel;
import biz.letsweb.tasker.persistence.dao.ChronicleLineDao;
import biz.letsweb.tasker.persistence.model.ChronicleLine;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.List;
import javax.sql.DataSource;
import org.apache.commons.configuration.XMLConfiguration;
import static org.fest.assertions.Assertions.assertThat;
import org.joda.time.DateTime;
import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 * @author Tomasz
 */
public class ChronicleLineDaoTest {

    public ChronicleLineDaoTest() {
    }

    public static final Logger log = LoggerFactory.getLogger(ChronicleLineDaoTest.class);

    private ChronicleLineDao chronicleDao;
    private InitializeDb initializeDb;

    @Before
    public void setUp() throws SQLException {
        final XMLConfiguration configuration = new ConfigurationProvider("src/test/resources/configuration.xml").getXMLConfiguration();
        final DataSourceFactory dataSourceFactory = new DataSourceFactory(configuration);
        chronicleDao = new ChronicleLineDao(dataSourceFactory.getDataSource());
        setupDatabase(dataSourceFactory.getDataSource());
    }

    @After
    public void tearDown() throws SQLException {
    }

    private void setupDatabase(DataSource ds) throws SQLException {
        initializeDb = new InitializeDb(ds);
        final InitializeDb.Feedback createTables = initializeDb.createTables();
        if (createTables == InitializeDb.Feedback.TABLES_EXISTED) {
            initializeDb.clearTables();
        }
    }

    @Test
    public void returnsEmptyListWhenNoRecordsPerDay() throws NoRecordsInPoolException, SQLException {
        final List<ChronicleLine> allRecords = chronicleDao.findAllRecords();
        assertThat(allRecords).isNotNull();
        assertThat(allRecords).isEmpty();
    }
    @Rule
    public ExpectedException thrown = ExpectedException.none();

    @Test
    public void cantDeleteRecordWithIdNotSet() throws UnsetIdException {
        ChronicleLine line0 = new ChronicleLine();
        line0.setTag("work0");
        chronicleDao.insertNewRecord(line0);
        thrown.expect(UnsetIdException.class);
        chronicleDao.deleteRecord(line0);
    }

    @Test
    public void findNRecordsDescendingIsHavingExactNumberDescentingOrdered() throws NoRecordsInPoolException {
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

        ChronicleLine line2 = new ChronicleLine();
        line2.setTag("work2");
        line2.setDescription("line2 description");
        dateTime = new DateTime(2014, 11, 24, 0, 10, 0, 0);
        line2.setTimestamp(new Timestamp(dateTime.getMillis()));
        chronicleDao.insertNewRecord(line2);

        int rowsAfterAdds = chronicleDao.findRecordsCount();
        assertThat(rowsAfterAdds).isEqualTo(rowsAtStart + 4);

        final List<ChronicleLine> last3Lines = chronicleDao.findLastNRecordsUpwards(3);
        assertThat(last3Lines).hasSize(3);
        assertThat(line2).isEqualTo(last3Lines.get(0));
        assertThat(line0).isEqualTo(last3Lines.get(2));
    }

    @Test
    public void findNNamingRecordsNonWorkOrNonBreak() throws NoRecordsInPoolException {
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

        ChronicleLine line2 = new ChronicleLine();
        line2.setTag("work2");
        line2.setDescription("line2 description");
        dateTime = new DateTime(2014, 11, 24, 0, 10, 0, 0);
        line2.setTimestamp(new Timestamp(dateTime.getMillis()));
        chronicleDao.insertNewRecord(line2);

        int rowsAfterAdds = chronicleDao.findRecordsCount();
        assertThat(rowsAfterAdds).isEqualTo(rowsAtStart + 4);

        final List<ChronicleLine> last3Lines = chronicleDao.findLastNNamingRecordsDownwards(3);
        assertThat(last3Lines).hasSize(3);
        assertThat(line2).isEqualTo(last3Lines.get(2));
        assertThat(line0).isEqualTo(last3Lines.get(0));
    }

    @Test
    public void findNNamingRecordsNonWorkOrNonBreakWhenNonInDb() throws NoRecordsInPoolException {
        int rowsAtStart = chronicleDao.findRecordsCount();
        assertThat(rowsAtStart).isEqualTo(0);
        final List<ChronicleLine> last3Lines = chronicleDao.findLastNNamingRecordsDownwards(3);
        assertThat(last3Lines).isEmpty();
    }

    @Test
    public void findsLastNRecordsByTag() throws NoRecordsInPoolException {
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

        ChronicleLine line2 = new ChronicleLine();
        line2.setTag("work2");
        line2.setDescription("line2 description");
        dateTime = new DateTime(2014, 11, 24, 0, 10, 0, 0);
        line2.setTimestamp(new Timestamp(dateTime.getMillis()));
        chronicleDao.insertNewRecord(line2);

        int rowsAfterAdds = chronicleDao.findRecordsCount();
        assertThat(rowsAfterAdds).isEqualTo(rowsAtStart + 4);

        final List<ChronicleLine> lastNByTag = chronicleDao.findLastNRecordsByTagUpwards("work0", 2);
        assertThat(lastNByTag).hasSize(2);
        assertThat(line0).isEqualTo(lastNByTag.get(0));
        assertThat(line_1).isEqualTo(lastNByTag.get(1));
    }

    @Test
    public void requestMoreRecordsThanInDbGetsAsManyAsAvailable() {
        int rowsAtStart = chronicleDao.findRecordsCount();
        assertThat(rowsAtStart).isEqualTo(0);

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

        ChronicleLine line2 = new ChronicleLine();
        line2.setTag("work2");
        line2.setDescription("line2 description");
        dateTime = new DateTime(2014, 11, 24, 0, 10, 0, 0);
        line2.setTimestamp(new Timestamp(dateTime.getMillis()));
        chronicleDao.insertNewRecord(line2);

        ChronicleLine line3 = new ChronicleLine();
        line3.setTag("work3");
        line3.setDescription("line2 description");
        chronicleDao.insertNewRecord(line3);

        int rowsAfterAdds = chronicleDao.findRecordsCount();
        assertThat(rowsAfterAdds).isEqualTo(rowsAtStart + 5);

        List<ChronicleLine> lastNLines;
        lastNLines = chronicleDao.findLastNRecordsUpwards(10);
        assertThat(lastNLines).hasSize(rowsAfterAdds);
        lastNLines = chronicleDao.findLastNRecordsDownwards(10);
        assertThat(lastNLines).hasSize(rowsAfterAdds);
        lastNLines = chronicleDao.findLastNRecordsTodayUpwards(14);
        assertThat(lastNLines).hasSize(1);
    }

    @Test
    public void restingLastRecordFromEmptyDbGetsEmptyObject() throws NoRecordsInPoolException {
        thrown.expect(NoRecordsInPoolException.class);
        final ChronicleLine lastRecord = chronicleDao.findLastRecord();
        assertThat(lastRecord).isNotNull();
    }

    @Test
    public void extraConfigurationInTestingIsAvailable() throws SQLException, NoRecordsInPoolException {
        final XMLConfiguration configuration = new ConfigurationProvider("src/test/resources/configuration_1.xml").getXMLConfiguration();
        final DataSourceFactory dataSourceFactory = new DataSourceFactory(configuration);
        final DataSource dataSource = dataSourceFactory.getDataSource();
        chronicleDao = new ChronicleLineDao(dataSource);
        assertThat(chronicleDao).isNotNull();
    }

    @Test
    public void findsParentsToId() {
        final XMLConfiguration configuration = new ConfigurationProvider("src/test/resources/configuration_1.xml").getXMLConfiguration();
        final DataSourceFactory dataSourceFactory = new DataSourceFactory(configuration);
        final DataSource dataSource = dataSourceFactory.getDataSource();
        chronicleDao = new ChronicleLineDao(dataSource);
        final List<ChronicleLine> parents = chronicleDao.findAllParentsToId(8);
        log.info("{}", parents);
        assertThat(parents.size()).isEqualTo(3);
    }
}
