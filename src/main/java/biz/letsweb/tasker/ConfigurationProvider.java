package biz.letsweb.tasker;

import java.io.File;
import java.util.logging.Level;
import org.apache.commons.configuration.ConfigurationException;
import org.apache.commons.configuration.XMLConfiguration;
import org.apache.commons.configuration.tree.xpath.XPathExpressionEngine;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 * @author toks
 */
public class ConfigurationProvider {

    private final Logger LOG = LoggerFactory.getLogger(ConfigurationProvider.class);
    private final XMLConfiguration config;

    public ConfigurationProvider() {
        config = new XMLConfiguration();
        config.setExpressionEngine(new XPathExpressionEngine());
    }

    public ConfigurationProvider(final String fileName) {
        this();
        initializeWithFileName(fileName);
    }

    private void initializeWithFileName(String fileName) {
        config.setFileName(fileName);
        try {
            config.load();
        } catch (ConfigurationException ex) {
            LOG.error("Error while initialising configuration with file name: {}", fileName);
        }
    }

    public XMLConfiguration getXMLConfiguration() {
        return config;
    }
}
