package biz.letsweb.tasker.templating;

import biz.letsweb.tasker.model.ConsoleViewModel;
import biz.letsweb.tasker.persistence.model.ChronicleRecordLine;
import freemarker.ext.beans.BeansWrapper;
import freemarker.template.Configuration;
import freemarker.template.DefaultObjectWrapper;
import freemarker.template.ObjectWrapper;
import freemarker.template.Template;
import java.io.File;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import org.joda.time.Duration;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

/**
 *
 * @author toks
 */
public class TemplatingTest {

    public TemplatingTest() {
    }

    @BeforeClass
    public static void setUpClass() {
    }

    @AfterClass
    public static void tearDownClass() {
    }

    @Before
    public void setUp() {
    }

    @After
    public void tearDown() {
    }

    /**
     * Test of parseTemplate method, of class Templating.
     */
    @Test
    public void dontKnowHowToPassJsonToFreemarker() throws Exception {
        String json = "{\n"
                + "  \"chronicleRecordLine\" : {\n"
                + "    \"id\" : 1,\n"
                + "    \"count\" : 1,\n"
                + "    \"tag\" : \"work\",\n"
                + "    \"description\" : \"desc\",\n"
                + "    \"timestamp\" : 1416996687187\n"
                + "  },\n"
                + "  \"duration\" : {\n"
                + "    \"standardDays\" : 0,\n"
                + "    \"standardHours\"    : 0,\n"
                + "    \"standardMinutes\" : 0,\n"
                + "    \"standardSeconds\" : 0,\n"
                + "    \"millis\" : 0\n"
                + "  },\n"
                + "  \"totalDuration\" : {\n"
                + "    \"standardDays\" : 0,\n"
                + "    \"standardHours\" : 0,\n"
                + "    \"standardMinutes\" : 0,\n"
                + "    \"standardSeconds\" : 0,\n"
                + "    \"millis\" : 0\n"
                + "  }";
        File templateFolder = new File("src/test/resources");
        Configuration cfg = new Configuration();
        cfg.setDirectoryForTemplateLoading(templateFolder);
        cfg.setEncoding(new Locale("pl", "PL"), "UTF-8");
//        cfg.setObjectWrapper(ObjectWrapper.DEFAULT_WRAPPER);
        cfg.setObjectWrapper(ObjectWrapper.BEANS_WRAPPER);
        cfg.setDefaultEncoding("UTF-8");
        cfg.setOutputEncoding("UTF-8");
        final Template template = cfg.getTemplate("template.ftl");
        Map<String, Object> root = new HashMap();
//        InputStream inputStream = new ByteArrayInputStream(json.getBytes());
//        InputSource inputSource = new InputSource(inputStream);
//        final NodeModel parsed = NodeModel.parse(inputSource);
//        root.put("xml", parsed);
        root.put("json", json);
        Writer out = new OutputStreamWriter(System.out);
        template.process(root, out);

    }

    @Test
    public void passingObjectToFreemarker() throws Exception {
        List<ConsoleViewModel> lines = new ArrayList<>();
        ConsoleViewModel consoleViewModel = new ConsoleViewModel();
        consoleViewModel.setChronicleRecordLine(new ChronicleRecordLine(1, 1, "work", "desc", new Timestamp(System.currentTimeMillis())));
        consoleViewModel.setDuration(Duration.ZERO);
        consoleViewModel.setTotalDuration(Duration.ZERO);
        lines.add(consoleViewModel);
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
        root.put("linez", bw.wrap(consoleViewModel));
        root.put("lines", lines);
        Writer out = new OutputStreamWriter(System.out);
        template.process(root, out);

    }

}
