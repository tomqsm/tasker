package biz.letsweb.tasker;

import java.sql.SQLException;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.fest.assertions.Assertions.assertThat;

/**
 *
 * @author toks
 */
public class EmbeddedDbTest {

    private final CheckDB checkDB = new CheckDB();
    private final CreateDB createDB = new CreateDB();
    private final DeleteDB deleteDB = new DeleteDB();

    public EmbeddedDbTest() {
    }

    @BeforeClass
    public static void setUpClass() {
        
    }

    @AfterClass
    public static void tearDownClass() {
    }

    @Before
    public void setUp() throws ClassNotFoundException, SQLException {
        if (!checkDB.dbDirectoryExists()) {
            createDB.createDb();
            System.out.println("Created database: " + checkDB.getDbPath());
        } else {
            System.out.println("WARNING: Test didn't create database: " + checkDB.getDbPath());
        }
    }

    @After
    public void tearDown() {
        if (checkDB.dbDirectoryExists() && deleteDB.deleteDb()) {
            System.out.println("Deleted database: " + checkDB.getDbPath());
        } else {
            System.out.println("WARNING: Test didn't delete database: " + checkDB.getDbPath());
        }
    }

    @Test
    public void testCreateDb() throws ClassNotFoundException, SQLException {
        System.out.println("running");
//        createDB.loadDb();
//        createDB.emptyDb();
    }

}
