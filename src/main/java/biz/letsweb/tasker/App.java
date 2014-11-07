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
        options.addOption("activity", true, "kind of task");
        final CommandLineParser parser = new BasicParser();
        final String breakString = "break";
        final String breakCoffeString = "breakCoffe";
        final String workString = "work";
        final String showCurrentString = "showCurrent";
        final String activityOption = "activity";
        String activityString = "";
        CommandLine cmd = null;
        try {
            cmd = parser.parse(options, args);
        } catch (ParseException ex) {
            log.error("Error while parsing options for the command line arguments. ", ex);
        }

        boolean useConfig = cmd.hasOption("useConfig");
        log.info("Using configuration: {}", useConfig);
        DerbyPooledDataSourceFactory dataSourceFactory = new DerbyPooledDataSourceFactory(useConfig);
        final PooledConnection pooledConnection = dataSourceFactory.getPooledConnection();
        if (cmd.hasOption(activityOption)) {
            activityString = cmd.getOptionValue(activityOption);
            PreparedStatement ps = null;
            try {
                final Connection con = pooledConnection.getConnection();
                if (activityString.equalsIgnoreCase(breakString)) {
                    final String sql = "insert into teka.chronicle (description) values (?)";
                    ps = con.prepareStatement(sql);
                    ps.setString(1, "przerwa");
                    ps.execute();
                    log.info("{} should run: {}", activityOption, activityString);
                } else if (activityString.equalsIgnoreCase(breakCoffeString)) {
                    final String sql = "insert into teka.chronicle (description) values (?)";
                    ps = con.prepareStatement(sql);
                    ps.setString(1, "przerwa na kawę");
                    ps.execute();
                    log.info("{} should run: {}", activityOption, activityString);
                } else if (activityString.equalsIgnoreCase(workString)) {
                    final String sql = "insert into teka.chronicle (description) values (?)";
                    ps = con.prepareStatement(sql);
                    ps.setString(1, "praca");
                    ps.execute();
                    log.info("{} should run: {}", activityOption, activityString);
                } else if (activityString.equalsIgnoreCase(showCurrentString)) {
                    final String sql = "select * from teka.chronicle";
                    ps = con.prepareStatement(sql);
                    final ResultSet resultSet = ps.executeQuery();
                    while (resultSet.next()) {
                        log.info("{}", resultSet.getString("description"));
                    }
                }

                if (ps != null) {
                    ps.close();
                }
                con.close();
                pooledConnection.close();
            } catch (SQLException ex) {
                log.error("Application couldn't get a connection from the pool. ", ex);
            }

        } else {
            log.info("no options", cmd.getOptionValue('t'));
        }

    }
}
