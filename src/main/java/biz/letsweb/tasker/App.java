package biz.letsweb.tasker;

import biz.letsweb.tasker.configuration.ConfigurationProvider;
import biz.letsweb.tasker.databaseconnectivity.DataSourceFactory;
import biz.letsweb.tasker.databaseconnectivity.InitializeDb;
import biz.letsweb.tasker.persistence.dao.ChronicleLineDao;
import biz.letsweb.tasker.persistence.model.ChronicleRecordLine;
import biz.letsweb.tasker.templating.Templating;
import java.sql.SQLException;
import java.util.List;
import javax.sql.DataSource;
import org.apache.commons.cli.BasicParser;
import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.CommandLineParser;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;
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
        final DataSourceFactory dataSourceFactory = new DataSourceFactory(configuration);
        final DataSource dataSource = dataSourceFactory.getDataSource();
        final InitializeDb initializeDb = new InitializeDb(dataSource);
        initializeDb.createTables();
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

        final ChronicleLineDao chronicleLineDao = new ChronicleLineDao(dataSource);
        ChronicleRecordLine currentChronicle = null;
        try {
            currentChronicle = chronicleLineDao.findLastRecord();
        } catch (NoRecordsInPoolException ex) {
            System.out.println(ex.getMessage());
        }
        // List<ChronicleRecordLine> lastNRecordsToday = chronicleLineDao.findLastNRecordsToday(3);
        List<ChronicleRecordLine> lastNRecords = chronicleLineDao.findLastNRecordsUpwards(3);
        ConsolePresenter presenter = new ConsolePresenter();
        if (cmd.hasOption(activityOption)) {

            String description = cmd.getOptionValue(desc);
            activityString = cmd.getOptionValue(activityOption);

            ChronicleRecordLine chronicleLine = new ChronicleRecordLine();

            if (activityString.equalsIgnoreCase(breakString)) {
                chronicleLine.setTag(breakString);
                chronicleLine.setDescription(description.isEmpty() ? null : description);
                chronicleLineDao.insertNewRecord(chronicleLine);
            } else if (activityString.equalsIgnoreCase(breakCoffeString)) {
                chronicleLine.setTag(breakCoffeString);
                chronicleLine.setDescription(description.isEmpty() ? null : description);
                chronicleLineDao.insertNewRecord(chronicleLine);
            } else if (activityString.equalsIgnoreCase(stopString)) {
                chronicleLine.setTag(stopString);
                chronicleLine.setDescription(description.isEmpty() ? null : description);
                chronicleLineDao.insertNewRecord(chronicleLine);
            } else if (activityString.equalsIgnoreCase(workString)) {
                chronicleLine.setTag(workString);
                chronicleLine.setDescription(description.isEmpty() ? null : description);
                chronicleLineDao.insertNewRecord(chronicleLine);
                log.info("inserted: {}", chronicleLine);
            } else if (activityString.equalsIgnoreCase(showAllString)) {

            }
        } else if (activityString.equalsIgnoreCase(showTodaysEntries)) {
        } else if (activityString.equalsIgnoreCase(durationOfCurrent)) {
        }
        Templating templating = new Templating();
    }

}
