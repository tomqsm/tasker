package biz.letsweb.tasker.persistence.model;

import java.sql.Timestamp;

/**
 *
 * @author toks
 */
public class ChronicleRecordLine {
    private int id;
    private String description;
    private Timestamp timestamp;

    public ChronicleRecordLine(int id, String description, Timestamp timestamp) {
        this.id = id;
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

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Timestamp getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(Timestamp timestamp) {
        this.timestamp = timestamp;
    }
    
}
