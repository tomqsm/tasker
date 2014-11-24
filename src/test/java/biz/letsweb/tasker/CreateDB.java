package biz.letsweb.tasker;

import biz.letsweb.tasker.databaseconnectivity.DataSourcePrepare;
import java.sql.Connection;
import java.sql.SQLException;

/**
 *
 * @author toks
 */
public class CreateDB {

    public void createDb() throws ClassNotFoundException, SQLException {
        DataSourcePrepare dataSourcePrepare = new DataSourcePrepare(DataSourcePrepare.Type.EMBEDDED);
        try (Connection connection = dataSourcePrepare.getDataSource().getConnection()) {
            connection.createStatement().execute("CREATE TABLE chronicle (\n"
                    + "    id INT NOT NULL CONSTRAINT chronicle_pk PRIMARY KEY,\n"
                    + "    tag VARCHAR(50) DEFAULT NULL,\n"
                    + "    description VARCHAR(400) DEFAULT NULL,\n"
                    + "    inserted TIMESTAMP DEFAULT CURRENT_TIMESTAMP\n"
                    + ")");
            connection.createStatement().execute("CREATE TABLE comment (\n"
                    + "    id INT NOT NULL CONSTRAINT comment_pk PRIMARY KEY,\n"
                    + "    chronicleId INT,\n"
                    + "    description VARCHAR(400) DEFAULT NULL,\n"
                    + "    inserted TIMESTAMP DEFAULT CURRENT_TIMESTAMP,\n"
                    + "    CONSTRAINT chronicleId_fk FOREIGN KEY (chronicleId) REFERENCES chronicle (id)\n"
                    + ")");
        }
    }

    public void emptyDb() throws SQLException, ClassNotFoundException {
        ConnectDB connectDB = new ConnectDB();
        try (Connection connection = connectDB.getConnection()) {
            connection.createStatement().execute("delete from chronicle");
            connection.createStatement().execute("delete from comment");
        }
    }

    public void loadDb() throws ClassNotFoundException, SQLException {
        String insert = "insert into chronicle (id, tag, description, inserted) values (1, 'work', null, '2014-11-21 17:01:44.188')\n"
                + "insert into chronicle (id, tag, description, inserted) values (2, 'break', null, '2014-11-21 17:01:44.188')";
        ConnectDB connectDB = new ConnectDB();
        try (Connection connection = connectDB.getConnection()) {
            connection.createStatement().execute(insert);
        }
    }

}
