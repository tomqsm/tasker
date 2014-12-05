package biz.letsweb.tasker.db;

import java.io.File;
import org.apache.commons.configuration.SubnodeConfiguration;
import org.apache.commons.configuration.XMLConfiguration;
import org.apache.derby.iapi.services.io.FileUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 * @author toks
 */
public final class DbFileOperations {

    public static final Logger log = LoggerFactory.getLogger(DbFileOperations.class);

    public static SubnodeConfiguration DB_XML_NODE;
    /**
     * the tag in the configuration file should look like this:
     * <config><database></database><config>
     */
    private static final String DB_USER_TAG = "user";
    private static final String DB_PASSWORD_TAG = "password";
    private static final String DB_NAME_TAG = "databaseName";
    private static String DB_ROOT_FOLDER = "rootFolder";
    private static final String DB_SERVER_NAME_TAG = "serverName";
    private static final String DB_PORT_NUMBER_TAG = "portNumber";
    private static final String DB_CREATE_TAG = "create";
    private static final String DB_TYPE = "type";
    private static final String DB_XML_TAG = "database";
    private static DbFileOperations INSTANCE;

    public static synchronized DbFileOperations getInstance(final XMLConfiguration xmlConfiguration) {
        if (INSTANCE == null) {
            INSTANCE = new DbFileOperations(xmlConfiguration);
        }
        return INSTANCE;
    }

    private DbFileOperations() {
    }

    private DbFileOperations(final XMLConfiguration xmlConfiguration) {
        DB_XML_NODE = DB_XML_NODE == null ? xmlConfiguration.configurationAt(DB_XML_TAG) : DB_XML_NODE;
        initilaiseDbRootFolder();
    }

    private void initilaiseDbRootFolder() {
        final String rootFolderString = DB_XML_NODE.getString(DB_ROOT_FOLDER);
        if (rootFolderString == null || rootFolderString.isEmpty()) {
            DB_ROOT_FOLDER = getCurrentPath();
        } else {
            DB_ROOT_FOLDER = rootFolderString;
        }
    }

    private String getCurrentPath() {
        return new File("").getAbsolutePath();
    }

    public String getDbDirectoryPath() {
        return String.format("%s%s%s", DB_ROOT_FOLDER, File.separator, DB_XML_NODE.getString(DB_NAME_TAG));
    }

    public boolean dbDirectoryExists() {
        File dbDir = new File(getDbDirectoryPath());
        return dbDir.exists() && dbDir.isDirectory();
    }

    public boolean deleteDb() {
        boolean deleted = false;
        if (dbDirectoryExists()) {
            File dbDir = new File(getDbDirectoryPath());
            deleted = FileUtil.removeDirectory(dbDir);
        } else {
            log.warn("No deletion: {} doesn't exist.", this.getDbDirectoryPath());
        }
        return deleted;
    }

    public SubnodeConfiguration getDbConfiguration() {
        return DB_XML_NODE;
    }

    public void setDbConfiguration(SubnodeConfiguration dbConfiguration) {
        this.DB_XML_NODE = dbConfiguration;
    }

}
