package biz.letsweb.tasker;

import biz.letsweb.tasker.database.DerbyPooledDataSourceFactory;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import javax.sql.PooledConnection;
import org.apache.commons.cli.BasicParser;
import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.CommandLineParser;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class App {
    
    public static final Logger log = LoggerFactory.getLogger(App.class);
    
    public App() {
    }
    
    public static void main(String[] args) {
        log.info("Tasker starts.");
        final Options options = new Options();
        options.addOption("t", true, "type of entry");
        options.addOption("useConfig", false, "type of entry");
        final CommandLineParser parser = new BasicParser();
        CommandLine cmd = null;
        try {
            cmd = parser.parse(options, args);
            if (cmd.hasOption("t")) {
                log.info("option value: {}", cmd.getOptionValue("t"));
            } else {
                log.info("no options", cmd.getOptionValue('t'));
            }
        } catch (ParseException ex) {
            log.error("Error while parsing options for the command line arguments. ", ex);
        }
        boolean useConfig = cmd.hasOption("useConfig");
        log.info("Using configuration: {}", useConfig);
        DerbyPooledDataSourceFactory dataSourceFactory = new DerbyPooledDataSourceFactory(useConfig);
        final PooledConnection pooledConnection = dataSourceFactory.getPooledConnection();
        try {
            final Connection con = pooledConnection.getConnection();
            final String sql = "select * from teka.chronicle";
            final PreparedStatement ps = con.prepareStatement(sql);
            final ResultSet resultSet = ps.executeQuery();
            while (resultSet.next()) {
                log.info("{}", resultSet.getString("description"));
            }
            ps.close();
            con.close();
            pooledConnection.close();
        } catch (SQLException ex) {
            log.error("Application couldn't get a connection from the pool. ", ex);
        }
    }
}
