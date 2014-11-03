package biz.letsweb.tasker.configuration;

import java.util.Date;
import org.apache.commons.configuration.ConfigurationException;
import org.apache.commons.configuration.HierarchicalConfiguration;
import org.apache.commons.configuration.SubnodeConfiguration;
import org.apache.commons.configuration.XMLConfiguration;
import static org.fest.assertions.Assertions.assertThat;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

/**
 *
 * @author toks
 */
public class ConfigurationProviderTest {

    private ConfigurationProvider configurationProvider;

    public ConfigurationProviderTest() {
    }

    @BeforeClass
    public static void setUpClass() {
    }

    @AfterClass
    public static void tearDownClass() {
    }

    @Before
    public void setUp() {
        configurationProvider = new ConfigurationProvider("src/test/resources/configuration.xml");
    }

    @After
    public void tearDown() {
    }

    /**
     * Test of getXMLConfiguration method, of class ConfigurationProvider.
     */
    @Test
    public void testGetXMLConfiguration() {
        ConfigurationProvider instance = new ConfigurationProvider();
        assertThat(instance).isNotNull();
        XMLConfiguration result = instance.getXMLConfiguration();
        assertThat(result).isNotNull();
    }

    @Test
    public void readsConfigurationFromFile() throws ConfigurationException {
        configurationProvider = new ConfigurationProvider("src/test/resources/configuration.xml");
        assertThat(configurationProvider).isNotNull();

        final XMLConfiguration xmlConfiguration = configurationProvider.getXMLConfiguration();
        assertThat(xmlConfiguration).isNotNull();

        final SubnodeConfiguration configurationAt = xmlConfiguration.configurationAt("properties");
        assertThat(configurationAt).isNotNull();
        final String colorString = configurationAt.getString("color");
        assertThat(colorString).isEqualTo("blue");

        final HierarchicalConfiguration get = xmlConfiguration.configurationsAt("properties").get(0);
        assertThat(get).isNotNull();
    }

    @Test
    public void savesConfigurationToFile() throws ConfigurationException {
        final XMLConfiguration xmlConfiguration = configurationProvider.getXMLConfiguration();
        final SubnodeConfiguration configurationAt = xmlConfiguration.configurationAt("properties");
        configurationAt.setProperty("time", new Date());
        xmlConfiguration.save();
    }

}
