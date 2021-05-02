package com.epam.rd.java.basic.practice8.db;

import com.epam.rd.java.basic.practice8.db.entity.Team;
import com.epam.rd.java.basic.practice8.db.entity.User;

import java.io.FileInputStream;
import java.io.IOException;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

public class DBManager {

    private static DBManager dbManager;
    private String url;


    public static User getUser(String login) {
        String sql = "SELECT id,login FROM users WHERE login=?;";
        User user = null;
        try (Connection connection = DBManager.getInstance().getConnection(dbManager.url);
             PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setString(1, login);
            ResultSet resultSet = statement.executeQuery(sql);
            user = User.createUser(resultSet.getString("login"));
            user.setId(resultSet.getInt("id"));
        } catch (SQLException throwables) {
            System.err.println(throwables.getMessage());
        }
        return user;
    }

    public static Team getTeam(String name) {
        String sql = "SELECT id,name FROM teams WHERE name=?;";
        Team team = null;
        try (Connection connection = DBManager.getInstance().getConnection(dbManager.url);
             PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setString(1, name);
            ResultSet resultSet = statement.executeQuery(sql);
            team = Team.createTeam(resultSet.getString("name"));
            team.setId(resultSet.getInt("id"));
        } catch (SQLException throwables) {
            System.err.println(throwables.getMessage());
        }
        return team;
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
        String sql = "INSERT INTO users VALUES (id,?);";
        try (Connection connection = dbManager.getConnection(dbManager.url);
             PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setString(1, user.getLogin());
            statement.executeUpdate();
        } catch (SQLException throwables) {
            System.err.println(throwables.getMessage());
        }
    }

    public static void insertTeam(Team team) {
        String sql = "INSERT INTO teams VALUES (id,?);";
        try (Connection connection = dbManager.getConnection(dbManager.url);
             PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setString(1, team.getName());
            statement.executeUpdate();
        } catch (SQLException throwables) {
            System.err.println(throwables.getMessage());
        }
    }

    public List<User> findAllUsers() {
        List<User> userList = new ArrayList<>();
        String sql = "SELECT id,login FROM users;";
        try (Connection connection = DBManager.getInstance().getConnection(dbManager.url);
             Statement statement = connection.createStatement()) {
            ResultSet resultSet = statement.executeQuery(sql);
            while (resultSet.next()) {
                User user = User.createUser(resultSet.getString("login"));
                user.setId(resultSet.getInt("id"));
                userList.add(user);
            }
        } catch (SQLException throwables) {
            System.err.println(throwables.getMessage());
        }
        return userList;
    }

    public List<Team> findAllTeams() {
        List<Team> teamList = new ArrayList<>();
        String sql = "SELECT id,name FROM teams;";
        try (Connection connection = DBManager.getInstance().getConnection(dbManager.url);
             Statement statement = connection.createStatement()) {
            ResultSet resultSet = statement.executeQuery(sql);
            while (resultSet.next()) {
                Team team = Team.createTeam(resultSet.getString("name"));
                team.setId(resultSet.getInt("id"));
                teamList.add(team);
            }
        } catch (SQLException throwables) {
            System.err.println(throwables.getMessage());
        }
        return teamList;
    }

    public void setTeamsForUser(User user, Team team) {

    }

    public void setTeamsForUser(User user, Team team1, Team team2) {

    }

    public void setTeamsForUser(User user, Team team1, Team team2, Team team3) {

    }

    public List<Team> getUserTeams(User user) {
        String sql = "SELECT users.login,teams.name FROM users JOIN users_teams ON users.id = users_teams.user_id " +
                "JOIN teams ON teams.id=users_teams.team_id WHERE users.login=?;";
        List<Team> teamList = new ArrayList<>();
        try (Connection connection = DBManager.getInstance().getConnection(dbManager.url);
             PreparedStatement statement = connection.prepareStatement(sql);) {
            statement.setString(1, user.getLogin());
            ResultSet resultSet = statement.executeQuery();
            while (resultSet.next()) {
                Team team = Team.createTeam(resultSet.getString("name"));
                teamList.add(team);
            }
        } catch (SQLException throwables) {
            System.err.println(throwables.getMessage());
        }
        return teamList;
    }

    public void deleteTeam(Team teamA) {
        String sql = "DELETE FROM teams WHERE id=?";
        try (Connection connection = DBManager.getInstance().getConnection(dbManager.url);
             PreparedStatement statement = connection.prepareStatement(sql);){
            statement.setInt(1,teamA.getId());
            statement.executeUpdate();
        } catch (SQLException throwables) {
            System.err.println(throwables.getMessage());
        }
    }

    public void updateTeam(Team teamC) {
        String sql = "UPDATE teams SET name=? WHERE id=?;";
        try (Connection connection = DBManager.getInstance().getConnection(dbManager.url);
             PreparedStatement statement = connection.prepareStatement(sql);) {
            statement.setString(1,teamC.getName());
            statement.setInt(2,teamC.getId());
            statement.executeUpdate();
        } catch (SQLException throwables) {
            System.err.println(throwables.getMessage());
        }
    }
}
