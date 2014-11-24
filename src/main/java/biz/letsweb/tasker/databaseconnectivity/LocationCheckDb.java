package biz.letsweb.tasker.databaseconnectivity;

import java.io.File;

/**
 *
 * @author toks
 */
public class LocationCheckDb {

  public static String DB_NAME = "testdb";

  public String getProjectDirectory() {
    return new File("").getAbsolutePath();
  }

  public String getDbPath() {
    return String.format("%s%s%s", getProjectDirectory(), File.separator, DB_NAME);
  }

  public boolean dbDirectoryExists() {
    File dbDir = new File(getDbPath());
    return dbDir.exists() && dbDir.isDirectory();
  }
}
