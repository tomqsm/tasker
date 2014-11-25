package biz.letsweb.tasker.timecalculator;

import biz.letsweb.tasker.persistence.model.ChronicleRecordLine;
import biz.letsweb.tasker.model.ConsoleViewModel;
import java.sql.Timestamp;
import java.util.ArrayList;
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
public class TimeCalaculator {

  public static final Logger log = LoggerFactory.getLogger(TimeCalaculator.class);

  public List<ConsoleViewModel> getDurationsPerTag(List<ChronicleRecordLine> recPool, int lines) {
        int size = recPool.size();
        Map<String, Duration> durations = new HashMap<>();
        List<ConsoleViewModel> viewModels = new ArrayList<>(size);
        ConsoleViewModel viewModel;
        for (int i = 0; i < size; i++) {
            Timestamp earlierTimestamp
                    = (i == 0 ? recPool.get(i).getTimestamp() : recPool.get(i).getTimestamp());
            Timestamp laterTimestamp
                    = (i == (recPool.size() - 1)
                    ? new Timestamp(System.currentTimeMillis())
                    : recPool.get(i + 1).getTimestamp());
            DateTime from = new DateTime(earlierTimestamp);
            DateTime to = new DateTime(laterTimestamp);
            Duration duration = new Duration(from, to);
            if (durations.containsKey(recPool.get(i).getTag())) {
                Duration d = durations.get(recPool.get(i).getTag());
                d = d.plus(duration.getMillis());
                durations.put(recPool.get(i).getTag(), d);
            } else {
                durations.put(recPool.get(i).getTag(), duration);
            }
            viewModel = new ConsoleViewModel();
            viewModel.setChronicleRecordLine(recPool.get(i));
            viewModel.setDuration(duration);
            viewModel.setTotalDuration(durations.get(recPool.get(i).getTag()));
            viewModels.add(viewModel);
        }
        return viewModels;
    }
}
