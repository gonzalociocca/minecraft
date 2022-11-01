package server.user;

import com.google.gson.annotations.Expose;
import server.api.DatabaseAPI;
import server.api.database.SqlCallback;
import server.user.column.Simple.SimpleData;
import server.user.column.Valuable.ValuableData;
import server.util.UtilGson;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.UUID;

/**
 * Created by noname on 24/3/2017.
 */
public class User {

    public int playerid = -1;
    public boolean isLoaded = false;
    @Expose
    String _nameLowerCase;
    @Expose
    String _realName;
    @Expose
    UUID _uuid;
    // Update on current server only.
    SimpleData _simpleData = new SimpleData();
    // Update on store server only.
    ValuableData _valuableData = new ValuableData();
    boolean hasBeenInserted = false;

    public User(String nameLowerCase, String realName) {
        _nameLowerCase = nameLowerCase;
        _realName = realName;
        load(true, true);
    }

    public String getLowercaseName() {
        return _nameLowerCase;
    }

    public String getRealName() {
        return _realName;
    }

    public boolean canSave() {
        return playerid != -1 && isLoaded;
    }

    public SimpleData getSimpleData() {
        return _simpleData;
    }

    public ValuableData getValuableData() {
        return _valuableData;
    }

    public void saveCheck(){
        if (canSave() && getSimpleData().shouldSave()) {
            try {
                save(true, false);
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

    public void save(boolean simpleData, boolean valuableData) throws SQLException {
        if (simpleData) {
            DatabaseAPI.addQuery(_nameLowerCase, getSimpleDataQuery());
        }
        if (valuableData) {
            DatabaseAPI.addQuery(_nameLowerCase, getValuableDataQuery());
        }
    }

    public void parse(ResultSet resultSet, boolean simpleData, boolean valuableData) throws SQLException {
        if (simpleData) {
            SimpleData newSimpleData = UtilGson.getSimple().fromJson(resultSet.getString(SimpleData.getColumn()), SimpleData.class);
            if(newSimpleData != null){
                _simpleData = newSimpleData;
            }
        }
        if (valuableData) {
            ValuableData newValuableData = UtilGson.getSimple().fromJson(resultSet.getString(ValuableData.getColumn()), ValuableData.class);
            if(newValuableData != null){
                _valuableData = newValuableData;
            }
        }
    }

    public void load(boolean simpleData, boolean valuableData) {
        String sqlQuery = "SELECT " + (simpleData && valuableData ? SimpleData.getColumn() + "," + ValuableData.getColumn() : simpleData ? SimpleData.getColumn() : valuableData ? ValuableData.getColumn() : "") + " FROM " + DatabaseAPI.getUserTable() + " WHERE Name = '" + _nameLowerCase + "'";

        SqlCallback sqlCallback = new SqlCallback() {
            @Override
            public void onSuccess(ResultSet resultSet) throws SQLException {
                if(resultSet.next()){
                    parse(resultSet, simpleData, valuableData);
                    isLoaded = simpleData && valuableData;
                } else {
                    insertIfNotExists();
                }
            }
        };

        DatabaseAPI.addQuery(_nameLowerCase, sqlQuery, sqlCallback);
    }

    public boolean insertIfNotExists() {
            String sqlinsert = "INSERT INTO `" + DatabaseAPI.getUserTable() + "` ("
                    + "Name" + ","
                    + "UUID" + ","
                    + SimpleData.getColumn() + ","
                    + ValuableData.getColumn()
                    + ") " +
                    "  SELECT '" + getLowercaseName() + "',"
                    + "'" + "" + "',"
                    + "'" + "" + "',"
                    + "'" + "" + "'"
                    + " FROM dual " +
                    "WHERE NOT EXISTS " +
                    "( SELECT Name FROM " + DatabaseAPI.getUserTable() + " WHERE Name='" + getLowercaseName() + "' );";

            DatabaseAPI.addQuery(_nameLowerCase, sqlinsert, new SqlCallback() {
                @Override
                public void onSuccess(ResultSet resultSet) throws SQLException {
                    load(true, true);
                }
            });
            return true;
    }

    public String getSimpleDataQuery() {
        String query = null;

        if (_simpleData != null && playerid != -1) {
            query = "UPDATE " + DatabaseAPI.getUserTable() + " SET " +
                    _simpleData.getColumn() + " ='" + UtilGson.getSimple().toJson(_simpleData, SimpleData.class) + "'" +
                    " WHERE ID = '" + playerid + "'";
        }

        return query;
    }

    public String getValuableDataQuery() {
        String query = null;

        if (_valuableData != null && playerid != -1) {
            query = "UPDATE " + DatabaseAPI.getUserTable() + " SET " +
                    _valuableData.getColumn() + " ='" + UtilGson.getSimple().toJson(_valuableData, ValuableData.class) + "'" +
                    " WHERE ID = '" + playerid + "'";
        }

        return query;
    }

    @Override
    public boolean equals(Object o) {
        if (o instanceof User) {
            User other = (User) o;
            return other.getLowercaseName().equals(getLowercaseName());
        }
        return false;
    }

}
