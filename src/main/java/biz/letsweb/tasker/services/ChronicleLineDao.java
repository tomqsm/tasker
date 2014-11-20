package biz.letsweb.tasker.services;

import biz.letsweb.tasker.persistence.model.ChronicleRecordLine;
import biz.letsweb.tasker.timing.DayBoundsTimestamp;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import javax.sql.PooledConnection;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 * @author toks
 */
public class ChronicleLineDao {

    public static final Logger log = LoggerFactory.getLogger(ChronicleLineDao.class);
    private final PooledConnection pooledConnection;

    public ChronicleLineDao(PooledConnection pooledConnection) {
        this.pooledConnection = pooledConnection;
    }

    public ChronicleRecordLine findLastRecord() {
        final ChronicleRecordLine record = new ChronicleRecordLine();
        final String currentSql = "select * from chronicle where id=(select max(id) from chronicle)";
        try (Connection con = pooledConnection.getConnection();
                PreparedStatement ps = con.prepareStatement(currentSql);
                ResultSet currentResultSet = ps.executeQuery();) {
            while (currentResultSet.next()) {
                record.setId(currentResultSet.getInt("id"));
                record.setTag(currentResultSet.getString("tag"));
                record.setDescription(currentResultSet.getString("description"));
                record.setTimestamp(currentResultSet.getTimestamp("inserted"));
            }
        } catch (SQLException ex) {
            log.error("Application couldn't get a connection from the pool. ", ex);
        }
        return record;
    }

    public ChronicleRecordLine findLastButOneRecord() {
        //find current task
        final ChronicleRecordLine record = new ChronicleRecordLine();
        final String currentSql = "select * from (select ROW_NUMBER() OVER() as CNT, chronicle.* from chronicle) AS CR where CNT > (select count (*) from chronicle) - 2 order by CNT desc offset 1 rows";
        try (Connection con = pooledConnection.getConnection();
                PreparedStatement ps = con.prepareStatement(currentSql);
                ResultSet rs = ps.executeQuery();) {
            while (rs.next()) {
                record.setId(rs.getInt("id"));
                record.setTag(rs.getString("tag"));
                record.setDescription(rs.getString("description"));
                record.setTimestamp(rs.getTimestamp("inserted"));
            }
        } catch (SQLException ex) {
            log.error("Application couldn't get a connection from the pool. ", ex);
        }
        return record;
    }

    public List<ChronicleRecordLine> findAllRecords() {
        //find current task
        final List<ChronicleRecordLine> recordLines = new ArrayList<>();
        try (Connection con = pooledConnection.getConnection();
                PreparedStatement ps = con.prepareStatement("select * from chronicle");
                ResultSet rs = ps.executeQuery();) {
            while (rs.next()) {
                final ChronicleRecordLine currentEntry = new ChronicleRecordLine();
                currentEntry.setId(rs.getInt("id"));
                currentEntry.setTag(rs.getString("tag"));
                currentEntry.setDescription(rs.getString("description"));
                currentEntry.setTimestamp(rs.getTimestamp("inserted"));
                recordLines.add(currentEntry);
            }
        } catch (SQLException ex) {
            log.error("Application couldn't get a connection from the pool. ", ex);
        }
        return recordLines;
    }

    public List<ChronicleRecordLine> findAllRecordsWithCount() {
        //find current task
        final List<ChronicleRecordLine> recordLines = new ArrayList<>();
        try (Connection con = pooledConnection.getConnection();
                PreparedStatement ps = con.prepareStatement("select * from (select ROW_NUMBER() OVER() as CNT, chronicle.* from chronicle) AS CR");
                ResultSet rs = ps.executeQuery();) {
            while (rs.next()) {
                final ChronicleRecordLine entry = new ChronicleRecordLine();
                entry.setId(rs.getInt("id"));
                entry.setCount(rs.getInt("cnt"));
                entry.setTag(rs.getString("tag"));
                entry.setDescription(rs.getString("description"));
                entry.setTimestamp(rs.getTimestamp("inserted"));
                recordLines.add(entry);
            }
        } catch (SQLException ex) {
            log.error("Application couldn't get a connection from the pool. ", ex);
        }
        return recordLines;
    }

    public List<ChronicleRecordLine> findTodaysRecords() {
        final DayBoundsTimestamp boundsTimestamp = new DayBoundsTimestamp();
        final List<ChronicleRecordLine> recordLines = new ArrayList<>();
        final String sql = String.format("select * from chronicle where inserted between '%s' and '%s'",
                boundsTimestamp.getStartOfTodayTimestamp().toString(), boundsTimestamp.getEndOfTodayTimestamp().toString());
        log.info("{}", sql);
        try (Connection con = pooledConnection.getConnection();
                PreparedStatement ps = con.prepareStatement(sql);
                ResultSet rs = ps.executeQuery();) {
            while (rs.next()) {
                final ChronicleRecordLine entry = new ChronicleRecordLine();
                entry.setId(rs.getInt("id"));
                entry.setTag(rs.getString("tag"));
                entry.setDescription(rs.getString("description"));
                entry.setTimestamp(rs.getTimestamp("inserted"));
                recordLines.add(entry);
            }
        } catch (SQLException ex) {
            log.error("Application couldn't get a connection from the pool. ", ex);
        }
        return recordLines;
    }

    public boolean insertNewRecord(ChronicleRecordLine recordLine) {
        boolean isInserted = false;
        try (Connection con = pooledConnection.getConnection();
                PreparedStatement ps = con.prepareStatement("insert into chronicle (tag, description) values (?, ?)");) {
            ps.setString(1, recordLine.getTag());
            ps.setString(2, recordLine.getDescription());
            isInserted = ps.execute();
        } catch (SQLException ex) {
            log.error("Application couldn't get a connection from the pool. ", ex);
        }
        return isInserted;
    }

}