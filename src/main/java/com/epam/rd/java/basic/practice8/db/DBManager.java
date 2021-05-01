package com.epam.rd.java.basic.practice8.db;

import com.epam.rd.java.basic.practice8.db.entity.Team;
import com.epam.rd.java.basic.practice8.db.entity.User;

import java.io.FileInputStream;
import java.io.IOException;
import java.sql.*;
import java.util.List;
import java.util.Properties;

public class DBManager {

    private static DBManager dbManager;
    private String url;


    public static User getUser(String login) {
        return null;
    }

    public static Team getTeam(String name) {
        return null;
    }

    private DBManager() {
    }

    public static DBManager getInstance() {
        if (dbManager == null) {
            dbManager = new DBManager();
            try (FileInputStream fis = new FileInputStream("app.properties")) {
                Properties properties = new Properties();
                properties.load(fis);
                dbManager.url = properties.getProperty("connection.url");
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return dbManager;
    }

    public Connection getConnection(String connectionUrl) throws SQLException {
        Connection connection = null;
        DriverManager.registerDriver(new com.mysql.cj.jdbc.Driver());
        connection = DriverManager.getConnection(connectionUrl);
        return connection;
    }


    public static void insertUser(User user) {
        try {
            Connection connection = dbManager.getConnection(dbManager.url);
            String sql = "INSERT INTO users VALUES (id,?);";
           PreparedStatement statement = connection.prepareStatement(sql);
            statement.setString(1, user.getLogin());
            System.out.println(statement.executeUpdate());
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }

    }

    public static void insertTeam(Team team) {

    }

    public List<User> findAllUsers() {
        return null;
    }

    public List<Team> findAllTeams() {
        return null;
    }

    public void setTeamsForUser(User user, Team team) {

    }

    public void setTeamsForUser(User user, Team team1, Team team2) {

    }

    public void setTeamsForUser(User user, Team team1, Team team2, Team team3) {

    }

    public List<Team> getUserTeams(User user) {
        return null;
    }

    public void deleteTeam(Team teamA) {
    }

    public void updateTeam(Team teamC) {
    }
}
