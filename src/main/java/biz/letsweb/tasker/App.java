package biz.letsweb.tasker;

import biz.letsweb.tasker.configuration.ConfigurationProvider;
import biz.letsweb.tasker.db.DataSourceFactory;
import biz.letsweb.tasker.chronicle.dao.ChronicleLineDao;
import biz.letsweb.tasker.chronicle.model.ChronicleLine;
import biz.letsweb.tasker.db.InitializeDb;
import biz.letsweb.tasker.response.json.Jsoner;
import biz.letsweb.tasker.templating.Templating;
import biz.letsweb.tasker.timing.Calculator;
import freemarker.template.TemplateException;
import java.io.IOException;
import java.util.List;
import java.util.logging.Level;
import javax.sql.DataSource;
import javax.xml.parsers.ParserConfigurationException;
import org.apache.commons.cli.BasicParser;
import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.CommandLineParser;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;
import org.apache.commons.configuration.XMLConfiguration;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.xml.sax.SAXException;

public class App {

    public static final Logger log = LoggerFactory.getLogger(App.class);

    public App() {
    }

    public static void main(String[] args) throws IOException, TemplateException, SAXException, ParserConfigurationException {
//        final XMLConfiguration configuration = new ConfigurationProvider("config/configuration.xml").getXMLConfiguration();
        final XMLConfiguration configuration = new ConfigurationProvider("src/test/resources/configuration.xml").getXMLConfiguration();
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
        Jsoner jsoner = new Jsoner();
        
        final ChronicleLineDao chronicleLineDao = new ChronicleLineDao(dataSource);
        ChronicleLine currentChronicle = new ChronicleLine();
        boolean recordsInDb = false;
        try {
            try {
                currentChronicle = chronicleLineDao.findLastRecord();
            } catch (UninitialisedTablesException ex) {
                InitializeDb initializeDb = new InitializeDb(dataSource, configuration);
                initializeDb.createTables();
            }
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
                ChronicleLine newLine = new ChronicleLine();
                newLine.setTag(tagString);
                newLine.setDescription(description);
                chronicleLineDao.insertNewRecord(newLine);
            }
        }
        if (recordsInDb) {
            try {
                final Calculator calculator = new Calculator();
                final List<ChronicleLine> lines = calculator.calculateDurations(chronicleLineDao.findTodaysRecords());
                final List<ChronicleLine> namingRecords = chronicleLineDao.findLastNNamingRecordsDownwards(3);
                final Templating templating = new Templating();
                templating.addParameter("lines", lines);
                templating.addParameter("namingRecords", namingRecords);
                templating.parseTemplate();
            } catch (UninitialisedTablesException ex) {
            } catch (NoRecordsInPoolException ex) {
                System.out.println("Please insert a record.");
            }
        }
    }
}
