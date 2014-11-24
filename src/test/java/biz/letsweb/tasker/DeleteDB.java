package biz.letsweb.tasker;

import java.io.File;
import org.apache.derby.iapi.services.io.FileUtil;

/**
 *
 * @author toks
 */
public class DeleteDB {

    public boolean deleteDb() {
        CheckDB checkDB = new CheckDB();
        boolean deleted = false;
        if(checkDB.dbDirectoryExists()){
            File dbDir = new File(checkDB.getDbPath());
            deleted = FileUtil.removeDirectory(dbDir);
        } 
        return deleted;
    }
    
}
