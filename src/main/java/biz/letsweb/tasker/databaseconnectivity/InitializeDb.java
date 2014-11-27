package biz.letsweb.tasker.databaseconnectivity;

import biz.letsweb.tasker.configuration.ConfigurationProvider;
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

    public InitializeDb(DataSource dataSource) {
        initializeXmlConfiguration();
        this.dataSource = dataSource;
    }

    private void initializeXmlConfiguration() {
        ConfigurationProvider configurationProvider
                = new ConfigurationProvider("config/configuration.xml");
        final XMLConfiguration xmlConfiguration = configurationProvider.getXMLConfiguration();
        xmlConfig = xmlConfiguration.configurationAt("database/initializeTables");
    }

    public Feedback createTables() throws SQLException {
        Feedback feedback = Feedback.TABLES_FAILED;
        PreparedStatement ps = null;
        try (Connection connection = dataSource.getConnection()) {
            for (Object createSql : xmlConfig.getList("initializeTable")) {
                ps = connection.prepareStatement(createSql.toString());
                ps.execute();
            }
            feedback = Feedback.TABLES_CREATED;
        } catch (SQLTransactionRollbackException e) {
            if (e.getMessage().contains(TableNames.CHRONICLE.name()) || e.getMessage().contains(TableNames.COMMENT.name())) {
                feedback = Feedback.TABLES_EXISTED;
            }
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
        TABLES_FAILED
    }
}
