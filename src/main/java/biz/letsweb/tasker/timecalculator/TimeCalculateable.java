package biz.letsweb.tasker.timecalculator;

import java.sql.Timestamp;
import java.util.Date;

/**
 *
 * @author toks
 */
public interface TimeCalculateable {
    Date calculateDurationFromPravious(Timestamp ts);
}
