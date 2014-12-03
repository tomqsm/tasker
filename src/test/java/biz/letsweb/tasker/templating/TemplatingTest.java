package biz.letsweb.tasker.templating;

import biz.letsweb.tasker.configuration.ConfigurationProvider;
import biz.letsweb.tasker.databaseconnectivity.DataSourceFactory;
import biz.letsweb.tasker.databaseconnectivity.InitializeDb;
import biz.letsweb.tasker.model.ConsoleViewModel;
import biz.letsweb.tasker.persistence.dao.ChronicleLineDao;
import biz.letsweb.tasker.persistence.model.ChronicleLine;
import biz.letsweb.tasker.timing.Calculator;
import freemarker.ext.beans.BeansWrapper;
import freemarker.template.Configuration;
import freemarker.template.DefaultObjectWrapper;
import freemarker.template.ObjectWrapper;
import freemarker.template.Template;
import java.io.File;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import javax.sql.DataSource;
import org.apache.commons.configuration.XMLConfiguration;
import static org.fest.assertions.Assertions.assertThat;
import org.joda.time.DateTime;
import org.joda.time.Duration;
import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 * @author toks
 */
public class TemplatingTest {

    public static final Logger log = LoggerFactory.getLogger(TemplatingTest.class);
    private InitializeDb initializeDb;
    private ChronicleLineDao chronicleDao;
    private Calculator calculator;

    public TemplatingTest() {
    }

    @Before
    public void setUp() throws SQLException {
        final XMLConfiguration configuration = new ConfigurationProvider("src/test/resources/configuration.xml").getXMLConfiguration();
        final DataSourceFactory dataSourceFactory = new DataSourceFactory(configuration);
        chronicleDao = new ChronicleLineDao(dataSourceFactory.getDataSource());
        setupDatabase(dataSourceFactory.getDataSource());
        calculator = new Calculator();
    }

    @After
    public void tearDown() {
    }

    private void setupDatabase(DataSource ds) throws SQLException {
        initializeDb = new InitializeDb(ds);
        final InitializeDb.Feedback createTables = initializeDb.createTables();
        if (createTables == InitializeDb.Feedback.TABLES_EXISTED) {
            initializeDb.clearTables();
        }
    }

    /**
     * Test of parseTemplate method, of class Templating.
     */
    // @Test
    public void dontKnowHowToPassJsonToFreemarker() throws Exception {
        String json
                = "{\n" + "  \"chronicleRecordLine\" : {\n" + "    \"id\" : 1,\n" + "    \"count\" : 1,\n"
                + "    \"tag\" : \"work\",\n" + "    \"description\" : \"desc\",\n"
                + "    \"timestamp\" : 1416996687187\n" + "  },\n" + "  \"duration\" : {\n"
                + "    \"standardDays\" : 0,\n" + "    \"standardHours\"    : 0,\n"
                + "    \"standardMinutes\" : 0,\n" + "    \"standardSeconds\" : 0,\n"
                + "    \"millis\" : 0\n" + "  },\n" + "  \"totalDuration\" : {\n"
                + "    \"standardDays\" : 0,\n" + "    \"standardHours\" : 0,\n"
                + "    \"standardMinutes\" : 0,\n" + "    \"standardSeconds\" : 0,\n"
                + "    \"millis\" : 0\n" + "  }";
        File templateFolder = new File("src/test/resources");
        Configuration cfg = new Configuration();
        cfg.setDirectoryForTemplateLoading(templateFolder);
        cfg.setEncoding(new Locale("pl", "PL"), "UTF-8");
        // cfg.setObjectWrapper(ObjectWrapper.DEFAULT_WRAPPER);
        cfg.setObjectWrapper(ObjectWrapper.BEANS_WRAPPER);
        cfg.setDefaultEncoding("UTF-8");
        cfg.setOutputEncoding("UTF-8");
        final Template template = cfg.getTemplate("template.ftl");
        Map<String, Object> root = new HashMap();
        // InputStream inputStream = new ByteArrayInputStream(json.getBytes());
        // InputSource inputSource = new InputSource(inputStream);
        // final NodeModel parsed = NodeModel.parse(inputSource);
        // root.put("xml", parsed);
        root.put("json", json);
        Writer out = new OutputStreamWriter(System.out);
        template.process(root, out);

    }
    @Rule
    public ExpectedException thrown = ExpectedException.none();

//    @Test
    public void passingObjectToFreemarker() throws Exception {
        List<ConsoleViewModel> lines = new ArrayList<>();
        ConsoleViewModel consoleViewModel0 = new ConsoleViewModel();
        consoleViewModel0.setChronicleRecordLine(new ChronicleLine(1, 1, "work", "desc", new Timestamp(System.currentTimeMillis())));
        consoleViewModel0.setDuration(Duration.millis(System.currentTimeMillis()));
        consoleViewModel0.setTotalDuration(Duration.ZERO);
        ConsoleViewModel consoleViewModel1 = new ConsoleViewModel();
        consoleViewModel1.setChronicleRecordLine(new ChronicleLine(2, 2, "break", "desc", new Timestamp(System.currentTimeMillis())));
        consoleViewModel1.setDuration(Duration.millis(System.currentTimeMillis()));
        consoleViewModel1.setTotalDuration(Duration.ZERO);
        ConsoleViewModel consoleViewModel2 = new ConsoleViewModel();
        consoleViewModel2.setChronicleRecordLine(new ChronicleLine(3, 3, "work", "desc", new Timestamp(System.currentTimeMillis())));
        consoleViewModel2.setDuration(Duration.millis(System.currentTimeMillis()));
        consoleViewModel2.setTotalDuration(Duration.ZERO);
        lines.add(consoleViewModel0);
        lines.add(consoleViewModel1);
        lines.add(consoleViewModel2);

        File templateFolder = new File("src/test/resources");
        Configuration cfg = new Configuration();
        cfg.setDirectoryForTemplateLoading(templateFolder);
        cfg.setEncoding(new Locale("pl", "PL"), "UTF-8");
        cfg.setObjectWrapper(new DefaultObjectWrapper());
        cfg.setDefaultEncoding("UTF-8");
        cfg.setOutputEncoding("UTF-8");
        BeansWrapper bw = new BeansWrapper();
        final Template template = cfg.getTemplate("template.ftl");
        Map<String, Object> root = new HashMap();
        root.put("lines", lines);
        Writer out = new OutputStreamWriter(System.out);
        template.process(root, out);
        out.flush();
        out.close();
    }

//    @Test
    public void parseTemplateUsingTemplateObject() throws Exception {
        final ConsoleViewModel consoleViewModel = new ConsoleViewModel();
        consoleViewModel.setChronicleRecordLine(new ChronicleLine(1, 1, "work", "work description", new Timestamp(System.currentTimeMillis())));
        consoleViewModel.setDuration(new Duration(System.currentTimeMillis()));
        consoleViewModel.setTotalDuration(new Duration(System.currentTimeMillis()));
        List<ConsoleViewModel> models = new ArrayList<>();
        models.add(consoleViewModel);
        Templating templating = new Templating();
        templating.addParameter("lines", models);
        templating.addParameter("durations", null);
        templating.parseTemplate();
    }

    @Test
    public void consumeMapInTemplate() throws Exception {
        int rowsAtStart = chronicleDao.findRecordsCount();
        assertThat(rowsAtStart).isEqualTo(0);
        // line 0
        ChronicleLine line_1 = new ChronicleLine();
        line_1.setTag("work");
        line_1.setDescription("line0 description");
        DateTime dateTime = new DateTime(2014, 11, 24, 0, 0, 0, 0);
        line_1.setTimestamp(new Timestamp(dateTime.getMillis()));
        chronicleDao.insertNewRecord(line_1);

        ChronicleLine line0 = new ChronicleLine();
        line0.setTag("work0");
        line0.setDescription("line0 description");
        dateTime = new DateTime(2014, 11, 24, 0, 1, 0, 0);
        line0.setTimestamp(new Timestamp(dateTime.getMillis()));
        chronicleDao.insertNewRecord(line0);

        ChronicleLine line1 = new ChronicleLine();
        line1.setTag("work");
        line1.setDescription("line1 description");
        dateTime = new DateTime(2014, 11, 24, 0, 6, 0, 0);
        line1.setTimestamp(new Timestamp(dateTime.getMillis()));
        chronicleDao.insertNewRecord(line1);
        int work0DurationMinutes = 6;

        ChronicleLine line2 = new ChronicleLine();
        line2.setTag("work2");
        line2.setDescription("line2 description");
        dateTime = new DateTime(2014, 11, 24, 0, 10, 0, 0);
        line2.setTimestamp(new Timestamp(dateTime.getMillis()));
        chronicleDao.insertNewRecord(line2);
        int work1DurationMinutes = 4;

        int rowsAfterAdds = chronicleDao.findRecordsCount();
        assertThat(rowsAfterAdds).isEqualTo(rowsAtStart + 4);

        final List<ChronicleLine> lastNLines = chronicleDao.findLastNRecordsUpwards(4);
//        assertThat(last3Lines).hasSize(3);
//        assertThat(line2).isEqualTo(last3Lines.get(0));

        final List<ChronicleLine> lines = calculator.calculateDurations(lastNLines);

        final ConsoleViewModel consoleViewModel = new ConsoleViewModel();
        consoleViewModel.setChronicleRecordLine(new ChronicleLine(1, 1, "work", "work description", new Timestamp(System.currentTimeMillis())));
        consoleViewModel.setDuration(new Duration(System.currentTimeMillis()));
        consoleViewModel.setTotalDuration(new Duration(System.currentTimeMillis()));
        List<ConsoleViewModel> models = new ArrayList<>();
        models.add(consoleViewModel);
        //
        Templating templating = new Templating();
        templating.addParameter("lines", lines);
        templating.addParameter("namingRecords", chronicleDao.findLastNNamingRecordsDownwards(2));
        templating.parseTemplate();
    }

}
