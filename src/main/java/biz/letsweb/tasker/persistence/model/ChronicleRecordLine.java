package biz.letsweb.tasker.persistence.model;

import java.sql.Timestamp;

/**
 *
 * @author toks
 */
public class ChronicleRecordLine {
    private int id;
    private int count;
    private String tag;
    private String description;
    private Timestamp timestamp;

    public ChronicleRecordLine(int id, int count, String tag, String description, Timestamp timestamp) {
        this.id = id;
        this.count = count;
        this.tag = tag;
        this.description = description;
        this.timestamp = timestamp;
    }

    public ChronicleRecordLine() {
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setTag(String tag) {
        this.tag = tag;
    }

    public String getTag() {
        return tag;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }

    public Timestamp getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(Timestamp timestamp) {
        this.timestamp = timestamp;
    }

    @Override
    public String toString() {
        return "ChronicleRecordLine{" + "id=" + id + ", count=" + count + ", tag=" + tag + ", description=" + description + ", timestamp=" + timestamp + '}';
    }
    
}
