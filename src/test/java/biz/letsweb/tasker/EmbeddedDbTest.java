package biz.letsweb.tasker;

import biz.letsweb.tasker.configuration.ConfigurationProvider;
import biz.letsweb.tasker.db.DataSourceFactory;
import biz.letsweb.tasker.db.DbFileOperations;
import biz.letsweb.tasker.db.InitializeDb;
import java.sql.SQLException;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

/**
 *
 * @author toks
 */
public class EmbeddedDbTest {

    private static DbFileOperations dbFileOperations;

    public EmbeddedDbTest() {
    }

    @BeforeClass
    public static void setUpClass() {
        final ConfigurationProvider provider = new ConfigurationProvider("src/test/resources/configuration.xml");
        dbFileOperations = DbFileOperations.getInstance(provider.getXMLConfiguration());
        final DataSourceFactory factory = new DataSourceFactory(provider.getXMLConfiguration());
        final InitializeDb initializeDb = new InitializeDb(factory.getDataSource(), provider.getXMLConfiguration());
        if (!dbFileOperations.dbDirectoryExists()) {
            System.out.println("Before creating database: " + dbFileOperations.getDbDirectoryPath());
            initializeDb.createTables();
            System.out.println("Created database: " + dbFileOperations.getDbDirectoryPath());
        } else {
            System.out.println("WARNING: Test didn't create database: " + dbFileOperations.getDbDirectoryPath());
        }
    }

    @AfterClass
    public static void tearDownClass() {
        final boolean dbDirectoryExists = dbFileOperations.dbDirectoryExists();
        final boolean deleteDb = dbFileOperations.deleteDb();
        System.out.println("db dir exists?: " + dbDirectoryExists);
        System.out.println("db dir deleted?: " + deleteDb);
        if (dbDirectoryExists && deleteDb) {
            System.out.println("Deleted database: " + dbFileOperations.getDbDirectoryPath());
        } else {
            System.out.println("WARNING: Test didn't delete database: " + dbFileOperations.getDbDirectoryPath());
        }
    }

    @Before
    public void setUp() throws ClassNotFoundException, SQLException {

    }

    @After
    public void tearDown() {

    }

    @Test
    public void testCreateDb() {
        System.out.println("running");
        System.out.println(dbFileOperations.getDbDirectoryPath());
        // createDB.loadDb();
        // createDB.emptyDb();
    }

}
