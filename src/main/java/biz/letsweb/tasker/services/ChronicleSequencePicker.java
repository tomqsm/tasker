package biz.letsweb.tasker.services;

import biz.letsweb.tasker.persistence.model.ChronicleRecordLine;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 * @author toks
 */
public class ChronicleSequencePicker {

  public static final Logger log = LoggerFactory.getLogger(ChronicleSequencePicker.class);

  /**
   * Returns end index of a group.
   *
   * @param todaysRecords
   * @return
   */
  public List<ChronicleRecordLine> dayRecordsGrouper(List<ChronicleRecordLine> todaysRecords) {
        log.info("count: {}", todaysRecords.get(0).getCount());
        String lastTag = todaysRecords.get(0).getTag();
        log.info("last: {}", todaysRecords.get(0));
        List<ChronicleRecordLine> groups = new ArrayList<>();
        for (int i = 0; i < todaysRecords.size(); i++) {
            if (groups.isEmpty() && todaysRecords.get(i).getTag().equalsIgnoreCase(lastTag)) {
                groups.add(todaysRecords.get(i));
            } else if(!todaysRecords.get(i-1).getTag().equalsIgnoreCase(lastTag)){
                lastTag = todaysRecords.get(i).getTag();
                groups.add(todaysRecords.get(i));
            } else {
            }
        }
        Collections.reverse(groups);
        return groups;
    }
}
