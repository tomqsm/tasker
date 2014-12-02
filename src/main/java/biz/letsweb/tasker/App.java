package biz.letsweb.tasker;

import biz.letsweb.tasker.configuration.ConfigurationProvider;
import biz.letsweb.tasker.databaseconnectivity.DataSourceFactory;
import biz.letsweb.tasker.databaseconnectivity.InitializeDb;
import biz.letsweb.tasker.persistence.dao.ChronicleLineDao;
import biz.letsweb.tasker.persistence.model.ChronicleRecordLine;
import biz.letsweb.tasker.templating.Templating;
import biz.letsweb.tasker.timing.Calculator;
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

    public static void main(String[] args) throws Exception {
        final XMLConfiguration configuration = new ConfigurationProvider("config/configuration.xml").getXMLConfiguration();
//        final XMLConfiguration configuration = new ConfigurationProvider("src/test/resources/configuration.xml").getXMLConfiguration();
        final DataSourceFactory dataSourceFactory = new DataSourceFactory(configuration);
        final DataSource dataSource = dataSourceFactory.getDataSource();
        final Options options = new Options();
        options.addOption("t", true, "type of entry");
        options.addOption("tag", true, "kind of task");
        options.addOption("desc", true, "description of task");
        final CommandLineParser parser = new BasicParser();
        final String tagOption = "tag";
        final String desc = "desc";
        String tagString = "";
        CommandLine cmd = null;
        try {
            cmd = parser.parse(options, args);
        } catch (ParseException ex) {
            log.error("Error while parsing options for the command line arguments. ", ex);
        }

        final ChronicleLineDao chronicleLineDao = new ChronicleLineDao(dataSource);
        ChronicleRecordLine currentChronicle = new ChronicleRecordLine();
        boolean recordsInDb = false;
        try {
            currentChronicle = chronicleLineDao.findLastRecord();
            recordsInDb = true;
        } catch (NoRecordsInPoolException ex) {
            System.out.println("You haven't got any named tasks yet, please enter a task name and description.");
            // when here it means 1) no records in db
        }
        if (cmd.hasOption(tagOption)) {
            tagString = cmd.getOptionValue(tagOption);
            if (recordsInDb && tagString.equalsIgnoreCase(currentChronicle.getTag())) {
                System.out.println("You haven't got any named tasks yet, please enter a task name and description.");
            } else {
                String description = cmd.getOptionValue(desc);
                ChronicleRecordLine newLine = new ChronicleRecordLine();
                newLine.setTag(tagString);
                newLine.setDescription(description);
                chronicleLineDao.insertNewRecord(newLine);
            }
        }
        if (recordsInDb) {
            final Calculator calculator = new Calculator();
            final List<ChronicleRecordLine> lines = calculator.calculateDurations(chronicleLineDao.findTodaysRecords());
            final List<ChronicleRecordLine> namingRecords = chronicleLineDao.findLastNNamingRecordsDownwards(3);
            final Templating templating = new Templating();
            templating.addParameter("lines", lines);
            templating.addParameter("namingRecords", namingRecords);
            templating.parseTemplate();
        }
    }
}
