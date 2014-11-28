package biz.letsweb.tasker.databaseconnectivity;

import java.io.File;
import javax.sql.DataSource;
import org.apache.commons.configuration.SubnodeConfiguration;
import org.apache.commons.configuration.XMLConfiguration;
import org.apache.derby.jdbc.ClientDataSource;
import org.apache.derby.jdbc.EmbeddedDataSource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 * @author toks
 */
public class DataSourceFactory {

  public static final Logger log = LoggerFactory.getLogger(DataSourceFactory.class);

  public static final File ROOT_FOLDER = new File("");
  public static String DB_NAME = "testdb";
  public static int PORT = 1527;
  public String db = String.format("%s%s%s", ROOT_FOLDER.getAbsolutePath(), File.separator, DB_NAME);
  public static String USER = "tumcyk";
  public static String PASSWORD = "mypass";
  public static String SERVER_NAME = "localhost";
  public static boolean CREATE = true;
  private DataSource dataSource;

  public DataSourceFactory(XMLConfiguration xmlConfiguration) {
    final SubnodeConfiguration xmlConfig = xmlConfiguration.configurationAt("database");
    USER = xmlConfig.getString("user");
    PASSWORD = xmlConfig.getString("password");
    DB_NAME = xmlConfig.getString("databaseName");
    SERVER_NAME = xmlConfig.getString("serverName");
    PORT = xmlConfig.getInt("portNumber");
    CREATE = xmlConfig.getBoolean("create");
    db = String.format("%s%s%s", ROOT_FOLDER.getAbsolutePath(), File.separator, DB_NAME);
    final String type = xmlConfig.getString("type").toUpperCase();
    initialize(Type.valueOf(type));
  }

//  public DataSourceFactory(Type type) {
//    initialize(type);
//  }

  private void initialize(Type type) {
    if (type == Type.CLIENT) {
      dataSource = initializeClient();
    } else if (type == Type.EMBEDDED) {
      dataSource = initializeEmbedded();
    } else {

    }
  }

  private DataSource initializeEmbedded() {
    EmbeddedDataSource ds = new EmbeddedDataSource();
    ds.setDatabaseName(db);
    if (CREATE) {
      ds.setCreateDatabase("create");
    }
    log.debug("Initialized enbedded db in: {}", db);
    return ds;
  }

  private DataSource initializeClient() {
    ClientDataSource ds = new ClientDataSource();
    ds.setDatabaseName(db);
    // ds.setUser(USER); //when uncommented it wants TUMCYK schema
    // ds.setPassword(PASSWORD);
    ds.setServerName(SERVER_NAME);
    if (CREATE) {
      ds.setCreateDatabase("create");
    }
    ds.setPortNumber(PORT);
    log.debug("Initialized client db in: {}", db);
    return ds;
  }


  public DataSource getDataSource() {
    return dataSource;
  }

  public void setDataSource(DataSource dataSource) {
    this.dataSource = dataSource;
  }

  public enum Type {

    CLIENT, EMBEDDED
  }
}
