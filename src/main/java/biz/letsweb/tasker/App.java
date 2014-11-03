package biz.letsweb.tasker;

import biz.letsweb.tasker.configuration.ConfigurationProvider;
import biz.letsweb.tasker.database.DerbyPooledDataSourceMaker;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import javax.sql.PooledConnection;
import org.apache.derby.jdbc.ClientConnectionPoolDataSource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class App {

  public static final Logger log = LoggerFactory.getLogger(App.class);

  public App() {}

  public static void main(String[] args) {
    log.info("Tasker starts.");
    log.info("Get configuration.");

    final ConfigurationProvider configurationProvider =
        new ConfigurationProvider("config/configuration.xml");
    final DerbyPooledDataSourceMaker derbyPooledDataSourceMaker =
        new DerbyPooledDataSourceMaker(configurationProvider.getXMLConfiguration());
    final ClientConnectionPoolDataSource clientConnectionPoolDataSource =
        derbyPooledDataSourceMaker.getClientConnectionPoolDataSource();

    try {
      final PooledConnection pooledConnection =
          clientConnectionPoolDataSource.getPooledConnection();
      final Connection con = pooledConnection.getConnection();
      final PreparedStatement ps = con.prepareStatement("select * from test.prices");
      final ResultSet resultSet = ps.executeQuery();
      while (resultSet.next()) {
        log.info("{}", resultSet.getString("service"));

      }
      // do stuff
      log.info("Perform db operation.");
      ps.close();
      con.close();
      pooledConnection.close();
    } catch (SQLException ex) {
      log.error("Application couldn't get a connection from the pool. ", ex);
    }
  }
}
