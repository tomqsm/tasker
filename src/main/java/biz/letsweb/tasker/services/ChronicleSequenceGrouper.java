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
public class ChronicleSequenceGrouper {

    public static final Logger log = LoggerFactory.getLogger(ChronicleSequenceGrouper.class);

    /**
     * Returns end index of a group.
     *
     * @param todaysRecords
     * @return
     */
    public final List<ChronicleRecordLine> getGroupRearEntry(List<ChronicleRecordLine> todaysRecords) {
        if(todaysRecords.isEmpty()){
            throw new RuntimeException("No work has been done today.");
        }
        final List<ChronicleRecordLine> groups = new ArrayList<>();
        for (int i = 0; i < todaysRecords.size(); i++) {
            final ChronicleRecordLine currentLine = todaysRecords.get(i);
            ChronicleRecordLine previousLine = null;
            if (!groups.isEmpty()) {
                previousLine = todaysRecords.get(i - 1);
            }
            if (groups.isEmpty() && currentLine.getTag().equalsIgnoreCase(currentLine.getTag())) {
                groups.add(currentLine);
            } else if (!previousLine.getTag().equalsIgnoreCase(currentLine.getTag())) {
                groups.add(currentLine);
            } else {
            }
        }
        Collections.reverse(groups);
        return groups;
    }
}
