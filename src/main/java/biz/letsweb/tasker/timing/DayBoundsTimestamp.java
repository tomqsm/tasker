package biz.letsweb.tasker.timing;

import java.sql.Timestamp;
import org.joda.time.DateTime;

/**
 *
 * @author Tomasz
 */
public class DayBoundsTimestamp {

  public Timestamp getStartOfTodayTimestamp() {
    DateTime today = new DateTime();
    final DateTime todayMidnight = today.withTimeAtStartOfDay();
    return new Timestamp(todayMidnight.getMillis());
  }

  public Timestamp getEndOfTodayTimestamp() {
    DateTime today = new DateTime();
    final DateTime tomorrow = today.plusDays(1);
    final DateTime todayMidnight = tomorrow.withTimeAtStartOfDay();
    return new Timestamp(todayMidnight.getMillis() - 1);
  }

  public Timestamp getStartOfNDayBackTimestamp(int daysBack) {
    DateTime today = new DateTime();
    final DateTime tomorrow = today.minusDays(1);
    final DateTime todayMidnight = tomorrow.withTimeAtStartOfDay();
    return new Timestamp(todayMidnight.getMillis());
  }

  public Timestamp getEndOfNDayBackTimestamp(int daysBack) {
    DateTime today = new DateTime();
    final DateTime tomorrow = today.minusDays(1);
    final DateTime todayMidnight = tomorrow.withTimeAtStartOfDay();
    return new Timestamp(todayMidnight.getMillis() - 1);
  }
}
