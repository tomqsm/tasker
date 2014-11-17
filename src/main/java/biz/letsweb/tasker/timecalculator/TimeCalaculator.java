package biz.letsweb.tasker.timecalculator;

import java.sql.Timestamp;
import java.util.Date;
import org.joda.time.DateTime;
import org.joda.time.DateTimeZone;

/**
 *
 * @author toks
 */
public class TimeCalaculator implements TimeCalculateable{

    @Override
    public Date calculateDurationFromPravious(Timestamp ts) {
        DateTime dateTime = new DateTime(ts, DateTimeZone.forID("CET"));
        
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
    
}
