package biz.letsweb.tasker.database;

import org.apache.derby.jdbc.ClientConnectionPoolDataSource;

/**
 *
 * @author toks
 */
public class DataSourceMaker {

    final ClientConnectionPoolDataSource clientConnectionPoolDataSource;

    public DataSourceMaker(){
        clientConnectionPoolDataSource = new ClientConnectionPoolDataSource();
        // Set the number of statements the cache is allowed to cache. Any number greater than zero will enable the cache.
        clientConnectionPoolDataSource.setMaxStatements(20);
        // Set other DataSource properties
        clientConnectionPoolDataSource.setDatabaseName("mydb");
        clientConnectionPoolDataSource.setCreateDatabase("create");
        clientConnectionPoolDataSource.setUser("tumcyk");
        clientConnectionPoolDataSource.setPassword("mypass");
        clientConnectionPoolDataSource.setServerName("localhost");
        clientConnectionPoolDataSource.setPortNumber(1527);
        // This physical connection will have JDBC statement caching enabled.
    }

    public ClientConnectionPoolDataSource getClientConnectionPoolDataSource() {
        return clientConnectionPoolDataSource;
    }
}
