package biz.letsweb.tasker.timing;

import java.sql.Timestamp;
import static org.fest.assertions.Assertions.assertThat;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

/**
 *
 * @author Tomasz
 */
public class DayOfWorkBoundsTest {

    public DayOfWorkBoundsTest() {
    }

    @BeforeClass
    public static void setUpClass() {
    }

    @AfterClass
    public static void tearDownClass() {
    }

    @Before
    public void setUp() {
    }

    @After
    public void tearDown() {
    }

    /**
     * Should produce e.g. 2014-11-17 00:00:00.0
     */
    @Test
    public void testGetStartOfNDayBackTimestamp() {
        DayBoundsTimestamp instance = new DayBoundsTimestamp();
        Timestamp timeStamp = instance.getStartOfNDayBackTimestamp(2);
        assertThat(timeStamp.toString()).contains("00:00:00.0");
        System.out.println(timeStamp);
    }

    /**
     * Should produce e.g. 23:59:59.999
     */
    @Test
    public void testgetEndOfNDayBackTimestamp() {
        DayBoundsTimestamp instance = new DayBoundsTimestamp();
        Timestamp timeStamp = instance.getEndOfNDayBackTimestamp(2);
        assertThat(timeStamp.toString()).contains("23:59:59.999");
        System.out.println(timeStamp);
    }

    /**
     * Test of getTodayMidnight method, of class DayBoundsTimestamp.
     */
    @Test
    public void getStartOfTodayTimestamp() {
        DayBoundsTimestamp instance = new DayBoundsTimestamp();
        Timestamp timeStamp = instance.getStartOfTodayTimestamp();
        assertThat(timeStamp.toString()).contains("00:00:00.0");
        System.out.println(timeStamp);
    }

    @Test
    public void getEndOfTodayTimestamp() {
        DayBoundsTimestamp instance = new DayBoundsTimestamp();
        Timestamp timeStamp = instance.getEndOfTodayTimestamp();
        assertThat(timeStamp.toString()).contains("23:59:59.999");
        System.out.println(timeStamp);
    }

}
