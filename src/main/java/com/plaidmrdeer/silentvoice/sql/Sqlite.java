package com.plaidmrdeer.silentvoice.sql;

import java.sql.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author PlaidMrdeer
 */
public class Sqlite {
    private Connection c;
    private Statement stmt;

    public void connectSqlite() throws Exception {
        Class.forName("org.sqlite.JDBC");
        c = DriverManager.getConnection("jdbc:sqlite:plugins/SilentVoice/silentvoice.db");
        if (c == null) {
            throw new RuntimeException();
        }
    }

    public void createTable(String tableName) throws Exception {
        String sqlTable = "CREATE TABLE " + tableName +
                " (ID INTEGER PRIMARY KEY AUTOINCREMENT    NOT NULL," +
                " MESSAGE                TEXT              NOT NULL," +
                " TIME                   TEXT              NOT NULL)";
        stmt = c.createStatement();
        ResultSet rs = stmt.executeQuery("SELECT name FROM sqlite_master WHERE type='table' AND name='" + tableName + "'" + ";");
        boolean exists = rs.next();
        if (!exists) {
            stmt.executeUpdate(sqlTable);
        }
        stmt.close();
    }

    public void insert(String tableName, String message, String time) throws SQLException {
        String sql = "INSERT INTO " + tableName + " VALUES(?,?,?)";
        PreparedStatement pstmt = c.prepareStatement(sql);
        pstmt.setString(2, message);
        pstmt.setString(3, time);
        pstmt.executeUpdate();
        pstmt.close();
    }

    public Map<Integer, List<String>> query(String tableName) throws Exception {
        Map<Integer, List<String>> messageList = new HashMap<>();
        stmt = c.createStatement();
        ResultSet rs = stmt.executeQuery("SELECT name FROM sqlite_master WHERE type='table' AND name='" + tableName + "'" + ";");
        boolean exists = rs.next();
        if (!exists) {
            return messageList;
        }

        rs = stmt.executeQuery("SELECT * FROM " + tableName);
        while (rs.next()) {
            int id = rs.getInt("id");
            List<String> list = new ArrayList<>();
            list.add(rs.getString("MESSAGE"));
            list.add(rs.getString("TIME"));
            messageList.put(id, list);
        }
        rs.close();
        stmt.close();

        return messageList;
    }

    public void closeSqlite() throws SQLException {
        if (c == null) {
            return;
        }
        c.close();
    }
}
