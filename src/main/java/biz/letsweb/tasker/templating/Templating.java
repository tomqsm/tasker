package biz.letsweb.tasker.templating;

import freemarker.template.Configuration;
import freemarker.template.ObjectWrapper;
import freemarker.template.Template;
import freemarker.template.TemplateException;
import java.io.File;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.nio.charset.Charset;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import javax.xml.parsers.ParserConfigurationException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.xml.sax.SAXException;

public class Templating {

    public static final Logger log = LoggerFactory.getLogger(Templating.class);

    private Template template;
    private final Map<String, Object> root;
    private final File templateFolder;

    public Templating() {
        templateFolder = new File("");
        root = new HashMap();
        initializeTemplate();
    }

    private void initializeTemplate() {
        try {
            final Configuration cfg = prepareTemplateConfiguration();
            template = cfg.getTemplate("template.ftl");
        } catch (final IOException ex) {
            throw new RuntimeException(ex);
        }
    }

    private Configuration prepareTemplateConfiguration() throws IOException {
        Configuration cfg = new Configuration();
        cfg.setDirectoryForTemplateLoading(templateFolder);
        cfg.setEncoding(new Locale("pl", "PL"), "UTF-8");
        cfg.setObjectWrapper(ObjectWrapper.DEFAULT_WRAPPER);
        cfg.setDefaultEncoding("UTF-8");
        cfg.setOutputEncoding("UTF-8");
        return cfg;
    }

    public void parseTemplate() throws IOException, TemplateException, SAXException, ParserConfigurationException {
        loadMetaParameters();
        try (Writer out = new OutputStreamWriter(System.out, Charset.defaultCharset().displayName())) {
            template.process(root, out);
            out.flush();
        }
    }

    public void addParameter(final String key, final Object value) {
        root.put(key, value);
    }

    private void loadMetaParameters() {
        addParameter("time", new Date());
        addParameter("currentWeek", "not set");
        addParameter("version", "123");
    }
}
