package biz.letsweb.tasker.services;

import biz.letsweb.tasker.persistence.model.ChronicleRecordLineCopier;
import biz.letsweb.tasker.persistence.model.ChronicleRecordLine;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 * @author toks
 */
public class FindCurrentEntryService implements Serviceable<ChronicleRecordLine> {

    public static final Logger log = LoggerFactory.getLogger(FindCurrentEntryService.class);
    private final ChronicleRecordLine currentEntry;

    public FindCurrentEntryService() {
        currentEntry = new ChronicleRecordLine();
    }

    @Override
    public void execute(Connection con) {
        //find current task
        PreparedStatement ps;
        try {
            final String currentSql = "select * from chronicle where id=(select max(id) from chronicle)";
            ps = con.prepareStatement(currentSql);
            final ResultSet currentResultSet = ps.executeQuery();
            while (currentResultSet.next()) {
                currentEntry.setId(currentResultSet.getInt("id"));
                currentEntry.setTag(currentResultSet.getString("tag"));
                currentEntry.setDescription(currentResultSet.getString("description"));
                currentEntry.setTimestamp(currentResultSet.getTimestamp("inserted"));
            }
        } catch (SQLException ex) {
            log.error("Application couldn't get a connection from the pool. ", ex);
        }
    }

    @Override
    public ChronicleRecordLine getEntry() {
        return currentEntry;
    }

    @Override
    public void setEntry(ChronicleRecordLine entry) {
        new ChronicleRecordLineCopier().copyEntry(entry, currentEntry);
    }
}
