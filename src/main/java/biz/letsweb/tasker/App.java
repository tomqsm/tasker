package biz.letsweb.tasker;

import biz.letsweb.tasker.databaseconnectivity.DerbyPooledDataSourceFactory;
import biz.letsweb.tasker.persistence.model.ChronicleRecordLine;
import biz.letsweb.tasker.persistence.model.CommentLine;
import biz.letsweb.tasker.services.ChronicleLineDao;
import biz.letsweb.tasker.services.ChronicleSequenceGrouper;
import biz.letsweb.tasker.services.CommentLineDao;
import java.sql.SQLException;
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

  public App() {}

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
    DerbyPooledDataSourceFactory dataSourceFactory = new DerbyPooledDataSourceFactory(useConfig);
    final PooledConnection pooledConnection = dataSourceFactory.getPooledConnection();
    ChronicleLineDao chronicleLineDao = new ChronicleLineDao(pooledConnection);
    CommentLineDao commentLineDao = new CommentLineDao(pooledConnection);
    final ChronicleSequenceGrouper grouper = new ChronicleSequenceGrouper();
    final List<ChronicleRecordLine> boundaries =
        grouper.getGroupRearEntry(chronicleLineDao.findTodaysRecords());
    ChronicleRecordLine currentChronicle = boundaries.get(0);
    if (cmd.hasOption(activityOption)) {

      String description = cmd.getOptionValue(desc);
      activityString = cmd.getOptionValue(activityOption);
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
          }
        } else if (activityString.equalsIgnoreCase(breakCoffeString)) {
          if (currentChronicle.getTag().equalsIgnoreCase(breakCoffeString)
              && !description.isEmpty()) {
            commentLine.setChronicleId(currentChronicle.getId());
            commentLine.setDescription(description);
            commentLineDao.insertNewRecord(commentLine);
            log.info("inserted: {}", commentLine);
          } else if (!currentChronicle.getTag().equalsIgnoreCase(breakCoffeString)) {
            entry.setTag(breakCoffeString);
            entry.setDescription(description.isEmpty() ? null : description);
            chronicleLineDao.insertNewRecord(entry);
            log.info("inserted: {}", entry);
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
          final ChronicleRecordLine last = boundaries.get(0);
          final ChronicleRecordLine lastButOne = boundaries.get(1);
          final ChronicleRecordLine lastButTwo = boundaries.get(2);
          DateTime dateTimeOfCurrent = new DateTime(last.getTimestamp());
          DateTime dateTimeNow = new DateTime();
          Duration durationLastButOne =
              new Duration(new DateTime(lastButOne.getTimestamp()), new DateTime(
                  last.getTimestamp()));
          Duration durationLastButTwo =
              new Duration(new DateTime(lastButTwo.getTimestamp()), new DateTime(
                  lastButOne.getTimestamp()));
          Duration durationCurrent = new Duration(dateTimeOfCurrent, dateTimeNow);
          log.info(
              "\n#{} previous: {} {} {} minutes\n#{} previous: {} {} {} minutes\n#{} current: {} {} {} minutes",
              lastButTwo.getCount(), lastButTwo.getTag(), lastButTwo.getDescription(),
              durationLastButTwo.getStandardMinutes(), lastButOne.getCount(), lastButOne.getTag(),
              lastButOne.getDescription(), durationLastButOne.getStandardMinutes(),
              last.getCount(), last.getTag(), last.getDescription(),
              durationCurrent.getStandardMinutes());
        } else if (activityString.equalsIgnoreCase(showTodayEntriesString)) {

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
