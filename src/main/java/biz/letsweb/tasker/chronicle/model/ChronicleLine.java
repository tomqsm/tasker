package biz.letsweb.tasker.chronicle.model;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import org.joda.time.Duration;

/**
 *
 * @author toks
 */
public class ChronicleLine {

    private int id;
    private int count;
    private int parentId;
    private String tag;
    private String description = "";
    private Timestamp timestamp;
    private Duration duration;
    private Duration totalDuration;
    private List<ChronicleLine> otherLines;

    public ChronicleLine(int id, int count, String tag, String description, Timestamp timestamp) {
        this.id = id;
        this.count = count;
        this.tag = tag;
        this.description = description;
        this.timestamp = timestamp;
    }

    public ChronicleLine(int id, int count, String tag, String description, Timestamp timestamp, ChronicleLine otherLine) {
        this(id, count, tag, description, timestamp);
        this.otherLines = new ArrayList<>();
        this.otherLines.add(otherLine);
    }

    public ChronicleLine(int id, int count, String tag, String description, Timestamp timestamp, List<ChronicleLine> otherLine) {
        this(id, count, tag, description, timestamp);
        this.otherLines = otherLine;
    }

    public ChronicleLine() {
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

    public Duration getDuration() {
        return duration;
    }

    public void setDuration(Duration duration) {
        this.duration = duration;
    }

    public Duration getTotalDuration() {
        return totalDuration;
    }

    public void setTotalDuration(Duration totalDuration) {
        this.totalDuration = totalDuration;
    }

    public List<ChronicleLine> getOtherLines() {
        return otherLines;
    }

    public void setOtherLines(List<ChronicleLine> comments) {
        this.otherLines = comments;
    }

    public void addOtherLineComment(ChronicleLine commentLine) {
        if (otherLines == null) {
            otherLines = new ArrayList<>();
        }
        otherLines.add(commentLine);
    }

    public int getParentId() {
        return parentId;
    }

    public void setParentId(int parentId) {
        this.parentId = parentId;
    }

    @Override
    public int hashCode() {
        int hash = 3;
        hash = 97 * hash + Objects.hashCode(this.tag);
        hash = 97 * hash + Objects.hashCode(this.timestamp);
        return hash;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final ChronicleLine other = (ChronicleLine) obj;
        if (!Objects.equals(this.tag, other.tag)) {
            return false;
        }
        if (!Objects.equals(this.timestamp, other.timestamp)) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "ChronicleRecordLine{" + "id=" + id + ", count=" + count + ", tag=" + tag + ", description=" + description + ", timestamp=" + timestamp + '}';
    }

}
