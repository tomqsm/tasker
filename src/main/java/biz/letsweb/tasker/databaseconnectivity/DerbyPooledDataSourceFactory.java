package biz.letsweb.tasker.databaseconnectivity;

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
    private DerbyPooledDataSourceMaker derbyPooledDataSourceMaker;
    private ClientConnectionPoolDataSource clientConnectionPoolDataSource;
    private PooledConnection pooledConnection;

    public DerbyPooledDataSourceFactory() {
        initializeConnections(false);
    }

    public DerbyPooledDataSourceFactory(boolean useConfig) {
        initializeConnections(useConfig);
    }

    public PooledConnection getPooledConnection() {
        return pooledConnection;
    }

    final public void initializeConnections(boolean useConfig) {
        if (useConfig) {
            derbyPooledDataSourceMaker = new DerbyPooledDataSourceMaker(configurationProvider.getXMLConfiguration());
        } else {
            derbyPooledDataSourceMaker = new DerbyPooledDataSourceMaker();
        }
        clientConnectionPoolDataSource = derbyPooledDataSourceMaker.getClientConnectionPoolDataSource();
        try {
            pooledConnection = clientConnectionPoolDataSource.getPooledConnection();
        } catch (SQLException ex) {
            log.error("Error while initialising pooled connections.", ex);
        }
        clientConnectionPoolDataSource = derbyPooledDataSourceMaker
            .getClientConnectionPoolDataSource();
    }

}
