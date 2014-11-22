package biz.letsweb.tasker;

import biz.letsweb.tasker.persistence.model.ChronicleRecordLine;
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

  public void displayDurationOfNRecord(List<ChronicleRecordLine> lastNRecords) {
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

  public void displayDurationSummative(List<ChronicleRecordLine> lastNRecords) {
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

  public Map<String, Duration> displayDurationSummativePerTag(List<ChronicleRecordLine> allRecords) {
        Map<String, Duration> durations = new HashMap<>();
        for (int i = 0; i < allRecords.size(); i++) {
            Timestamp earlierTimestamp
                    = (i == 0 ? allRecords.get(i).getTimestamp() : allRecords.get(i).getTimestamp());
            Timestamp laterTimestamp
                    = (i == (allRecords.size() - 1)
                    ? new Timestamp(System.currentTimeMillis())
                    : allRecords.get(i + 1).getTimestamp());
            DateTime from = new DateTime(earlierTimestamp);
            DateTime to = new DateTime(laterTimestamp);
            Duration duration = new Duration(from, to);
            if (durations.containsKey(allRecords.get(i).getTag())) {
                Duration d = durations.get(allRecords.get(i).getTag());
                d = d.plus(duration.getMillis());
                durations.put(allRecords.get(i).getTag(), d);
            } else {
                durations.put(allRecords.get(i).getTag(), duration);
            }
        }
        return durations;
    }
}
