package biz.letsweb.tasker.database;

import java.sql.SQLException;
import javax.sql.PooledConnection;
import org.apache.derby.jdbc.ClientConnectionPoolDataSource;
import org.apache.log4j.Logger;

/**
 *
 * @author toks
 */
public class ConnectionMaker {

    public static final Logger log = Logger.getLogger(ConnectionMaker.class);
    final private ClientConnectionPoolDataSource clientConnectionPoolDataSource;
    private PooledConnection pooledConnection;

    public ConnectionMaker(ClientConnectionPoolDataSource clientConnectionPoolDataSource) {
        this.clientConnectionPoolDataSource = clientConnectionPoolDataSource;
        makePooledConnection();
    }

    private void makePooledConnection() {
        try {
            pooledConnection = clientConnectionPoolDataSource.getPooledConnection();
        } catch (SQLException ex) {
            log.error("App could not make connection object. ", ex);
        }
    }

    public PooledConnection getPooledConnection() {
        return pooledConnection;
    }

}
