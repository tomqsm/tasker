package biz.letsweb.tasker.model;

import biz.letsweb.tasker.persistence.model.ChronicleLine;
import org.joda.time.Duration;

/**
 *
 * @author Tomasz
 */
//@XmlRootElement
public class ConsoleViewModel {

    private ChronicleLine chronicleRecordLine;
    private Duration duration;
    private Duration totalDuration;

    public ConsoleViewModel() {
    }

    public ChronicleLine getChronicleRecordLine() {
        return chronicleRecordLine;
    }

    public void setChronicleRecordLine(ChronicleLine chronicleRecordLine) {
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
