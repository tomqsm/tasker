package biz.letsweb.tasker.services;

import biz.letsweb.tasker.persistence.model.ChronicleRecordLine;
import biz.letsweb.tasker.timing.DayBoundsTimestamp;
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
public class FindTodayEntriesService implements Serviceable<List<ChronicleRecordLine>> {

  public static final Logger log = LoggerFactory.getLogger(FindTodayEntriesService.class);
  private final List<ChronicleRecordLine> entries;

  public FindTodayEntriesService() {
        this.entries = new ArrayList<>();
    }

  @Override
  public void execute(Connection con) {
    DayBoundsTimestamp boundsTimestamp = new DayBoundsTimestamp();
    // find current task
    PreparedStatement ps;
    ChronicleRecordLine currentEntry;
    try {
      final String currentSql =
          String.format("select * from chronicle where inserted between %s and %s",
              boundsTimestamp.getStartOfTodayTimestamp(), boundsTimestamp.getEndOfTodayTimestamp());
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
