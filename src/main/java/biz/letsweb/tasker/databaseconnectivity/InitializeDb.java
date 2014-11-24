package biz.letsweb.tasker.databaseconnectivity;

import biz.letsweb.tasker.configuration.ConfigurationProvider;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.SQLTransactionRollbackException;
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

  private final DataSourcePrepare dataSourcePrepare;
  private final SubnodeConfiguration xmlConfig;

  public InitializeDb() {
    ConfigurationProvider configurationProvider =
        new ConfigurationProvider("config/configuration.xml");
    final XMLConfiguration xmlConfiguration = configurationProvider.getXMLConfiguration();
    xmlConfig = xmlConfiguration.configurationAt("database/initializeTables");
    dataSourcePrepare = new DataSourcePrepare(DataSourcePrepare.Type.CLIENT);
  }

  public void createTables() throws SQLException {
        PreparedStatement ps = null;
        try (Connection connection = dataSourcePrepare.getDataSource().getConnection()) {
            for (Object createSql : xmlConfig.getList("initializeTable")) {
                ps = connection.prepareStatement(createSql.toString());
                ps.execute();
            }
        } catch (SQLTransactionRollbackException e) {
            if (e.getMessage().contains(TableNames.CHRONICLE.name()) || e.getMessage().contains(TableNames.COMMENT.name())) {
                log.warn("{}",Feedback.TABLE_EXISTED);
            }
        } finally {
            if (ps != null) {
                ps.close();
            }
        }
    }

  public void clearTables() throws SQLException {
        PreparedStatement ps = null;
        try (Connection connection = dataSourcePrepare.getDataSource().getConnection()) {
            for (Object createSql : xmlConfig.getList("clearTable")) {
                ps = connection.prepareStatement(createSql.toString());
                ps.execute();
            }
        }
        if (ps != null) {
            ps.close();
        }
    }

  public enum Feedback {

    TABLE_EXISTED, CREATED
  }
}
