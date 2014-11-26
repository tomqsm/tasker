package biz.letsweb.tasker.templating;

import freemarker.ext.dom.NodeModel;
import freemarker.template.Configuration;
import freemarker.template.ObjectWrapper;
import freemarker.template.Template;
import freemarker.template.TemplateException;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import javax.xml.parsers.ParserConfigurationException;
import org.xml.sax.SAXException;

public class Templating {

    private final File templateFile;
    private final File dataXmlFile;
    private final File directoryForTemplateLoading;
    private final File output;
    private Template template;
    private final Map<String, Object> root;
    private final ConfigStruct configStruct;

    public Templating(final ConfigStruct configStruct) {
        this.configStruct = configStruct;
        templateFile = configStruct.getTemplateFile();
        dataXmlFile = configStruct.getDataXmlFile();
        directoryForTemplateLoading = configStruct.getDirectoryForTemplateLoading();
        output = configStruct.getOutput();
        root = new HashMap();
        initializeTemplate();
    }

    private Configuration prepareTemplateConfiguration() throws IOException {
        Configuration cfg = new Configuration();
        cfg.setDirectoryForTemplateLoading(directoryForTemplateLoading);
        cfg.setEncoding(new Locale("pl", "PL"), "UTF-8");
        cfg.setObjectWrapper(ObjectWrapper.DEFAULT_WRAPPER);
        cfg.setDefaultEncoding("UTF-8");
        cfg.setOutputEncoding("UTF-8");
        return cfg;
    }

    private void initializeTemplate() {
        try {
            final Configuration cfg = prepareTemplateConfiguration();
            template = cfg.getTemplate(templateFile.getName());
        } catch (final IOException ex) {
            throw new RuntimeException(ex);
        }
    }

    public void parseTemplate() throws IOException, TemplateException, SAXException, ParserConfigurationException {
        loadXmlData(root, dataXmlFile);
        loadAnyData();
        final FileWriter writer = new FileWriter(output);
        template.process(root, writer);
        writer.flush();
        writer.close();
    }

    private void addParameter(final String key, final Object value) {
        root.put(key, value);
    }

    private void loadXmlData(final Map root, final File xml) throws SAXException, IOException, ParserConfigurationException {
        final NodeModel parsed = NodeModel.parse(xml);
        root.put("xml", parsed);
    }

    private void loadAnyData() {
        addParameter("time", new Date());
        addParameter("currentWeek", "not set");
        addParameter("version", "123");
    }
}   