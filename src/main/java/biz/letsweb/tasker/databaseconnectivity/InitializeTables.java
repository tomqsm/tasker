package biz.letsweb.tasker.databaseconnectivity;

import biz.letsweb.tasker.configuration.ConfigurationProvider;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import org.apache.commons.configuration.SubnodeConfiguration;
import org.apache.commons.configuration.XMLConfiguration;

/**
 *
 * @author toks
 */
public class InitializeTables {

    private final String initializeTables;
    private final DataSourcePrepare dataSourcePrepare;

    public InitializeTables() {
        ConfigurationProvider configurationProvider = new ConfigurationProvider("config/configuration.xml");
        final XMLConfiguration xmlConfiguration = configurationProvider.getXMLConfiguration();
        final SubnodeConfiguration xmlConfig = xmlConfiguration.configurationAt("database");
        initializeTables = xmlConfig.getString("initializeTables");
        System.out.println(initializeTables);
        dataSourcePrepare = new DataSourcePrepare(DataSourcePrepare.Type.CLIENT);
    }

    public void createTables() throws SQLException {
        final PreparedStatement ps;
        try (Connection connection = dataSourcePrepare.getDataSource().getConnection()) {
            ps = connection.prepareStatement(initializeTables);
            ps.execute();
        }
        ps.close();
    }
}
