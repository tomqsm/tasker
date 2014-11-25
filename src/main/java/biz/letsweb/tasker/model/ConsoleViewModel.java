package biz.letsweb.tasker.model;

import biz.letsweb.tasker.persistence.model.ChronicleRecordLine;
import org.joda.time.Duration;

/**
 *
 * @author Tomasz
 */
//@XmlRootElement
public class ConsoleViewModel {

    private ChronicleRecordLine chronicleRecordLine;
    private Duration duration;
    private Duration totalDuration;

    public ConsoleViewModel() {
    }

    public ChronicleRecordLine getChronicleRecordLine() {
        return chronicleRecordLine;
    }

    public void setChronicleRecordLine(ChronicleRecordLine chronicleRecordLine) {
        this.chronicleRecordLine = chronicleRecordLine;
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

    @Override
    public String toString() {
        return "ConsoleViewModel{" + "chronicleRecordLine=" + chronicleRecordLine + ", duration="
                + duration + ", totalDuration=" + totalDuration + '}';
    }
}
