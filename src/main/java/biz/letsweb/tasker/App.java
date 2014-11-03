package biz.letsweb.tasker;

import biz.letsweb.tasker.configuration.ConfigurationProvider;
import biz.letsweb.tasker.database.DerbyPooledDataSourceMaker;
import java.sql.Connection;
import java.sql.SQLException;
import javax.sql.PooledConnection;
import org.apache.commons.configuration.SubnodeConfiguration;
import org.apache.commons.configuration.XMLConfiguration;
import org.apache.derby.jdbc.ClientConnectionPoolDataSource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class App {

    public static final Logger log = LoggerFactory.getLogger(App.class);

    public App() {
    }

    public static void main(String[] args) {
        log.info("Tasker starts.");
        log.info("Get configuration.");

        final ConfigurationProvider configurationProvider = new ConfigurationProvider("config/configuration.xml");
        final XMLConfiguration xmlConfiguration = configurationProvider.getXMLConfiguration();
        final SubnodeConfiguration xmlConfig = xmlConfiguration.configurationAt("database");
        final String user = xmlConfig.getString("user");
        final String password = xmlConfig.getString("password");
        final String databaseName = xmlConfig.getString("databaseName");
        final String serverName = xmlConfig.getString("serverName");
        final int portNumber = xmlConfig.getInt("portNumber");
        final String create = xmlConfig.getString("create");
        log.info("{}, {}, {}, {}, {}, {}", user, password, databaseName, serverName, portNumber, create);
        
        final DerbyPooledDataSourceMaker derbyPooledDataSourceMaker = new DerbyPooledDataSourceMaker(user, password, databaseName, serverName, portNumber, create);
        final ClientConnectionPoolDataSource clientConnectionPoolDataSource = derbyPooledDataSourceMaker.getClientConnectionPoolDataSource();

        try {
            final PooledConnection pooledConnection = clientConnectionPoolDataSource.getPooledConnection();
            final Connection con = pooledConnection.getConnection();
            // do stuff
            log.info("Perform db operation.");
            con.close();
            pooledConnection.close();
        } catch (SQLException ex) {
            log.error("Application couldn't get a connection from the pool. ", ex);
        }
    }
}
