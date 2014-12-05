package biz.letsweb.tasker.db;

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

    private DataSource dataSource;
    private final DbFileOperations dbFileOperations;
    private final SubnodeConfiguration xmlConfig;

    public DataSourceFactory(XMLConfiguration xmlConfiguration) {
        dbFileOperations = DbFileOperations.getInstance(xmlConfiguration);
        xmlConfig = xmlConfiguration.configurationAt("database");
        final String type = xmlConfig.getString("type").toUpperCase();
        initialize(Type.valueOf(type));
    }

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
        ds.setDatabaseName(dbFileOperations.getDbDirectoryPath());
        if (xmlConfig.getBoolean("create")) {
            ds.setCreateDatabase("create");
        }
        log.debug("Initialized enbedded db in: {}", dbFileOperations.getDbDirectoryPath());
        return ds;
    }

    private DataSource initializeClient() {
        ClientDataSource ds = new ClientDataSource();
        ds.setDatabaseName(dbFileOperations.getDbDirectoryPath());
        // ds.setUser(xmlConfig.getString("user")); //when uncommented it wants TUMCYK schema
        // ds.setPassword(xmlConfig.getString("password"));
        ds.setServerName(xmlConfig.getString("serverName"));
        if (xmlConfig.getBoolean("create")) {
            ds.setCreateDatabase("create");
        }
        ds.setPortNumber(xmlConfig.getInt("portNumber"));
        log.debug("Initialized client db in: {}", dbFileOperations.getDbDirectoryPath());
        return ds;
    }

    public DataSource getDataSource() {
        return dataSource;
    }

    public enum Type {

        CLIENT, EMBEDDED
    }
}
