package biz.letsweb.tasker.timing;

import biz.letsweb.tasker.NoRecordsInPoolException;
import biz.letsweb.tasker.chronicle.model.ChronicleLine;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.joda.time.DateTime;
import org.joda.time.Duration;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 * @author toks
 */
public class DurationsCalculator {
    public static final Logger log = LoggerFactory.getLogger(DurationsCalculator.class);

    public List<ChronicleLine> calculateDurations(List<ChronicleLine> recordPool) throws NoRecordsInPoolException {
        checkRecordPoolHasRecords(recordPool);
        assureAscendingOrderingById(recordPool);
        final Map<String, Duration> durations = new HashMap<>();
        final List<ChronicleLine> lines = new ArrayList<>();
        for (int i = 0; i < recordPool.size(); i++) {
            final ChronicleLine iLine = recordPool.get(i);
            Timestamp earlierTimestamp = (i == 0 ? iLine.getTimestamp() : iLine.getTimestamp());
            Timestamp laterTimestamp = (i == (recordPool.size() - 1) ? new Timestamp(System.currentTimeMillis()) : recordPool.get(i + 1).getTimestamp());
            DateTime from = new DateTime(earlierTimestamp.getTime());
            DateTime to = new DateTime(laterTimestamp.getTime());
            Duration duration = new Duration(from, to);
            iLine.setDuration(duration);
            if (durations.containsKey(iLine.getTag())) {
                Duration d = durations.get(iLine.getTag());
                d = d.plus(duration.getMillis());
                durations.put(iLine.getTag(), d);
                iLine.setTotalDuration(d);
            } else {
                durations.put(iLine.getTag(), duration);
                iLine.setTotalDuration(duration);
            }
            lines.add(iLine);
        }
        return lines;
    }

    private void assureAscendingOrderingById(List<ChronicleLine> recordPool) {
        final int first = recordPool.get(0).getId();
        final int size = recordPool.size();
        if (size > 1) {
            int second = recordPool.get(1).getId();
            if (first > second) {
                Collections.reverse(recordPool);
                log.info("Convering record pool of {} from descending to ascending.", size);
            }
        }
    }

    private void checkRecordPoolHasRecords(List<ChronicleLine> recordPool) throws NoRecordsInPoolException {
        if (recordPool.isEmpty()) {
            throw new NoRecordsInPoolException("At lease one record expected in the pool.");
        }
    }
//      public void displayDurationOfNRecord(List<ChronicleLine> lastNRecords)
//      throws NoRecordsInPoolException {
//    if (lastNRecords.isEmpty()) {
//      log.error("Record pool is empty.");
//      throw new NoRecordsInPoolException("No records in designated pool.");
//    }
//    for (int i = 0; i < lastNRecords.size(); i++) {
//      Timestamp earlierTimestamp =
//          (i == 0 ? lastNRecords.get(i).getTimestamp() : lastNRecords.get(i).getTimestamp());
//      Timestamp laterTimestamp =
//          (i == (lastNRecords.size() - 1)
//              ? new Timestamp(System.currentTimeMillis())
//              : lastNRecords.get(i + 1).getTimestamp());
//      DateTime from = new DateTime(earlierTimestamp);
//      DateTime to = new DateTime(laterTimestamp);
//      Duration duration = new Duration(from, to);
//      String output =
//          String.format("#%d duration: %s %s %d minutes", lastNRecords.get(i).getCount(),
//              lastNRecords.get(i).getTag(), lastNRecords.get(i).getDescription(),
//              duration.getStandardMinutes());
//      System.out.println(output);
//    }
//  }

//  public Map<String, Duration> displayDurationSummativePerTag(List<ChronicleLine> recordPool) throws NoRecordsInPoolException {
//        if (recordPool.isEmpty()) {
//            log.error("Record pool is empty.");
//            throw new NoRecordsInPoolException("No records in designated pool.");
//        }
//        Map<String, Duration> durations = new HashMap<>();
//        for (int i = 0; i < recordPool.size(); i++) {
//            Timestamp earlierTimestamp
//                    = (i == 0 ? recordPool.get(i).getTimestamp() : recordPool.get(i).getTimestamp());
//            Timestamp laterTimestamp
//                    = (i == (recordPool.size() - 1)
//                    ? new Timestamp(System.currentTimeMillis())
//                    : recordPool.get(i + 1).getTimestamp());
//            DateTime from = new DateTime(earlierTimestamp);
//            DateTime to = new DateTime(laterTimestamp);
//            Duration duration = new Duration(from, to);
//            if (durations.containsKey(recordPool.get(i).getTag())) {
//                Duration d = durations.get(recordPool.get(i).getTag());
//                d = d.plus(duration.getMillis());
//                durations.put(recordPool.get(i).getTag(), d);
//            } else {
//                durations.put(recordPool.get(i).getTag(), duration);
//            }
//        }
//        return durations;
//    }
//  public Map<String, Duration> loadDurations(List<ChronicleLine> recordPool) throws NoRecordsInPoolException {
//        if (recordPool.isEmpty()) {
//            log.error("Record pool is empty.");
//            throw new NoRecordsInPoolException("No records in designated pool.");
//        }
//        Map<String, Duration> durations = new HashMap<>();
//        for (int i = 0; i < recordPool.size(); i++) {
//            Timestamp earlierTimestamp
//                    = (i == 0 ? recordPool.get(i).getTimestamp() : recordPool.get(i).getTimestamp());
//            Timestamp laterTimestamp
//                    = (i == (recordPool.size() - 1)
//                    ? new Timestamp(System.currentTimeMillis())
//                    : recordPool.get(i + 1).getTimestamp());
//            DateTime from = new DateTime(earlierTimestamp);
//            DateTime to = new DateTime(laterTimestamp);
//            Duration duration = new Duration(from, to);
//            if (durations.containsKey(recordPool.get(i).getTag())) {
//                Duration d = durations.get(recordPool.get(i).getTag());
//                d = d.plus(duration.getMillis());
//                durations.put(recordPool.get(i).getTag(), d);
//            } else {
//                durations.put(recordPool.get(i).getTag(), duration);
//            }
//        }
//        return durations;
//    }
}
