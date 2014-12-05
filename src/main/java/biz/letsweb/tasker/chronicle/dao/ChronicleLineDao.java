package biz.letsweb.tasker.chronicle.dao;

import biz.letsweb.tasker.NoRecordsInPoolException;
import biz.letsweb.tasker.UninitialisedTablesException;
import biz.letsweb.tasker.UnsetIdException;
import biz.letsweb.tasker.chronicle.model.ChronicleLine;
import biz.letsweb.tasker.timing.DayBoundsTimestamp;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.SQLSyntaxErrorException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.sql.DataSource;
import org.joda.time.Duration;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 * @author toks
 */
public class ChronicleLineDao {

    public static final Logger log = LoggerFactory.getLogger(ChronicleLineDao.class);
    private final DataSource ds;
    private static final int NOT_EXISTING_MARKER = -1;

    public ChronicleLineDao(DataSource dataSource) {
        this.ds = dataSource;
    }

    public ChronicleLine findLastRecord() throws NoRecordsInPoolException, UninitialisedTablesException {
        ChronicleLine record = null;
        final String currentSql = "select * from (select ROW_NUMBER() OVER() as CNT, chronicle.* from chronicle) AS CR where id=(select max(id) from chronicle)";
        try (Connection con = ds.getConnection();
                PreparedStatement ps = con.prepareStatement(currentSql);
                ResultSet currentResultSet = ps.executeQuery();) {
            while (currentResultSet.next()) {
                record = new ChronicleLine();
                record.setId(currentResultSet.getInt("id"));
                record.setCount(currentResultSet.getInt("cnt"));
                record.setTag(currentResultSet.getString("tag"));
                record.setDescription(currentResultSet.getString("description"));
                record.setTimestamp(currentResultSet.getTimestamp("inserted"));
            }
        } catch (SQLException ex) {
            propagateTablesUninitialisedException(ex);
        }
        if (record == null) {
            throw new NoRecordsInPoolException("Can't return 'last record' because no records in database.");
        }
        return record;
    }

    public List<String> findDistictiveTags() throws NoRecordsInPoolException, UninitialisedTablesException {
        List<String> tags = new ArrayList<>();
        final String currentSql = "select distinct tag from chronicle";
        try (Connection con = ds.getConnection();
                PreparedStatement ps = con.prepareStatement(currentSql);
                ResultSet currentResultSet = ps.executeQuery();) {
            while (currentResultSet.next()) {
                tags.add(currentResultSet.getString("tag"));
            }
        } catch (SQLException ex) {
            propagateTablesUninitialisedException(ex);
        }
        if (tags.isEmpty()) {
            throw new NoRecordsInPoolException("No tags in database.");
        }
        return tags;
    }

    public ChronicleLine findRecordByCount(int cnt) throws UninitialisedTablesException {
        final ChronicleLine record = new ChronicleLine();
        final String currentSql = "select * from (select ROW_NUMBER() OVER() as CNT, chronicle.* from chronicle) AS CR where CNT = ?";
        try (Connection con = ds.getConnection();
                PreparedStatement ps = con.prepareStatement(currentSql);) {
            ps.setInt(1, cnt);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                record.setId(rs.getInt("id"));
                record.setCount(rs.getInt("cnt"));
                record.setTag(rs.getString("tag"));
                record.setDescription(rs.getString("description"));
                record.setTimestamp(rs.getTimestamp("inserted"));
            }
            rs.close();
        } catch (SQLException ex) {
            propagateTablesUninitialisedException(ex);
        }
        return record;
    }

    public ChronicleLine findParentRecordToId(int id) throws UninitialisedTablesException {
        final ChronicleLine record = new ChronicleLine();
        final String currentSql = "SELECT * FROM CHRONICLE WHERE ID = (select PARENTID from CHRONICLE WHERE ID=?)";
        try (Connection con = ds.getConnection();
                PreparedStatement ps = con.prepareStatement(currentSql);) {
            ps.setInt(1, id);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                record.setId(rs.getInt("id"));
                record.setTag(rs.getString("tag"));
                record.setDescription(rs.getString("description"));
                record.setTimestamp(rs.getTimestamp("inserted"));
            }
            rs.close();
        } catch (SQLException ex) {
            propagateTablesUninitialisedException(ex);
        }
        return record;
    }

    public List<ChronicleLine> findAllParentsToId(int id) throws UninitialisedTablesException {
        final List<ChronicleLine> recordLines = new ArrayList<>();
        int currentId = id;
        while ((currentId = findParentId(currentId)) > NOT_EXISTING_MARKER) {
            final ChronicleLine foundParent = findRecordById(currentId);
            recordLines.add(foundParent);
        }
        return recordLines;
    }

    public ChronicleLine findRecordById(int id) throws UninitialisedTablesException {
        final ChronicleLine record = new ChronicleLine();
        final String currentSql = "SELECT * FROM CHRONICLE WHERE ID = ?";
        try (Connection con = ds.getConnection();
                PreparedStatement ps = con.prepareStatement(currentSql);) {
            ps.setInt(1, id);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                record.setId(rs.getInt("id"));
                record.setTag(rs.getString("tag"));
                record.setDescription(rs.getString("description"));
                record.setTimestamp(rs.getTimestamp("inserted"));
            }
            rs.close();
        } catch (SQLException ex) {
            propagateTablesUninitialisedException(ex);
        }
        return record;
    }

    public int findParentId(int id) throws UninitialisedTablesException {
        int parentId = NOT_EXISTING_MARKER;
        final String currentSql = "SELECT id FROM CHRONICLE WHERE ID = (select PARENTID from CHRONICLE WHERE ID=?)";
        try (Connection con = ds.getConnection();
                PreparedStatement ps = con.prepareStatement(currentSql);) {
            ps.setInt(1, id);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                parentId = (rs.getInt("id"));
            }
            rs.close();
        } catch (SQLException ex) {
            propagateTablesUninitialisedException(ex);
        }
        return parentId;
    }

    public ChronicleLine findLastButOneRecord() throws UninitialisedTablesException {
        //find current task
        final ChronicleLine record = new ChronicleLine();
        final String currentSql = "select * from (select ROW_NUMBER() OVER() as CNT, chronicle.* from chronicle) AS CR where CNT > (select count (*) from chronicle) - 2 order by CNT desc offset 1 rows";
        try (Connection con = ds.getConnection();
                PreparedStatement ps = con.prepareStatement(currentSql);
                ResultSet rs = ps.executeQuery();) {
            while (rs.next()) {
                record.setId(rs.getInt("id"));
                record.setTag(rs.getString("tag"));
                record.setDescription(rs.getString("description"));
                record.setTimestamp(rs.getTimestamp("inserted"));
            }
        } catch (SQLException ex) {
            propagateTablesUninitialisedException(ex);
        }
        return record;
    }

    public List<ChronicleLine> findAllRecords() throws UninitialisedTablesException {
        //find current task
        final List<ChronicleLine> recordLines = new ArrayList<>();
        try (Connection con = ds.getConnection();
                PreparedStatement ps = con.prepareStatement("select * from chronicle");
                ResultSet rs = ps.executeQuery();) {
            while (rs.next()) {
                final ChronicleLine currentEntry = new ChronicleLine();
                currentEntry.setId(rs.getInt("id"));
                currentEntry.setTag(rs.getString("tag"));
                currentEntry.setDescription(rs.getString("description"));
                currentEntry.setTimestamp(rs.getTimestamp("inserted"));
                recordLines.add(currentEntry);
            }
        } catch (SQLException ex) {
            propagateTablesUninitialisedException(ex);
        }
        return recordLines;
    }

    public List<ChronicleLine> findLastNRecordsDownwards(int n) throws UninitialisedTablesException {
        final List<ChronicleLine> recordLines = new ArrayList<>();
        try (Connection con = ds.getConnection();
                PreparedStatement ps = con.prepareStatement("select * from (select ROW_NUMBER() OVER() as CNT, chronicle.* from chronicle) AS CR where CNT > (select count (*) from chronicle) - ? order by CNT asc");) {
            ps.setInt(1, n);
            final ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                final ChronicleLine currentEntry = new ChronicleLine();
                currentEntry.setId(rs.getInt("id"));
                currentEntry.setCount(rs.getInt("cnt"));
                currentEntry.setTag(rs.getString("tag"));
                currentEntry.setDescription(rs.getString("description"));
                currentEntry.setTimestamp(rs.getTimestamp("inserted"));
                recordLines.add(currentEntry);
            }
            rs.close();
        } catch (SQLException ex) {
            propagateTablesUninitialisedException(ex);
        }
        return recordLines;
    }

    public List<ChronicleLine> findLastNRecordsUpwards(int n) throws UninitialisedTablesException {
        final List<ChronicleLine> recordLines = new ArrayList<>();
        try (Connection con = ds.getConnection();
                PreparedStatement ps = con.prepareStatement("select * from (select ROW_NUMBER() OVER() as CNT, chronicle.* from chronicle) AS CR where CNT > (select count (*) from chronicle) - ? order by CNT desc");) {
            ps.setInt(1, n);
            final ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                final ChronicleLine currentEntry = new ChronicleLine();
                currentEntry.setId(rs.getInt("id"));
                currentEntry.setCount(rs.getInt("cnt"));
                currentEntry.setTag(rs.getString("tag"));
                currentEntry.setDescription(rs.getString("description"));
                currentEntry.setTimestamp(rs.getTimestamp("inserted"));
                recordLines.add(currentEntry);
            }
            rs.close();
        } catch (SQLException ex) {
            propagateTablesUninitialisedException(ex);
        }
        return recordLines;
    }

    public List<ChronicleLine> findLastNRecordsByTagUpwards(String tag, int n) throws UninitialisedTablesException {
        final List<ChronicleLine> recordLines = new ArrayList<>();
        try (Connection con = ds.getConnection();
                PreparedStatement ps = con.prepareStatement("select * from (select ROW_NUMBER() OVER() as CNT, chronicle.* from chronicle where tag=?) AS CR where CNT > (select count (*) from chronicle where tag=?)-? order by CNT desc");) {
            ps.setString(1, tag);
            ps.setString(2, tag);
            ps.setInt(3, n);
            final ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                final ChronicleLine currentEntry = new ChronicleLine();
                currentEntry.setId(rs.getInt("id"));
                currentEntry.setCount(rs.getInt("cnt"));
                currentEntry.setTag(rs.getString("tag"));
                currentEntry.setDescription(rs.getString("description"));
                currentEntry.setTimestamp(rs.getTimestamp("inserted"));
                recordLines.add(currentEntry);
            }
            rs.close();
        } catch (SQLException ex) {
            propagateTablesUninitialisedException(ex);
        }
        return recordLines;
    }

    public List<ChronicleLine> findLastNNamingRecordsDownwards(int n) throws UninitialisedTablesException {
        final List<ChronicleLine> recordLines = new ArrayList<>();
        try (Connection con = ds.getConnection();
                PreparedStatement ps = con.prepareStatement("select * from (select ROW_NUMBER() OVER() as CNT, chronicle.* from chronicle where tag!='work' and tag!='break') AS CR where CNT > (select count (*) from chronicle where tag!='work' and tag!='break')-? order by CNT asc");) {
            ps.setInt(1, n);
            final ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                final ChronicleLine currentEntry = new ChronicleLine();
                currentEntry.setId(rs.getInt("id"));
                currentEntry.setCount(rs.getInt("cnt"));
                currentEntry.setTag(rs.getString("tag"));
                currentEntry.setDescription(rs.getString("description"));
                currentEntry.setTimestamp(rs.getTimestamp("inserted"));
                recordLines.add(currentEntry);
            }
            rs.close();
        } catch (SQLException ex) {
            propagateTablesUninitialisedException(ex);
        }
        return recordLines;
    }

    public List<ChronicleLine> findLastNRecordsTodayUpwards(int n) throws UninitialisedTablesException {
        final DayBoundsTimestamp boundsTimestamp = new DayBoundsTimestamp();
        final List<ChronicleLine> recordLines = new ArrayList<>();
        try (Connection con = ds.getConnection();
                PreparedStatement ps = con.prepareStatement("select * from (select ROW_NUMBER() OVER() as CNT, chronicle.* from chronicle) AS CR where CNT > (select count (*) from chronicle) - ? and inserted between ? and ? order by CNT desc");) {
            ps.setInt(1, n);
            ps.setTimestamp(2, boundsTimestamp.getStartOfTodayTimestamp());
            ps.setTimestamp(3, boundsTimestamp.getEndOfTodayTimestamp());
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                final ChronicleLine currentEntry = new ChronicleLine();
                currentEntry.setId(rs.getInt("id"));
                currentEntry.setCount(rs.getInt("cnt"));
                currentEntry.setTag(rs.getString("tag"));
                currentEntry.setDescription(rs.getString("description"));
                currentEntry.setTimestamp(rs.getTimestamp("inserted"));
                recordLines.add(currentEntry);
            }
            rs.close();
        } catch (SQLException ex) {
            propagateTablesUninitialisedException(ex);
        }
        return recordLines;
    }

    public List<ChronicleLine> findAllRecordsWithCount() throws UninitialisedTablesException {
        final List<ChronicleLine> recordLines = new ArrayList<>();
        try (Connection con = ds.getConnection();
                PreparedStatement ps = con.prepareStatement("select * from (select ROW_NUMBER() OVER() as CNT, chronicle.* from chronicle) AS CR");
                ResultSet rs = ps.executeQuery();) {
            while (rs.next()) {
                final ChronicleLine entry = new ChronicleLine();
                entry.setId(rs.getInt("id"));
                entry.setCount(rs.getInt("cnt"));
                entry.setTag(rs.getString("tag"));
                entry.setDescription(rs.getString("description"));
                entry.setTimestamp(rs.getTimestamp("inserted"));
                recordLines.add(entry);
            }
        } catch (SQLException ex) {
            propagateTablesUninitialisedException(ex);
        }
        return recordLines;
    }

    public List<ChronicleLine> findTodaysRecords() throws UninitialisedTablesException {
        final DayBoundsTimestamp boundsTimestamp = new DayBoundsTimestamp();
        final List<ChronicleLine> recordLines = new ArrayList<>();
        final String sql = String.format("select * from (select ROW_NUMBER() OVER() as CNT, chronicle.* from chronicle where inserted between '%s' and '%s') AS CR",
                boundsTimestamp.getStartOfTodayTimestamp().toString(), boundsTimestamp.getEndOfTodayTimestamp().toString());
        try (Connection con = ds.getConnection();
                PreparedStatement ps = con.prepareStatement(sql);
                ResultSet rs = ps.executeQuery();) {
            while (rs.next()) {
                final ChronicleLine entry = new ChronicleLine();
                entry.setId(rs.getInt("id"));
                entry.setCount(rs.getInt("cnt"));
                entry.setTag(rs.getString("tag"));
                entry.setDescription(rs.getString("description"));
                entry.setTimestamp(rs.getTimestamp("inserted"));
                recordLines.add(entry);
            }
        } catch (SQLException ex) {
            propagateTablesUninitialisedException(ex);
        }
        return recordLines;
    }

    private void propagateTablesUninitialisedException(SQLException ex) throws UninitialisedTablesException {
        boolean notReachableClient = (ex instanceof SQLSyntaxErrorException) && (ex.getMessage().contains("CHRONICLE") || ex.getMessage().contains("COMMENT"));
        boolean notReachableEmbedded = (ex instanceof SQLException) && ex.getMessage().contains("Failed to start database");
        if (notReachableClient || notReachableEmbedded) {
            log.warn("Detected no tables exists.");
            throw new UninitialisedTablesException();
        } else {
            log.error("Application couldn't get a connection from the pool. ", ex);
        }
    }

    public Map<String, Duration> findDurationsOfTodaysRecords() throws UninitialisedTablesException {
        final Map<String, Duration> durations = new HashMap<>();
        final DayBoundsTimestamp boundsTimestamp = new DayBoundsTimestamp();
        final String sql = String.format("select * from (select ROW_NUMBER() OVER() as CNT, chronicle.* from chronicle) AS CR where inserted between '%s' and '%s'",
                boundsTimestamp.getStartOfTodayTimestamp().toString(), boundsTimestamp.getEndOfTodayTimestamp().toString());
        try (Connection con = ds.getConnection();
                PreparedStatement ps = con.prepareStatement(sql);
                ResultSet rs = ps.executeQuery();) {
            Timestamp timestampPrevious = null;
            while (rs.next()) {
                final String tag = rs.getString("tag");
                final Timestamp timestamp = rs.getTimestamp("inserted");
                if (durations.containsKey(tag)) {
                    final Duration duration = durations.get(tag);
                    final Duration plus = duration.plus(timestamp.getTime() - timestampPrevious.getTime());
                    durations.put(tag, plus);
                } else {
                    timestampPrevious = timestamp;
                    Duration duration = new Duration(0);
                    durations.put(tag, duration);
                }
            }
        } catch (SQLException ex) {
            propagateTablesUninitialisedException(ex);
        }
        return durations;
    }

    public int findRecordsCount() throws UninitialisedTablesException {
        int count = -1;
        final String sql = "select count(*) as cnt from chronicle";
        try (Connection con = ds.getConnection();
                PreparedStatement ps = con.prepareStatement(sql);
                ResultSet rs = ps.executeQuery();) {
            while (rs.next()) {
                count = rs.getInt("cnt");
            }
        } catch (SQLException ex) {
            propagateTablesUninitialisedException(ex);
        }
        return count;
    }

    public int insertNewRecord(ChronicleLine recordLine) {
        int rowNr = 0;
        String sql = "insert into chronicle (parentId, tag, description) values (?, ?, ?)";
        boolean hasInserted = false;
        Timestamp timestamp = recordLine.getTimestamp();
        if (timestamp != null) {
            sql = "insert into chronicle (parentId, tag, description, inserted) values (?, ?, ?, ?)";
            hasInserted = true;
        }
        try (Connection con = ds.getConnection();
                PreparedStatement ps = con.prepareStatement(sql);) {
            ps.setInt(1, recordLine.getParentId());
            ps.setString(2, recordLine.getTag());
            ps.setString(3, recordLine.getDescription());
            if (hasInserted) {
                ps.setTimestamp(4, timestamp);
            }
            rowNr = ps.executeUpdate();
        } catch (SQLException ex) {
            log.error("Application couldn't get a connection from the pool. ", ex);
        }
        return rowNr;
    }

    public int deleteRecord(ChronicleLine recordLine) throws UnsetIdException {
        int rowNr = 0;
        final int idToDelete = recordLine.getId();
        if (idToDelete == 0) {
            throw new UnsetIdException("Unset id: " + idToDelete);
        }
        try (Connection con = ds.getConnection();
                PreparedStatement ps = con.prepareStatement("delete from chronicle where id = ?");) {
            ps.setInt(1, idToDelete);
            rowNr = ps.executeUpdate();
        } catch (SQLException ex) {
            log.error("Application couldn't get a connection from the pool. ", ex);
        }
        return rowNr;
    }
}
