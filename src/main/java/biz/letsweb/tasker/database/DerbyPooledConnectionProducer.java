package biz.letsweb.tasker.database;

import biz.letsweb.tasker.PooledConnectionProduceable;
import java.sql.SQLException;
import javax.sql.PooledConnection;
import org.apache.derby.jdbc.ClientConnectionPoolDataSource;
import org.apache.log4j.Logger;

/**
 *
 * @author toks
 */
public class DerbyPooledConnectionProducer implements PooledConnectionProduceable{

    public static final Logger log = Logger.getLogger(DerbyPooledConnectionProducer.class);
    private final ClientConnectionPoolDataSource clientConnectionPoolDataSource;
    private PooledConnection pooledConnection;

    public DerbyPooledConnectionProducer(ClientConnectionPoolDataSource clientConnectionPoolDataSource) {
        this.clientConnectionPoolDataSource = clientConnectionPoolDataSource;
        makePooledConnection();
    }

    private void makePooledConnection() {
        try {
            pooledConnection = clientConnectionPoolDataSource.getPooledConnection();
        } catch (SQLException ex) {
            log.error("Application could not make connection object.", ex);
        }
    }

    @Override
    public PooledConnection getPooledConnection() {
        return pooledConnection;
    }

}
