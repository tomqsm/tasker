package biz.letsweb.tasker.timing;

import biz.letsweb.tasker.NoRecordsInPoolException;
import biz.letsweb.tasker.UnexpectedOrderingException;
import biz.letsweb.tasker.persistence.dao.ChronicleLineDao;
import biz.letsweb.tasker.persistence.model.ChronicleRecordLine;
import java.sql.Timestamp;
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
public class Calculator {
    public static final Logger log = LoggerFactory.getLogger(Calculator.class);

    public Map<String, Duration> calculateDurations(List<ChronicleRecordLine> recordPool) throws NoRecordsInPoolException {
        checkRecordPoolHasRecords(recordPool);
        assureAscendingOrderingById(recordPool);
        final Map<String, Duration> durations = new HashMap<>();
        for (int i = 0; i < recordPool.size(); i++) {
            Timestamp earlierTimestamp = (i == 0 ? recordPool.get(0).getTimestamp() : recordPool.get(i).getTimestamp());
            Timestamp laterTimestamp = (i == (recordPool.size() - 1) ? new Timestamp(System.currentTimeMillis()) : recordPool.get(i + 1).getTimestamp());
            DateTime from = new DateTime(earlierTimestamp.getTime());
            DateTime to = new DateTime(laterTimestamp.getTime());
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

    public void assureAscendingOrderingById(List<ChronicleRecordLine> recordPool) {
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

    public void checkRecordPoolHasRecords(List<ChronicleRecordLine> recordPool) throws NoRecordsInPoolException {
        if (recordPool.isEmpty()) {
            throw new NoRecordsInPoolException("At lease one record expected in the pool.");
        }
    }
}
