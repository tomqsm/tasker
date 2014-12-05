package biz.letsweb.tasker.db;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.SQLTransactionRollbackException;
import javax.sql.DataSource;
import org.apache.commons.configuration.SubnodeConfiguration;
import org.apache.commons.configuration.XMLConfiguration;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 * @author toks
 */
public class InitializeDb {

    public static final Logger log = LoggerFactory.getLogger(InitializeDb.class);

    private final DataSource dataSource;
    private SubnodeConfiguration xmlConfig;

    private InitializeDb(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    public InitializeDb(DataSource dataSource, XMLConfiguration xmlConfiguration) {
        this(dataSource);
        initializeXmlConfiguration(xmlConfiguration);
    }

    private void initializeXmlConfiguration(XMLConfiguration xmlConfiguration) {
        xmlConfig = xmlConfiguration.configurationAt("database/initializeTables");
    }

    public Feedback createTables() {
        Feedback feedback = Feedback.TABLES_FAILED;
        PreparedStatement ps = null;
        try (Connection connection = dataSource.getConnection()) {
            for (Object createSql : xmlConfig.getList("initializeTable")) {
                ps = connection.prepareStatement(createSql.toString());
                ps.execute();
            }
            feedback = Feedback.TABLES_CREATED;
        } catch (SQLException e) {
            feedback = Feedback.TABLES_EXISTED;
        }
        log.warn("{}", feedback);
        return feedback;
    }

    public Feedback runInserts() throws SQLException {
        Feedback feedback = Feedback.RECORDS_INSERTING_FAILED;
        PreparedStatement ps = null;
        try (Connection connection = dataSource.getConnection()) {
            for (Object createSql : xmlConfig.getList("insert")) {
                ps = connection.prepareStatement(createSql.toString());
                ps.execute();
            }
            feedback = Feedback.RECORDS_INSERTED;
        } catch (SQLTransactionRollbackException e) {
            log.error("Error while inserting records: " + e.getMessage());
        } finally {
            if (ps != null) {
                ps.close();
            }
        }
        log.warn("{}", feedback);
        return feedback;
    }

    public Feedback clearTables() throws SQLException {
        Feedback feedback = Feedback.TABLES_FAILED;
        PreparedStatement ps = null;
        try (Connection connection = dataSource.getConnection()) {
            for (Object createSql : xmlConfig.getList("clearTable")) {
                ps = connection.prepareStatement(createSql.toString());
                ps.execute();
            }
            feedback = Feedback.TABLES_CLEARED;
        } finally {
            if (ps != null) {
                ps.close();
            }
        }
        log.warn("{}", feedback);
        return feedback;
    }

    public enum Feedback {

        /**
         * Didn't create tables because they have already been created.
         */
        TABLES_EXISTED,
        /**
         * Created tables ok.
         */
        TABLES_CREATED,
        /**
         * All records have been removed from tables.
         */
        TABLES_CLEARED,
        /**
         * Method execution failed.
         */
        TABLES_FAILED,
        /**
         *
         */
        RECORDS_INSERTED,
        RECORDS_INSERTING_FAILED
    }
}
