package biz.letsweb.tasker;

import biz.letsweb.tasker.databaseconnectivity.DerbyPooledDataSourceFactory;
import biz.letsweb.tasker.persistence.model.ChronicleRecordLine;
import biz.letsweb.tasker.persistence.model.CommentLine;
import biz.letsweb.tasker.services.ChronicleLineDao;
import biz.letsweb.tasker.services.CommentLineDao;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.Collections;
import java.util.Iterator;
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
    private static final int FIRST = 0;
    private static final int SECOND = 1;
    private static final int THIRD = 2;

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
        final String stopString = "stop";
        final String showAllString = "showAll";
        final String showTodaysEntries = "showTodaysEntries";
        final String showTodayEntriesString = "showTodayEntries";
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
        final DerbyPooledDataSourceFactory dataSourceFactory = new DerbyPooledDataSourceFactory(useConfig);
        final PooledConnection pooledConnection = dataSourceFactory.getPooledConnection();
        final ChronicleLineDao chronicleLineDao = new ChronicleLineDao(pooledConnection);
        final CommentLineDao commentLineDao = new CommentLineDao(pooledConnection);
        ChronicleRecordLine currentChronicle = chronicleLineDao.findLastRecord();
//        List<ChronicleRecordLine> lastNRecordsToday = chronicleLineDao.findLastNRecordsToday(3);
        List<ChronicleRecordLine> lastNRecords = chronicleLineDao.findLastNRecords(3);

        if (cmd.hasOption(activityOption)) {

            String description = cmd.getOptionValue(desc);
            activityString = cmd.getOptionValue(activityOption);
            if (currentChronicle.getId() == 0) {
                // this happens when empty database
                currentChronicle.setId(-1);
                currentChronicle.setTag(activityString);
                currentChronicle.setDescription(description);
            }
            try {
                ChronicleRecordLine entry = new ChronicleRecordLine();
                CommentLine commentLine = new CommentLine();

                if (activityString.equalsIgnoreCase(breakString)) {
                    if (currentChronicle.getTag().equalsIgnoreCase(breakString) && !description.isEmpty()) {
                        commentLine.setChronicleId(currentChronicle.getId());
                        commentLine.setDescription(description);
                        commentLineDao.insertNewRecord(commentLine);
                        log.info("inserted: {}", commentLine);
                    } else if (!currentChronicle.getTag().equalsIgnoreCase(breakString)) {
                        entry.setTag(breakString);
                        entry.setDescription(description.isEmpty() ? null : description);
                        chronicleLineDao.insertNewRecord(entry);
                        log.info("inserted: {}", entry);
                    } else if (currentChronicle.getTag().equalsIgnoreCase(breakString) && currentChronicle.getId() == -1) {
                        entry.setTag(breakString);
                        entry.setDescription(description.isEmpty() ? null : description);
                        chronicleLineDao.insertNewRecord(entry);
                    }
                } else if (activityString.equalsIgnoreCase(breakCoffeString)) {
                    if (currentChronicle.getTag().equalsIgnoreCase(breakCoffeString) && !description.isEmpty()) {
                        commentLine.setChronicleId(currentChronicle.getId());
                        commentLine.setDescription(description);
                        commentLineDao.insertNewRecord(commentLine);
                        log.info("inserted: {}", commentLine);
                    } else if (!currentChronicle.getTag().equalsIgnoreCase(breakCoffeString)) {
                        entry.setTag(breakCoffeString);
                        entry.setDescription(description.isEmpty() ? null : description);
                        chronicleLineDao.insertNewRecord(entry);
                        log.info("inserted: {}", entry);
                    } else if (currentChronicle.getTag().equalsIgnoreCase(breakCoffeString) && currentChronicle.getId() == -1) {
                        entry.setTag(breakCoffeString);
                        entry.setDescription(description.isEmpty() ? null : description);
                        chronicleLineDao.insertNewRecord(entry);
                    }
                } else if (activityString.equalsIgnoreCase(stopString)) {
                    if (currentChronicle.getTag().equalsIgnoreCase(stopString) && !description.isEmpty()) {
                        commentLine.setChronicleId(currentChronicle.getId());
                        commentLine.setDescription(description);
                        commentLineDao.insertNewRecord(commentLine);
                        log.info("inserted: {}", commentLine);
                    } else if (!currentChronicle.getTag().equalsIgnoreCase(stopString)) {
                        entry.setTag(stopString);
                        entry.setDescription(description.isEmpty() ? null : description);
                        chronicleLineDao.insertNewRecord(entry);
                        log.info("inserted: {}", entry);
                    } else if (currentChronicle.getTag().equalsIgnoreCase(stopString) && currentChronicle.getId() == -1) {
                        entry.setTag(stopString);
                        entry.setDescription(description.isEmpty() ? null : description);
                        chronicleLineDao.insertNewRecord(entry);
                    }
                } else if (activityString.equalsIgnoreCase(workString)) {
                    if (currentChronicle.getTag().equalsIgnoreCase(workString) && !description.isEmpty()) {
                        commentLine.setChronicleId(currentChronicle.getId());
                        commentLine.setDescription(description);
                        commentLineDao.insertNewRecord(commentLine);
                        log.info("inserted: {}", commentLine);
                    } else if (!currentChronicle.getTag().equalsIgnoreCase(workString)) {
                        entry.setTag(workString);
                        entry.setDescription(description.isEmpty() ? null : description);
                        chronicleLineDao.insertNewRecord(entry);
                        log.info("inserted: {}", entry);
                    } else if (currentChronicle.getTag().equalsIgnoreCase(workString) && currentChronicle.getId() == -1) {
                        entry.setTag(workString);
                        entry.setDescription(description.isEmpty() ? null : description);
                        chronicleLineDao.insertNewRecord(entry);
                    }
                } else if (activityString.equalsIgnoreCase(showAllString)) {
                    final List<ChronicleRecordLine> allRecords = chronicleLineDao.findAllRecords();
                    final Iterator<ChronicleRecordLine> iterator = allRecords.iterator();
                    while (iterator.hasNext()) {
                        log.info("{}", iterator.next());
                    }
                } else if (activityString.equalsIgnoreCase(showTodaysEntries)) {
                    final List<ChronicleRecordLine> todaysRecords = chronicleLineDao.findTodaysRecords();
                    final Iterator<ChronicleRecordLine> iterator = todaysRecords.iterator();
                    while (iterator.hasNext()) {
                        log.info("{}", iterator.next());
                    }
                } else if (activityString.equalsIgnoreCase(durationOfCurrent)) {
                    if (lastNRecords.isEmpty()) {
                        log.info("No records in database ... exiting.");
                        System.exit(0);
                    }
                    Collections.reverse(lastNRecords);
                    for (int i = 0; i < lastNRecords.size(); i++) {
                        Timestamp earlierTimestamp = (i == 0 ? lastNRecords.get(i).getTimestamp() : lastNRecords.get(i - 1).getTimestamp());
                        Timestamp laterTimestamp = (i == 0 ? new Timestamp(System.currentTimeMillis()) : lastNRecords.get(i).getTimestamp());
                        DateTime from = new DateTime(earlierTimestamp);
                        DateTime to = new DateTime(laterTimestamp);
                        Duration duration = new Duration(from, to);
                        String output = String.format("#%d duration: %s %s %d minutes", 
                                lastNRecords.get(i).getCount(), 
                                lastNRecords.get(i).getTag(), 
                                lastNRecords.get(i).getDescription(), 
                                duration.getStandardMinutes());
                        System.out.println(output);
                    }
                }
                pooledConnection.close();
            } catch (SQLException ex) {
                log.error("Application couldn't get a connection from the pool. ", ex);
            }

        } else {
            log.info("no options", cmd.getOptionValue('t'));
        }

    }
}
