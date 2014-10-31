package biz.letsweb.tasker.database;

import org.apache.derby.jdbc.ClientConnectionPoolDataSource;

/**
 *
 * @author toks
 */
public class DerbyPooledDataSourceMaker {

    final ClientConnectionPoolDataSource clientConnectionPoolDataSource;

    public DerbyPooledDataSourceMaker(){
        clientConnectionPoolDataSource = new ClientConnectionPoolDataSource();
        // Set the number of statements the cache is allowed to cache. Any number greater than zero will enable the cache.
        clientConnectionPoolDataSource.setMaxStatements(20);
        // Set other DataSource properties
        clientConnectionPoolDataSource.setDatabaseName("/Users/toks/databases/mydb");
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
