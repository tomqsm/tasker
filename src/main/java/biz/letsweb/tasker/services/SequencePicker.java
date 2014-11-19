package biz.letsweb.tasker.services;

import biz.letsweb.tasker.persistence.model.ChronicleRecordLine;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 *
 * @author toks
 */
public class SequencePicker {

    public List<ChronicleRecordLine> pickSequenceByCount(List<ChronicleRecordLine> list, String key) {
        final Iterator<ChronicleRecordLine> iterator = list.iterator();
        final List<ChronicleRecordLine> picks = new ArrayList<>();
        int index = 0;
        while (iterator.hasNext()) {
            ChronicleRecordLine ch = iterator.next();
            int cnt = ch.getCount();
            if (ch.getTag().equalsIgnoreCase(key) && (picks.size() == 0 || (cnt - 1) == picks.get(index).getCount())) {
                picks.add(ch);
            }
        }
        return picks;
    }
}
