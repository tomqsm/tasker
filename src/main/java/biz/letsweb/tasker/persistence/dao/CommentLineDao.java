package biz.letsweb.tasker.persistence.dao;

import biz.letsweb.tasker.persistence.model.CommentLine;
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
public class CommentLineDao {

    public static final Logger log = LoggerFactory.getLogger(CommentLineDao.class);
    private final PooledConnection pooledConnection;

    public CommentLineDao(PooledConnection pooledConnection) {
        this.pooledConnection = pooledConnection;
    }

    public CommentLine findLastRecord() {
        final CommentLine record = new CommentLine();
        final String currentSql = "select * from comment where id=(select max(id) from comment)";
        try (Connection con = pooledConnection.getConnection();
                PreparedStatement ps = con.prepareStatement(currentSql);
                ResultSet rs = ps.executeQuery();) {
            while (rs.next()) {
                record.setId(rs.getInt("id"));
                record.setChronicleId(rs.getInt("chronicleId"));
                record.setDescription(rs.getString("description"));
                record.setTimestamp(rs.getTimestamp("inserted"));
            }
        } catch (SQLException ex) {
            log.error("Application couldn't get a connection from the pool. ", ex);
        }
        return record;
    }

    public CommentLine findLastButOneRecord() {
        //find current task
        final CommentLine record = new CommentLine();
        final String currentSql = "select * from (select ROW_NUMBER() OVER() as CNT, comment.* from comment) AS CR where CNT > (select count (*) from comment) - 2 order by CNT desc offset 1 rows";
        try (Connection con = pooledConnection.getConnection();
                PreparedStatement ps = con.prepareStatement(currentSql);
                ResultSet rs = ps.executeQuery();) {
            while (rs.next()) {
                record.setId(rs.getInt("id"));
                record.setChronicleId(rs.getInt("chronicleId"));
                record.setDescription(rs.getString("description"));
                record.setTimestamp(rs.getTimestamp("inserted"));
            }
        } catch (SQLException ex) {
            log.error("Application couldn't get a connection from the pool. ", ex);
        }
        return record;
    }

    public List<CommentLine> findAllRecords() {
        //find current task
        final List<CommentLine> recordLines = new ArrayList<>();
        try (Connection con = pooledConnection.getConnection();
                PreparedStatement ps = con.prepareStatement("select * from comment");
                ResultSet rs = ps.executeQuery();) {
            while (rs.next()) {
                final CommentLine currentEntry = new CommentLine();
                currentEntry.setId(rs.getInt("id"));
                currentEntry.setDescription(rs.getString("description"));
                currentEntry.setTimestamp(rs.getTimestamp("inserted"));
                recordLines.add(currentEntry);
            }
        } catch (SQLException ex) {
            log.error("Application couldn't get a connection from the pool. ", ex);
        }
        return recordLines;
    }

    public List<CommentLine> findAllRecordsWithCount() {
        //find current task
        final List<CommentLine> recordLines = new ArrayList<>();
        try (Connection con = pooledConnection.getConnection();
                PreparedStatement ps = con.prepareStatement("select * from (select ROW_NUMBER() OVER() as CNT, comment.* from comment) AS CR");
                ResultSet rs = ps.executeQuery();) {
            while (rs.next()) {
                final CommentLine entry = new CommentLine();
                entry.setId(rs.getInt("id"));
                entry.setCount(rs.getInt("cnt"));
                entry.setDescription(rs.getString("description"));
                entry.setTimestamp(rs.getTimestamp("inserted"));
                recordLines.add(entry);
            }
        } catch (SQLException ex) {
            log.error("Application couldn't get a connection from the pool. ", ex);
        }
        return recordLines;
    }

    public List<CommentLine> findTodaysRecords() {
        final DayBoundsTimestamp boundsTimestamp = new DayBoundsTimestamp();
        final List<CommentLine> recordLines = new ArrayList<>();
        final String sql = String.format("select * from (select ROW_NUMBER() OVER() as CNT, comment.* from comment) AS CR where inserted between '%s' and '%s'",
                boundsTimestamp.getStartOfTodayTimestamp().toString(), boundsTimestamp.getEndOfTodayTimestamp().toString());
        log.info("{}", sql);
        try (Connection con = pooledConnection.getConnection();
                PreparedStatement ps = con.prepareStatement(sql);
                ResultSet rs = ps.executeQuery();) {
            while (rs.next()) {
                final CommentLine entry = new CommentLine();
                entry.setId(rs.getInt("id"));
                entry.setCount(rs.getInt("cnt"));
                entry.setDescription(rs.getString("description"));
                entry.setTimestamp(rs.getTimestamp("inserted"));
                recordLines.add(entry);
            }
        } catch (SQLException ex) {
            log.error("Application couldn't get a connection from the pool. ", ex);
        }
        return recordLines;
    }

    public boolean insertNewRecord(CommentLine recordLine) {
        boolean isInserted = false;
        try (Connection con = pooledConnection.getConnection();
                PreparedStatement ps = con.prepareStatement("insert into comment (chronicleId, description) values (?, ?)");) {
            ps.setInt(1, recordLine.getChronicleId());
            ps.setString(2, recordLine.getDescription());
            isInserted = ps.execute();
        } catch (SQLException ex) {
            log.error("Application couldn't get a connection from the pool. ", ex);
        }
        return isInserted;
    }


}
