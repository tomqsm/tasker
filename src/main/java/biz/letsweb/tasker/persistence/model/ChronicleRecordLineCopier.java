package biz.letsweb.tasker.persistence.model;

import biz.letsweb.tasker.persistence.model.ChronicleRecordLine;

/**
 *
 * @author toks
 */
public class ChronicleRecordLineCopier {
    public void copyEntry(ChronicleRecordLine from, ChronicleRecordLine to) {
        to.setId(from.getId());
        to.setTag(from.getTag());
        to.setDescription(from.getDescription());
        to.setTimestamp(from.getTimestamp());
    }
}
