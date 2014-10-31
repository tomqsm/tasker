package biz.letsweb.tasker;

import biz.letsweb.tasker.database.DerbyPooledConnectionProducer;
import biz.letsweb.tasker.database.DerbyPooledDataSourceMaker;
import java.sql.Connection;
import java.sql.SQLException;
import javax.sql.PooledConnection;
import org.apache.derby.jdbc.ClientConnectionPoolDataSource;
import org.apache.log4j.Logger;

public class App {

    public static final Logger log = Logger.getLogger(App.class);

    public static void main(String[] args) {
        log.info("Hello info logger");
        final DerbyPooledDataSourceMaker derbyPooledDataSourceMaker = new DerbyPooledDataSourceMaker();
        final ClientConnectionPoolDataSource clientConnectionPoolDataSource = derbyPooledDataSourceMaker.getClientConnectionPoolDataSource();
        final PooledConnectionProduceable pooledConnectionProduceable = new DerbyPooledConnectionProducer(clientConnectionPoolDataSource);
        final PooledConnection connections = pooledConnectionProduceable.getPooledConnection();
        try {
            final Connection con = connections.getConnection();
            // do stuff
            con.close();
            connections.close();
        } catch (SQLException ex) {
            log.error("Application couldn't get a connection from the pool. ", ex);
        }
        
    }
}
