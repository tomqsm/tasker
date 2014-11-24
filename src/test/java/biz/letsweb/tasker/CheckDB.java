package biz.letsweb.tasker;

import java.io.File;

/**
 *
 * @author toks
 */
public class CheckDB {

    public String getProjectDirectory() {
        return new File("").getAbsolutePath();
    }

    public String getDbPath() {
        return String.format("%s%s%s", getProjectDirectory(), File.separator, ConnectDB.DB_NAME);
    }

    public boolean dbDirectoryExists() {
        File dbDir = new File(getDbPath());
        return dbDir.exists() && dbDir.isDirectory();
    }
}
