package biz.letsweb.tasker.services;

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
public class FindLastNEntriesService implements Serviceable<List<ChronicleRecordLine>>{
    public static final Logger log = LoggerFactory.getLogger(FindLastNEntriesService.class);
    private final List<ChronicleRecordLine> entries;
    private static final int ROWS = 2;

    public FindLastNEntriesService() {
        this.entries = new ArrayList<>();
    }

    @Override
    public void execute(Connection con) {
        //find current task
        PreparedStatement ps;
        ChronicleRecordLine currentEntry;
        try {
            final String currentSql = String.format("select * from (select ROW_NUMBER() OVER() as CNT, chronicle.* from chronicle) AS CR where CNT > (select count (*) from chronicle) - %d", ROWS);
            ps = con.prepareStatement(currentSql);
            final ResultSet currentResultSet = ps.executeQuery();
            while (currentResultSet.next()) {
                currentEntry = new ChronicleRecordLine();
                currentEntry.setId(currentResultSet.getInt("id"));
                currentEntry.setCount(currentResultSet.getInt("cnt"));
                currentEntry.setTag(currentResultSet.getString("tag"));
                currentEntry.setDescription(currentResultSet.getString("description"));
                currentEntry.setTimestamp(currentResultSet.getTimestamp("inserted"));
                entries.add(currentEntry);
            }
        } catch (SQLException ex) {
            log.error("Application couldn't get a connection from the pool. ", ex);
        }
    }

    @Override
    public List<ChronicleRecordLine> getData() {
        return entries;
    }

    @Override
    public void setData(List<ChronicleRecordLine> entry) {
        throw new UnsupportedOperationException("Not supported yet.");
    }
    
}
