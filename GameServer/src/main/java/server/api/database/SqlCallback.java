package server.api.database;

import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * Created by noname on 24/5/2017.
 */
public abstract class SqlCallback {
    public abstract void onSuccess(ResultSet resultSet) throws SQLException;
}
