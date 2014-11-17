package biz.letsweb.tasker.services;

import biz.letsweb.tasker.persistence.model.ChronicleRecordLineCopier;
import biz.letsweb.tasker.persistence.model.ChronicleRecordLine;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 * @author toks
 */
public class InsertTagAndDescription implements Serviceable<ChronicleRecordLine> {

    private static final Logger log = LoggerFactory.getLogger(InsertTagAndDescription.class);
    private final static String sql = "insert into chronicle (tag, description) values (?, ?)";

    private final ChronicleRecordLine entry;

    public InsertTagAndDescription() {
        entry = new ChronicleRecordLine();
    }

    @Override
    public void execute(Connection con) {
        PreparedStatement ps;
        try {
            ps = con.prepareStatement(sql);
            ps.setString(1, entry.getTag());
            ps.setString(2, entry.getDescription());
            ps.execute();
        } catch (SQLException ex) {
            log.error("Application couldn't get a connection from the pool. ", ex);
        }

    }

    @Override
    public ChronicleRecordLine getEntry() {
        return entry;
    }

    @Override
    public void setEntry(ChronicleRecordLine entry) {
        new ChronicleRecordLineCopier().copyEntry(entry, this.entry);
    }

}
