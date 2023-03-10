package com.plaidmrdeer.silentvoice.sql;

import java.sql.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author PlaidMrdeer
 */
public class MySql {
    private Connection c;
    private Statement stmt;

    public void connectMySql(String hostname, String port, String databaseName, String username, String password) throws Exception {
        Class.forName("com.mysql.jdbc.Driver");
        String url = "jdbc:mysql://" + hostname + ":" + port + "/" + databaseName;
        c = DriverManager.getConnection(url, username, password);
        if (c == null) {
            throw new RuntimeException();
        }
    }

    public void createTable(String tableName) throws Exception {
        String sqlTable = "CREATE TABLE IF NOT EXISTS `" + tableName + "` (" +
                          "`ID` INT PRIMARY KEY AUTO_INCREMENT NOT NULL," +
                          "`MESSAGE` TEXT NOT NULL," +
                          "`TIME` TEXT NOT NULL" +
                          ") ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;";
        stmt = c.createStatement();
        stmt.executeUpdate(sqlTable);
        stmt.close();
    }

    public void insert(String tableName, String message, String time) throws SQLException {
        String sql = "INSERT INTO " + tableName + " (`MESSAGE`, `TIME`) VALUES (?, ?)";
        PreparedStatement pstmt = c.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
        pstmt.setString(1, message);
        pstmt.setString(2, time);
        pstmt.executeUpdate();

        pstmt.close();
    }

    public Map<Integer, List<String>> queryList(String tableName) throws Exception {
        Map<Integer, List<String>> messageList = new HashMap<>();
        stmt = c.createStatement();
        ResultSet rs = stmt.executeQuery("SELECT 1 FROM `" + tableName + "` LIMIT 1");
        boolean exists = rs.next();
        if (!exists) {
            return messageList;
        }

        rs = stmt.executeQuery("SELECT * FROM `" + tableName + "`");
        while (rs.next()) {
            int id = rs.getInt("ID");
            List<String> list = new ArrayList<>();
            list.add(rs.getString("MESSAGE"));
            list.add(rs.getString("TIME"));
            messageList.put(id, list);
        }
        rs.close();
        stmt.close();

        return messageList;
    }

    public void closeMySql() throws SQLException {
        if (c == null) {
            return;
        }
        c.close();
    }
}
