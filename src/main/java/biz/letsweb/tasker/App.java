package biz.letsweb.tasker;

import biz.letsweb.tasker.database.DerbyPooledDataSourceFactory;
import biz.letsweb.tasker.persistence.model.ChronicleRecordLine;
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
import org.joda.time.DateTime;
import org.joda.time.Duration;
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
        options.addOption("desc", true, "description of task");
        final CommandLineParser parser = new BasicParser();
        final String breakString = "break";
        final String breakCoffeString = "breakCoffe";
        final String workString = "work";
        final String showCurrentString = "showCurrent";
        final String durationOfCurrent = "durationOfCurrent";
        final String activityOption = "activity";
        final String desc = "desc";
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
            String description = cmd.getOptionValue(desc);
            activityString = cmd.getOptionValue(activityOption);
            PreparedStatement ps = null;
            try {
                final Connection con = pooledConnection.getConnection();
                if (activityString.equalsIgnoreCase(breakString)) {
                    log.info("description: {}", cmd.getOptionValue(desc));
                    final String sql = "insert into chronicle (tag, description) values (?, ?)";
                    ps = con.prepareStatement(sql);
                    ps.setString(1, "przerwa");
                    ps.setString(2, description);
                    ps.execute();
                    log.info("{} should run: {}", activityOption, activityString);
                } else if (activityString.equalsIgnoreCase(breakCoffeString)) {
                    final String sql = "insert into chronicle (tag, description) values (?, ?)";
                    ps = con.prepareStatement(sql);
                    ps.setString(1, "przerwa na kawę");
                    ps.setString(2, description);
                    ps.execute();
                    log.info("{} should run: {}", activityOption, activityString);
                } else if (activityString.equalsIgnoreCase(workString)) {
                    final String sql = "insert into chronicle (tag, description) values (?, ?)";
                    ps = con.prepareStatement(sql);
                    ps.setString(1, "praca");
                    ps.setString(2, description);
                    ps.execute();
                    log.info("{} should run: {}", activityOption, activityString);
                } else if (activityString.equalsIgnoreCase(showCurrentString)) {
                    final String sql = "select * from chronicle";
                    ps = con.prepareStatement(sql);
                    final ResultSet resultSet = ps.executeQuery();
                    while (resultSet.next()) {
                        log.info("{}", resultSet.getString("tag"));
                    }
                } else if (activityString.equalsIgnoreCase(durationOfCurrent)) {
                    final String currentTaskSql = "select * from chronicle where id=(select max(id) from chronicle)";
                    ps = con.prepareStatement(currentTaskSql);
                    final ResultSet resultSet = ps.executeQuery();
                    final ChronicleRecordLine chronicleRecordLine = new ChronicleRecordLine();
                    while (resultSet.next()) {
                        chronicleRecordLine.setId(resultSet.getInt("id"));
                        chronicleRecordLine.setTag(resultSet.getString("tag"));
                        chronicleRecordLine.setDescription(resultSet.getString("description"));
                        chronicleRecordLine.setTimestamp(resultSet.getTimestamp("inserted"));
                    }
                    DateTime dateTimeFromTimestamp = new DateTime(chronicleRecordLine.getTimestamp());
                    DateTime dateTimeNow = new DateTime();
                    Duration duration = new Duration(dateTimeFromTimestamp, dateTimeNow);
                    log.info("current: {}", chronicleRecordLine);
                    log.info("chronicle timestamp: {}", dateTimeFromTimestamp);
                    log.info("chronicle minutes: {}", duration.getStandardMinutes());
//                    final String sql = "select * from teka.chronicle";
//                    ps = con.prepareStatement(sql);
//                    final ResultSet resultSet = ps.executeQuery();
//                    while (resultSet.next()) {
//                        log.info("{}", resultSet.getString("description"));
//                    }
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
