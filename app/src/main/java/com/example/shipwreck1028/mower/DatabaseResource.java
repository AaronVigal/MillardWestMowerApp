package com.example.shipwreck1028.mower;

import android.util.Log;

import java.sql.*;
import org.json.JSONArray;
import org.json.JSONObject;
public class DatabaseResource {
    private Connection con;
    private Statement st;
    private ResultSet rs;
    public DatabaseResource() {
        try {
            String dbUri = "jdbc:mysql://174.74.57.191:22222/mowerdb";
            Log.w("Action","Connecting to connect to database");
            Class.forName("com.mysql.jdbc.Driver");
            con = DriverManager.getConnection(dbUri, "avigal167", "neky4302");
            st = con.createStatement();
            Log.w("Success:", "Connected to database: " + dbUri);
        } catch (Exception ex) {
            Log.w("Failed", "Database connection error: " + ex);
        }
    }
    public String getData(String sqlQuery) {
        JSONArray json = new JSONArray();
        try {
            Log.w("Action","Querying table");
            rs = st.executeQuery(sqlQuery);
            Log.w("Success","SQL query was executed.");
            ResultSetMetaData rsmd = rs.getMetaData();
            int numColumns = rsmd.getColumnCount();
            while (rs.next()) {
                JSONObject obj = new JSONObject();
                for (int i = 1; i < numColumns + 1; i++) {
                    String column_name = rsmd.getColumnName(i);
                    if (rsmd.getColumnType(i) == java.sql.Types.ARRAY) {
                        obj.put(column_name, rs.getArray(column_name));
                    }
                    else if (rsmd.getColumnType(i) == java.sql.Types.BIGINT) {
                        obj.put(column_name, rs.getInt(column_name));
                    }
                    else if (rsmd.getColumnType(i) == java.sql.Types.BOOLEAN) {
                        obj.put(column_name, rs.getBoolean(column_name));
                    }
                    else if (rsmd.getColumnType(i) == java.sql.Types.BLOB) {
                        obj.put(column_name, rs.getBlob(column_name));
                    }
                    else if (rsmd.getColumnType(i) == java.sql.Types.DOUBLE) {
                        obj.put(column_name, rs.getDouble(column_name));
                    }
                    else if (rsmd.getColumnType(i) == java.sql.Types.FLOAT) {
                        obj.put(column_name, rs.getFloat(column_name));
                    }
                    else if (rsmd.getColumnType(i) == java.sql.Types.INTEGER) {
                        obj.put(column_name, rs.getInt(column_name));
                    }
                    else if (rsmd.getColumnType(i) == java.sql.Types.NVARCHAR) {
                        obj.put(column_name, rs.getNString(column_name));
                    }
                    else if (rsmd.getColumnType(i) == java.sql.Types.VARCHAR) {
                        obj.put(column_name, rs.getString(column_name));
                    }
                    else if (rsmd.getColumnType(i) == java.sql.Types.TINYINT) {
                        obj.put(column_name, rs.getInt(column_name));
                    }
                    else if (rsmd.getColumnType(i) == java.sql.Types.SMALLINT) {
                        obj.put(column_name, rs.getInt(column_name));
                    }
                    else if (rsmd.getColumnType(i) == java.sql.Types.DATE) {
                        obj.put(column_name, rs.getDate(column_name));
                    }
                    else if (rsmd.getColumnType(i) == java.sql.Types.TIMESTAMP) {
                        obj.put(column_name, rs.getTimestamp(column_name));
                    }
                    else {
                        obj.put(column_name, rs.getObject(column_name));
                    }
                }
                json.put(obj);
            }
        } catch (Exception ex) {
            Log.w("Failed", "Error formatting database response: " + ex);
        }
        String[] outputJSON = json.toString().replace("},{", " ,").split(" ");
        Log.w("Success","Parsed and formatted database response");
        String finalOutput = "";
        for (int i = 0; i < outputJSON.length; i++) {
            finalOutput = finalOutput + outputJSON[i];
        }
        return finalOutput;
    }
}