package biz.letsweb.tasker.persistence.model;

import java.sql.Timestamp;

/**
 *
 * @author toks
 */
public class CommentLine {
    private int id;
    private int count;
    private int chronicleId;
    private String description;
    private Timestamp timestamp;

    public CommentLine(int id, int count, int chronicleId, String description, Timestamp timestamp) {
        this.id = id;
        this.count = count;
        this.chronicleId = chronicleId;
        this.description = description;
        this.timestamp = timestamp;
    }

    public CommentLine() {
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getChronicleId() {
        return chronicleId;
    }

    public void setChronicleId(int chronicleId) {
        this.chronicleId = chronicleId;
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
        return "CommentLine{" + "description=" + description + '}';
    }
    
    
}
