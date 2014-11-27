package biz.letsweb.tasker.services;

import biz.letsweb.tasker.NoRecordsInPoolException;
import biz.letsweb.tasker.databaseconnectivity.DataSourceFactory;
import biz.letsweb.tasker.databaseconnectivity.InitializeDb;
import biz.letsweb.tasker.persistence.dao.ChronicleLineDao;
import biz.letsweb.tasker.persistence.model.ChronicleRecordLine;
import java.sql.SQLException;
import java.util.List;
import java.util.Map;
import static org.fest.assertions.Assertions.assertThat;
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

    public ChronicleLineDaoTest() {
    }

    public static final Logger log = LoggerFactory.getLogger(ChronicleLineDaoTest.class);

    private ChronicleLineDao chronicleDao;
    private InitializeDb initializeDb;

    @BeforeClass
    public static void setUpClass() {
    }

    @AfterClass
    public static void tearDownClass() {
    }

    @Before
    public void setUp() throws SQLException {
        final DataSourceFactory dataSourceFactory = new DataSourceFactory(DataSourceFactory.Type.CLIENT);
        chronicleDao = new ChronicleLineDao(dataSourceFactory.getDataSource());
        initializeDb = new InitializeDb(dataSourceFactory.getDataSource());
        final InitializeDb.Feedback createTables = initializeDb.createTables();
        if (createTables == InitializeDb.Feedback.TABLES_EXISTED) {
            initializeDb.clearTables();
        }
    }

    /**
     * Test of findLastRecord method, of class ChronicleLineDao.
     */
    // @Test
    public void testFindLastRecord() {
        final Map<String, Duration> durations = chronicleDao.findDurationsOfTodaysRecords();
        // assertThat(durations).isEmpty();
        System.out.println(durations.get("breakCoffe").getStandardMinutes());

    }

    @Test
    public void returnsEmptyListWhenNoRecordsPerDay() throws NoRecordsInPoolException, SQLException {
        final List<ChronicleRecordLine> allRecords = chronicleDao.findAllRecords();
        assertThat(allRecords).isNotNull();
        assertThat(allRecords).isEmpty();
    }
}
