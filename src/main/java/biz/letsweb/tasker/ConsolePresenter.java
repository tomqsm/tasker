package biz.letsweb.tasker;

import biz.letsweb.tasker.chronicle.model.ChronicleLine;
import java.sql.Timestamp;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.joda.time.DateTime;
import org.joda.time.Duration;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 * @author Tomasz
 */
public class ConsolePresenter {

  public static final Logger log = LoggerFactory.getLogger(ConsolePresenter.class);

  public void displayDurationOfNRecord(List<ChronicleLine> lastNRecords)
      throws NoRecordsInPoolException {
    if (lastNRecords.isEmpty()) {
      log.error("Record pool is empty.");
      throw new NoRecordsInPoolException("No records in designated pool.");
    }
    for (int i = 0; i < lastNRecords.size(); i++) {
      Timestamp earlierTimestamp =
          (i == 0 ? lastNRecords.get(i).getTimestamp() : lastNRecords.get(i).getTimestamp());
      Timestamp laterTimestamp =
          (i == (lastNRecords.size() - 1)
              ? new Timestamp(System.currentTimeMillis())
              : lastNRecords.get(i + 1).getTimestamp());
      DateTime from = new DateTime(earlierTimestamp);
      DateTime to = new DateTime(laterTimestamp);
      Duration duration = new Duration(from, to);
      String output =
          String.format("#%d duration: %s %s %d minutes", lastNRecords.get(i).getCount(),
              lastNRecords.get(i).getTag(), lastNRecords.get(i).getDescription(),
              duration.getStandardMinutes());
      System.out.println(output);
    }
  }

  public Map<String, Duration> displayDurationSummativePerTag(List<ChronicleLine> recordPool) throws NoRecordsInPoolException {
        if (recordPool.isEmpty()) {
            log.error("Record pool is empty.");
            throw new NoRecordsInPoolException("No records in designated pool.");
        }
        Map<String, Duration> durations = new HashMap<>();
        for (int i = 0; i < recordPool.size(); i++) {
            Timestamp earlierTimestamp
                    = (i == 0 ? recordPool.get(i).getTimestamp() : recordPool.get(i).getTimestamp());
            Timestamp laterTimestamp
                    = (i == (recordPool.size() - 1)
                    ? new Timestamp(System.currentTimeMillis())
                    : recordPool.get(i + 1).getTimestamp());
            DateTime from = new DateTime(earlierTimestamp);
            DateTime to = new DateTime(laterTimestamp);
            Duration duration = new Duration(from, to);
            if (durations.containsKey(recordPool.get(i).getTag())) {
                Duration d = durations.get(recordPool.get(i).getTag());
                d = d.plus(duration.getMillis());
                durations.put(recordPool.get(i).getTag(), d);
            } else {
                durations.put(recordPool.get(i).getTag(), duration);
            }
        }
        return durations;
    }
  public Map<String, Duration> loadDurations(List<ChronicleLine> recordPool) throws NoRecordsInPoolException {
        if (recordPool.isEmpty()) {
            log.error("Record pool is empty.");
            throw new NoRecordsInPoolException("No records in designated pool.");
        }
        Map<String, Duration> durations = new HashMap<>();
        for (int i = 0; i < recordPool.size(); i++) {
            Timestamp earlierTimestamp
                    = (i == 0 ? recordPool.get(i).getTimestamp() : recordPool.get(i).getTimestamp());
            Timestamp laterTimestamp
                    = (i == (recordPool.size() - 1)
                    ? new Timestamp(System.currentTimeMillis())
                    : recordPool.get(i + 1).getTimestamp());
            DateTime from = new DateTime(earlierTimestamp);
            DateTime to = new DateTime(laterTimestamp);
            Duration duration = new Duration(from, to);
            if (durations.containsKey(recordPool.get(i).getTag())) {
                Duration d = durations.get(recordPool.get(i).getTag());
                d = d.plus(duration.getMillis());
                durations.put(recordPool.get(i).getTag(), d);
            } else {
                durations.put(recordPool.get(i).getTag(), duration);
            }
        }
        return durations;
    }

}
