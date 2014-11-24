package biz.letsweb.tasker;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import javax.sql.DataSource;
import org.apache.derby.jdbc.ClientDataSource;
import org.apache.derby.jdbc.EmbeddedDataSource;

/**
 *
 * @author toks
 */
public class ConnectDB {

    public static final String DRIVER = "org.apache.derby.jdbc.EmbeddedDriver";
    public static final String DB_NAME = "testdb";
    public static final String JDBC_URL = String.format("jdbc:derby:%s;create=true", DB_NAME);

    public Connection getConnection() throws ClassNotFoundException, SQLException {
        Class.forName(DRIVER);
        return DriverManager.getConnection(JDBC_URL);
    }

    public DataSource getEmbeddedDS() {
        EmbeddedDataSource ds = new EmbeddedDataSource();
        ds.setDatabaseName(DB_NAME);
//        if (create) {
        ds.setCreateDatabase("create");
//        }
        return ds;
    }

    public DataSource getClientDS(String db) {
        ClientDataSource ds = new ClientDataSource();
        ds.setDatabaseName(db);
        ds.setUser("tumcyk");
        ds.setPassword("mypass");
        ds.setServerName("localhost");
        ds.setCreateDatabase("create");
        ds.setPortNumber(1527);
        return ds;
    }

}
