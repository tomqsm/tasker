package biz.letsweb.tasker;

import biz.letsweb.tasker.database.DerbyPooledDataSourceFactory;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import javax.sql.PooledConnection;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class App {

  public static final Logger log = LoggerFactory.getLogger(App.class);

  public App() {}

  public static void main(String[] args) {
    log.info("Tasker starts.");
    DerbyPooledDataSourceFactory dataSourceFactory = new DerbyPooledDataSourceFactory();
    final PooledConnection pooledConnection = dataSourceFactory.getPooledConnection();
    try {
      final Connection con = pooledConnection.getConnection();
      final String sql = "select * from test.prices";
      final PreparedStatement ps = con.prepareStatement(sql);
      final ResultSet resultSet = ps.executeQuery();
      while (resultSet.next()) {
        log.info("{}", resultSet.getString("service"));
      }
      ps.close();
      con.close();
      pooledConnection.close();
    } catch (SQLException ex) {
      log.error("Application couldn't get a connection from the pool. ", ex);
    }
  }
}
