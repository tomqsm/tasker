package biz.letsweb.tasker;

import biz.letsweb.tasker.databaseconnectivity.DerbyPooledDataSourceFactory;
import biz.letsweb.tasker.persistence.model.ChronicleRecordLine;
import biz.letsweb.tasker.services.InsertNewEntryService;
import biz.letsweb.tasker.services.FindCurrentEntryService;
import biz.letsweb.tasker.services.FindLastNEntriesService;
import biz.letsweb.tasker.services.FindPreviousEntryService;
import biz.letsweb.tasker.services.Serviceable;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
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
        final String durationOfPrevious = "durationOfPrevious";
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
                final Serviceable<ChronicleRecordLine> findCurrentEntryService = new FindCurrentEntryService();
                findCurrentEntryService.execute(con);
                ChronicleRecordLine currentEntry = findCurrentEntryService.getData();
                Serviceable<ChronicleRecordLine> insertNewEntryService = new InsertNewEntryService();
                ChronicleRecordLine entry = new ChronicleRecordLine();

                if (activityString.equalsIgnoreCase(breakString)) {
                    log.info("description: {}", cmd.getOptionValue(desc));
                    entry.setTag(breakString);
                    entry.setDescription(description);
                    insertNewEntryService.setData(entry);
                    insertNewEntryService.execute(con);
                    log.info("{} should run: {}", activityOption, activityString);
                } else if (activityString.equalsIgnoreCase(breakCoffeString)) {
                    entry.setTag(breakCoffeString);
                    entry.setDescription(description);
                    insertNewEntryService.setData(entry);
                    insertNewEntryService.execute(con);
                    log.info("{} should run: {}", activityOption, activityString);
                } else if (activityString.equalsIgnoreCase(workString)) {
                    entry.setTag(workString);
                    entry.setDescription(description);
                    insertNewEntryService.setData(entry);
                    insertNewEntryService.execute(con);
                    log.info("{} should run: {}", activityOption, activityString);
                } else if (activityString.equalsIgnoreCase(showCurrentString)) {
                    final String sql = "select * from chronicle";
                    ps = con.prepareStatement(sql);
                    final ResultSet resultSet = ps.executeQuery();
                    while (resultSet.next()) {
                        log.info("{}", resultSet.getString("tag"));
                    }
                } else if (activityString.equalsIgnoreCase(durationOfPrevious)) {
                    Serviceable<ChronicleRecordLine> dataService = new FindPreviousEntryService();
                    dataService.execute(con);
                    final ChronicleRecordLine previousLine = dataService.getData();
                    DateTime dateTimeOfCurrent = new DateTime(currentEntry.getTimestamp());
                    DateTime dateTimeOfPrevious = new DateTime(previousLine.getTimestamp());
                    Duration duration = new Duration(dateTimeOfPrevious, dateTimeOfCurrent);
                    log.info("previous: {} {} {}", previousLine.getTag(), previousLine.getDescription(), duration.getStandardMinutes());
                    log.info("content: {}", previousLine);
                } else if (activityString.equalsIgnoreCase(durationOfCurrent)) {
                    DateTime dateTimeOfCurrent = new DateTime(currentEntry.getTimestamp());
                    DateTime dateTimeNow = new DateTime();
                    Duration duration = new Duration(dateTimeOfCurrent, dateTimeNow);
                    log.info("\ncurrent: {} {} {} minutes", currentEntry.getTag(), currentEntry.getDescription(), duration.getStandardMinutes());
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
