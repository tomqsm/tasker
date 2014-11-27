package biz.letsweb.tasker;

import biz.letsweb.tasker.configuration.ConfigurationProvider;
import biz.letsweb.tasker.databaseconnectivity.DataSourceFactory;
import biz.letsweb.tasker.databaseconnectivity.InitializeDb;
import java.sql.SQLException;
import javax.sql.DataSource;
import org.apache.commons.configuration.XMLConfiguration;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class App {

    public static final Logger log = LoggerFactory.getLogger(App.class);

    public App() {
    }

    public static void main(String[] args) throws SQLException {
        log.info("Tasker starts.");
        final XMLConfiguration configuration = new ConfigurationProvider("src/test/resources/configuration.xml").getXMLConfiguration();
        DataSourceFactory dataSourceFactory = new DataSourceFactory(configuration);
        final DataSource dataSource = dataSourceFactory.getDataSource();
        InitializeDb initializeDb = new InitializeDb(dataSource);
        initializeDb.createTables();
    // final Options options = new Options();
        // options.addOption("t", true, "type of entry");
        // options.addOption("useConfig", false, "type of entry");
        // options.addOption("activity", true, "kind of task");
        // options.addOption("desc", true, "description of task");
        // final CommandLineParser parser = new BasicParser();
        // final String breakString = "break";
        // final String breakCoffeString = "breakCoffe";
        // final String workString = "work";
        // final String stopString = "stop";
        // final String showAllString = "showAll";
        // final String showTodaysEntries = "showTodaysEntries";
        // final String showTodayEntriesString = "showTodayEntries";
        // final String durationOfCurrent = "durationOfCurrent";
        // final String durationOfPrevious = "durationOfPrevious";
        // final String activityOption = "activity";
        // final String desc = "desc";
        // String activityString = "";
        // CommandLine cmd = null;
        // try {
        // cmd = parser.parse(options, args);
        // } catch (ParseException ex) {
        // log.error("Error while parsing options for the command line arguments. ", ex);
        // }
        //
        // boolean useConfig = cmd.hasOption("useConfig");
        // log.info("Using configuration: {}", useConfig);
        // final DerbyPooledDataSourceFactory dataSourceFactory =
        // new DerbyPooledDataSourceFactory(useConfig);
        // final PooledConnection pooledConnection = dataSourceFactory.getPooledConnection();
        // final ChronicleLineDao chronicleLineDao = new ChronicleLineDao(pooledConnection);
        // final CommentLineDao commentLineDao = new CommentLineDao(pooledConnection);
        // ChronicleRecordLine currentChronicle = chronicleLineDao.findLastRecord();
        // // List<ChronicleRecordLine> lastNRecordsToday = chronicleLineDao.findLastNRecordsToday(3);
        // List<ChronicleRecordLine> lastNRecords = chronicleLineDao.findLastNRecords(3);
        // ConsolePresenter presenter = new ConsolePresenter();
        // if (cmd.hasOption(activityOption)) {
        //
        // String description = cmd.getOptionValue(desc);
        // activityString = cmd.getOptionValue(activityOption);
        // if (currentChronicle.getId() == 0) {
        // // this happens when empty database
        // currentChronicle.setId(-1);
        // currentChronicle.setTag(activityString);
        // currentChronicle.setDescription(description);
        // }
        // try {
        // ChronicleRecordLine entry = new ChronicleRecordLine();
        // CommentLine commentLine = new CommentLine();
        //
        // if (activityString.equalsIgnoreCase(breakString)) {
        // if (currentChronicle.getTag().equalsIgnoreCase(breakString) && !description.isEmpty()) {
        // commentLine.setChronicleId(currentChronicle.getId());
        // commentLine.setDescription(description);
        // commentLineDao.insertNewRecord(commentLine);
        // log.info("inserted: {}", commentLine);
        // } else if (!currentChronicle.getTag().equalsIgnoreCase(breakString)) {
        // entry.setTag(breakString);
        // entry.setDescription(description.isEmpty() ? null : description);
        // chronicleLineDao.insertNewRecord(entry);
        // log.info("inserted: {}", entry);
        // } else if (currentChronicle.getTag().equalsIgnoreCase(breakString)
        // && currentChronicle.getId() == -1) {
        // entry.setTag(breakString);
        // entry.setDescription(description.isEmpty() ? null : description);
        // chronicleLineDao.insertNewRecord(entry);
        // }
        // } else if (activityString.equalsIgnoreCase(breakCoffeString)) {
        // if (currentChronicle.getTag().equalsIgnoreCase(breakCoffeString)
        // && !description.isEmpty()) {
        // commentLine.setChronicleId(currentChronicle.getId());
        // commentLine.setDescription(description);
        // commentLineDao.insertNewRecord(commentLine);
        // log.info("inserted: {}", commentLine);
        // } else if (!currentChronicle.getTag().equalsIgnoreCase(breakCoffeString)) {
        // entry.setTag(breakCoffeString);
        // entry.setDescription(description.isEmpty() ? null : description);
        // chronicleLineDao.insertNewRecord(entry);
        // log.info("inserted: {}", entry);
        // } else if (currentChronicle.getTag().equalsIgnoreCase(breakCoffeString)
        // && currentChronicle.getId() == -1) {
        // entry.setTag(breakCoffeString);
        // entry.setDescription(description.isEmpty() ? null : description);
        // chronicleLineDao.insertNewRecord(entry);
        // }
        // } else if (activityString.equalsIgnoreCase(stopString)) {
        // if (currentChronicle.getTag().equalsIgnoreCase(stopString) && !description.isEmpty()) {
        // commentLine.setChronicleId(currentChronicle.getId());
        // commentLine.setDescription(description);
        // commentLineDao.insertNewRecord(commentLine);
        // log.info("inserted: {}", commentLine);
        // } else if (!currentChronicle.getTag().equalsIgnoreCase(stopString)) {
        // entry.setTag(stopString);
        // entry.setDescription(description.isEmpty() ? null : description);
        // chronicleLineDao.insertNewRecord(entry);
        // log.info("inserted: {}", entry);
        // } else if (currentChronicle.getTag().equalsIgnoreCase(stopString)
        // && currentChronicle.getId() == -1) {
        // entry.setTag(stopString);
        // entry.setDescription(description.isEmpty() ? null : description);
        // chronicleLineDao.insertNewRecord(entry);
        // }
        // } else if (activityString.equalsIgnoreCase(workString)) {
        // if (currentChronicle.getTag().equalsIgnoreCase(workString) && !description.isEmpty()) {
        // commentLine.setChronicleId(currentChronicle.getId());
        // commentLine.setDescription(description);
        // commentLineDao.insertNewRecord(commentLine);
        // log.info("inserted: {}", commentLine);
        // } else if (!currentChronicle.getTag().equalsIgnoreCase(workString)) {
        // entry.setTag(workString);
        // entry.setDescription(description.isEmpty() ? null : description);
        // chronicleLineDao.insertNewRecord(entry);
        // log.info("inserted: {}", entry);
        // } else if (currentChronicle.getTag().equalsIgnoreCase(workString)
        // && currentChronicle.getId() == -1) {
        // entry.setTag(workString);
        // entry.setDescription(description.isEmpty() ? null : description);
        // chronicleLineDao.insertNewRecord(entry);
        // }
        // } else if (activityString.equalsIgnoreCase(showAllString)) {
        // final List<ChronicleRecordLine> allRecords = chronicleLineDao.findAllRecords();
        // final Iterator<ChronicleRecordLine> iterator = allRecords.iterator();
        // while (iterator.hasNext()) {
        // log.info("{}", iterator.next());
        // }
        // } else if (activityString.equalsIgnoreCase(showTodaysEntries)) {
        // final List<ChronicleRecordLine> todaysRecords = chronicleLineDao.findTodaysRecords();
        // final Iterator<ChronicleRecordLine> iterator = todaysRecords.iterator();
        // while (iterator.hasNext()) {
        // log.info("{}", iterator.next());
        // }
        // } else if (activityString.equalsIgnoreCase(durationOfCurrent)) {
        // TimeCalaculator timeCalaculator = new TimeCalaculator();
        // final List<ConsoleViewModel> durationsPerTag =
        // timeCalaculator.getDurationsPerTag(chronicleLineDao.findTodaysRecords(), 3);
        // for (int i = 0; i < 3; i++) {
        // ConsoleViewModel view = durationsPerTag.get(i);
        // String printLine =
        // String.format("#%d %s %d/%d %s", view.getChronicleRecordLine().getCount(), view
        // .getChronicleRecordLine().getTag(), view.getDuration().getStandardMinutes(),
        // view.getTotalDuration().getStandardMinutes(), view.getChronicleRecordLine()
        // .getDescription());
        // System.out.println(printLine);
        // }
        // }
        // pooledConnection.close();
        // } catch (SQLException ex) {
        // log.error("Application couldn't get a connection from the pool. ", ex);
        // }
        //
        // } else {
        // log.info("no options", cmd.getOptionValue('t'));
        // }

    }
}
