package com.example.shipwreck1028.mower;

import java.sql.SQLException;
import java.util.HashMap;
import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.dao.DaoManager;
import com.j256.ormlite.support.ConnectionSource;

public class DataSourceManager {
    private static DataSourceManager manager = new DataSourceManager( );

    @SuppressWarnings("rawtypes")
    private static HashMap<String, Dao> daoMap = new HashMap<String, Dao>();
    private static ConnectionSource source;
    private DataSourceManager(){}
    public static DataSourceManager getInstance( ) {
        return manager;
    }

    @SuppressWarnings("unchecked")
    protected static void addDao(@SuppressWarnings("rawtypes") Class daoClass) throws SQLException{
        daoMap.put(daoClass.getName(), DaoManager.createDao(source, daoClass));
    }

    @SuppressWarnings("rawtypes")
    public static Dao getDao(Class c){
        return daoMap.get(c.getName());
    }

    protected static void setConnectionSource(ConnectionSource s){
        source = s;
    }
}