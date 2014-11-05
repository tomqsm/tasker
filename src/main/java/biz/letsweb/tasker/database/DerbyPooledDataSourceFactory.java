package biz.letsweb.tasker.database;

import biz.letsweb.tasker.configuration.ConfigurationProvider;
import java.sql.SQLException;
import javax.sql.PooledConnection;
import org.apache.derby.jdbc.ClientConnectionPoolDataSource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 * @author Tomasz
 */
public class DerbyPooledDataSourceFactory {

  public static final Logger log = LoggerFactory.getLogger(DerbyPooledDataSourceFactory.class);

  final ConfigurationProvider configurationProvider = new ConfigurationProvider(
      "config/configuration.xml");
  final DerbyPooledDataSourceMaker derbyPooledDataSourceMaker = new DerbyPooledDataSourceMaker(
      configurationProvider.getXMLConfiguration());
  final ClientConnectionPoolDataSource clientConnectionPoolDataSource = derbyPooledDataSourceMaker
      .getClientConnectionPoolDataSource();
  private PooledConnection pooledConnection;

  public DerbyPooledDataSourceFactory() {
    initializeConnections();
  }

  public PooledConnection getPooledConnection() {
    return pooledConnection;
  }

  final public void initializeConnections() {
    try {
      pooledConnection = clientConnectionPoolDataSource.getPooledConnection();
    } catch (SQLException ex) {
      log.error("Error while initialising pooled connections.", ex);
    }
  }

}
