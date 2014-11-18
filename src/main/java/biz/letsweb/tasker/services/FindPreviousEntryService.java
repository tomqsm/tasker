package biz.letsweb.tasker.services;

import biz.letsweb.tasker.persistence.model.ChronicleRecordLineCopier;
import biz.letsweb.tasker.persistence.model.ChronicleRecordLine;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 * @author toks
 */
public class FindPreviousEntryService implements Serviceable<ChronicleRecordLine>{
    public static final Logger log = LoggerFactory.getLogger(FindLastNEntriesService.class);
    private final ChronicleRecordLine entry;
    private static final int SKIP_N_FIRST_ROWS = 1;
    private static final int FROM_LAST_N_ROWS = 2;

    public FindPreviousEntryService() {
        this.entry = new ChronicleRecordLine();
    }

    @Override
    public void execute(Connection con) {
        //find current task
        PreparedStatement ps;
        try {
            final String currentSql = String.format("select * from (select ROW_NUMBER() OVER() as CNT, chronicle.* from chronicle) AS CR where CNT > (select count (*) from chronicle) - %d order by CNT desc offset %d rows", FROM_LAST_N_ROWS, SKIP_N_FIRST_ROWS);
            ps = con.prepareStatement(currentSql);
            final ResultSet currentResultSet = ps.executeQuery();
            while (currentResultSet.next()) {
                entry.setId(currentResultSet.getInt("id"));                
                entry.setCount(currentResultSet.getInt("cnt"));
                entry.setTag(currentResultSet.getString("tag"));
                entry.setDescription(currentResultSet.getString("description"));
                entry.setTimestamp(currentResultSet.getTimestamp("inserted"));
            }
        } catch (SQLException ex) {
            log.error("Application couldn't get a connection from the pool. ", ex);
        }
    }

    @Override
    public ChronicleRecordLine getData() {
        return entry;
    }

    @Override
    public void setData(ChronicleRecordLine entry) {
        throw new UnsupportedOperationException("Not supported yet.");
    }
    
}
