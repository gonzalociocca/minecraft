package server.api.database;

/**
 * Created by noname on 24/5/2017.
 */
public class SqlQuery {

    private String _sqlQuery;
    private SqlCallback _sqlCallback;

    public SqlQuery(String sqlQuery, SqlCallback sqlCallback){
        _sqlQuery = sqlQuery;
        _sqlCallback = sqlCallback;
    }

    public String getQuery(){
        return _sqlQuery;
    }

    public boolean hasCallback(){
        return _sqlCallback != null;
    }

    public SqlCallback getCallback(){
        return _sqlCallback;
    }

}
