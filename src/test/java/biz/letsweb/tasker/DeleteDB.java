package biz.letsweb.tasker;

import biz.letsweb.tasker.databaseconnectivity.LocationCheckDb;
import java.io.File;
import org.apache.derby.iapi.services.io.FileUtil;

/**
 *
 * @author toks
 */
public class DeleteDB {

    public boolean deleteDb() {
        LocationCheckDb checkDB = new LocationCheckDb();
        boolean deleted = false;
        if(checkDB.dbDirectoryExists()){
            File dbDir = new File(checkDB.getDbPath());
            deleted = FileUtil.removeDirectory(dbDir);
        } 
        return deleted;
    }
    
}
