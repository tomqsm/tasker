package biz.letsweb.tasker;

import biz.letsweb.tasker.database.ConnectionMaker;
import biz.letsweb.tasker.database.DataSourceMaker;
import java.sql.Connection;
import java.sql.SQLException;
import javax.sql.PooledConnection;
import org.apache.log4j.Logger;

public class App {

    public static final Logger log = Logger.getLogger(App.class);

    public static void main(String[] args) {
        log.info("Hello info logger");
        PooledConnection connections = new ConnectionMaker(new DataSourceMaker().getClientConnectionPoolDataSource()).getPooledConnection();
        Connection con;
        try {
            con = connections.getConnection();
            // do stuff
            con.close();
            connections.close();
        } catch (SQLException ex) {
            log.error("Application couldn't get a connection from the pool. ", ex);
        }
        
    }
}
