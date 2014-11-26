package biz.letsweb.tasker.templating;

import java.io.File;

/**
 *
 * @author kusmierc
 */
public class ConfigStruct implements Cloneable{

    private File templateFile = new File("template.ftl");
    private File dataXmlFile = new File("src/main/resources/freemarker/withNspace/report.xml");
    private File directoryForTemplateLoading = new File("src/main/resources/freemarker/withNspace/");
    private File output = new File("src/main/resources/freemarker/withNspace/output.txt");

    public File getTemplateFile() {
        return templateFile;
    }

    public void setTemplateFile(File templateFile) {
        this.templateFile = templateFile;
    }

    public File getDataXmlFile() {
        return dataXmlFile;
    }

    public void setDataXmlFile(File dataXmlFile) {
        this.dataXmlFile = dataXmlFile;
    }

    public File getDirectoryForTemplateLoading() {
        return directoryForTemplateLoading;
    }

    public void setDirectoryForTemplateLoading(File directoryForTemplateLoading) {
        this.directoryForTemplateLoading = directoryForTemplateLoading;
    }

    public File getOutput() {
        return output;
    }

    public void setOutput(File output) {
        this.output = output;
    }

    @Override
    protected Object clone() throws CloneNotSupportedException {
        return super.clone(); 
    }
}
