package biz.letsweb.tasker.configuration;

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

  private ConfigurationProvider() {
    config = new XMLConfiguration();
    config.setExpressionEngine(new XPathExpressionEngine());
    config.setAttributeSplittingDisabled(true);
    config.setDelimiterParsingDisabled(true);
  }

  public ConfigurationProvider(final String xmlConfigFilePath) {
    this();
    initializeWithFileName(xmlConfigFilePath);
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
