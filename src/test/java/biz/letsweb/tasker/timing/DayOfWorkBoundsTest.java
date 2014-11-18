package biz.letsweb.tasker.timing;

import java.sql.Timestamp;
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

  public DayOfWorkBoundsTest() {}

  @BeforeClass
  public static void setUpClass() {}

  @AfterClass
  public static void tearDownClass() {}

  @Before
  public void setUp() {}

  @After
  public void tearDown() {}

  /**
   * Test of getTodayMidnight method, of class DayBoundsTimestamp.
   */
  @Test
  public void testGetTodayMidnight() {
    DayBoundsTimestamp instance = new DayBoundsTimestamp();
    Timestamp timeStamp = instance.getStartOfTodayTimestamp();
    System.out.println(timeStamp);
  }

  @Test
  public void testGetTomorrowMidnight() {
    DayBoundsTimestamp instance = new DayBoundsTimestamp();
    Timestamp timeStamp = instance.getEndOfTodayTimestamp();
    System.out.println(timeStamp);
  }

  @Test
  public void testGetStartOfNDayBackTimestamp() {
    DayBoundsTimestamp instance = new DayBoundsTimestamp();
    Timestamp timeStamp = instance.getStartOfNDayBackTimestamp(1);
    System.out.println(timeStamp);
  }

  @Test
  public void testgetEndOfNDayBackTimestamp() {
    DayBoundsTimestamp instance = new DayBoundsTimestamp();
    Timestamp timeStamp = instance.getEndOfNDayBackTimestamp(1);
    System.out.println(timeStamp);
  }

}
