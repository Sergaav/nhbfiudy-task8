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
        Connection connection = null;
        PreparedStatement statement = null;
        User user = null;
        try {
            connection = DBManager.getInstance().getConnection(dbManager.url);
            statement = connection.prepareStatement(sql);
            statement.setString(1, login);
            ResultSet resultSet = statement.executeQuery();
            while (resultSet.next()) {
                user = User.createUser(resultSet.getString("login"));
                user.setId(resultSet.getInt("id"));
            }
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        } finally {
            if (connection != null) {
                try {
                    connection.close();
                } catch (SQLException throwables) {
                    throwables.printStackTrace();
                }
            }

            if (statement != null) {
                try {
                    statement.close();
                } catch (SQLException throwables) {
                    throwables.printStackTrace();
                }
            }
        }
        return user;
    }

    public static Team getTeam(String name) {
        String sqlTeam = "SELECT id,name FROM teams WHERE name=?;";
        Team team = null;
        Connection connection = null;
        PreparedStatement statement = null;
        try {
            connection = DBManager.getInstance().getConnection(dbManager.url);
            statement = connection.prepareStatement(sqlTeam);
            statement.setString(1, name);
            ResultSet resultSet = statement.executeQuery();
            while (resultSet.next()) {
                team = Team.createTeam(resultSet.getString("name"));
                team.setId(resultSet.getInt("id"));
            }
        } catch (SQLException throwables) {
            System.err.println(throwables.getMessage());
        } finally {
            if (connection != null) {
                try {
                    connection.close();
                } catch (SQLException throwables) {
                    System.err.println(throwables.getMessage());
                }
            }
            if (statement != null) {
                try {
                    statement.close();
                } catch (SQLException throwables) {
                    System.err.println(throwables.getMessage());
                }
            }
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
                System.err.println(e.getMessage());
            }
        }
        return dbManager;
    }

    public Connection getConnection(String connectionUrl) throws SQLException {
        Connection connection;
        //   DriverManager.registerDriver(new com.mysql.cj.jdbc.Driver());
        connection = DriverManager.getConnection(connectionUrl);
        return connection;
    }

    public static void closeResultSet (ResultSet resultSet){
        if (resultSet != null){
            try {
                resultSet.close();
            } catch (SQLException throwables) {
                System.err.println(throwables.getMessage());
            }
        }
    }


    public static void insertUser(User user) {
        String sql = "INSERT INTO users VALUES (default,?);";
        ResultSet resultSet=null;
        try (Connection connection = dbManager.getConnection(dbManager.url);
             PreparedStatement statement = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            statement.setString(1, user.getLogin());
            statement.executeUpdate();
            resultSet = statement.getGeneratedKeys();
            if (resultSet.next()) {
                user.setId(resultSet.getInt(1));
            }
        } catch (SQLException throwables) {
            System.err.println(throwables.getMessage());
        }finally {
            closeResultSet(resultSet);
        }
    }

    public static void insertTeam(Team team) {
        String sql = "INSERT INTO teams VALUES (default,?);";
        ResultSet set=null;
        try (Connection connection = dbManager.getConnection(dbManager.url);
             PreparedStatement statement = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            statement.setString(1, team.getName());
            statement.executeUpdate();
            set = statement.getGeneratedKeys();
            if (set.next()) {
                team.setId(set.getInt(1));
            }
        } catch (SQLException throwables) {
            System.err.println(throwables.getMessage());
        }finally {
            closeResultSet(set);
        }

    }

    public List<User> findAllUsers() {
        List<User> userList = new ArrayList<>();
        String sql = "SELECT * FROM users;";
        ResultSet resultSet = null;
        try (Connection connection = DBManager.getInstance().getConnection(dbManager.url);
             PreparedStatement statement = connection.prepareStatement(sql)) {
            resultSet = statement.executeQuery();
            while (resultSet.next()) {
                User user = User.createUser(resultSet.getString("login"));
                user.setId(resultSet.getInt("id"));
                userList.add(user);
            }
        } catch (SQLException throwables) {
            System.err.println(throwables.getMessage());
        }finally {
            closeResultSet(resultSet);
        }
        return userList;
    }

    public List<Team> findAllTeams() {
        List<Team> teamList = new ArrayList<>();
        String sql = "SELECT * FROM teams;";
        ResultSet resultSet=null;
        try (Connection connection = DBManager.getInstance().getConnection(dbManager.url);
             PreparedStatement statement = connection.prepareStatement(sql)) {
            resultSet = statement.executeQuery();
            while (resultSet.next()) {
                Team team = Team.createTeam(resultSet.getString("name"));
                team.setId(resultSet.getInt("id"));
                teamList.add(team);
            }
        } catch (SQLException throwables) {
            System.err.println(throwables.getMessage());
        }finally {
            closeResultSet(resultSet);
        }
        return teamList;
    }

    public void setTeamsForUser(User user, Team team) {
        String sql = "INSERT INTO users_teams VALUES (?,?);";
        Connection connection = null;
        PreparedStatement statement = null;
        try {
            connection = getInstanceConnection();
            statement = connection.prepareStatement(sql);
            connection.setAutoCommit(false);
            statement.setInt(1, user.getId());
            statement.setInt(2, team.getId());
            statement.executeUpdate();
            connection.commit();
        } catch (SQLException throwables) {
            if (connection != null) {
                try {
                    connection.rollback();
                } catch (SQLException e) {
                    System.err.println(e.getMessage());
                }
            }
            System.err.println(throwables.getMessage());
        } finally {
            closeStatement(statement);
            closeConnection(connection);

        }
    }
    public Connection getInstanceConnection () throws SQLException {
        return DBManager.getInstance().getConnection(dbManager.url);
    }

    public void setTeamsForUser(User user, Team team1, Team team2) {
        String sql = "INSERT INTO users_teams VALUES (?,?),(?,?);";
        Connection connection = null;
        PreparedStatement statement = null;
        try {
            connection = getInstanceConnection();
            statement = connection.prepareStatement(sql);
            connection.setAutoCommit(false);
            statement.setInt(1, user.getId());
            statement.setInt(2, team1.getId());
            statement.setInt(3, user.getId());
            statement.setInt(4, team2.getId());
            statement.executeUpdate();
            connection.commit();
        } catch (SQLException throwables) {
            if (connection != null) {
                try {
                    connection.rollback();
                } catch (SQLException e) {
                    System.err.println(e.getMessage());
                }
            }
            System.err.println(throwables.getMessage());
        } finally {
            closeStatement(statement);
            closeConnectionAutocommit(connection);
        }
    }

    public void closeStatement(PreparedStatement statement) {
        if (statement != null) {
            try {
                statement.close();
            } catch (SQLException throwables) {
                System.err.println(throwables.getMessage());
            }
        }
    }

    public void closeConnection (Connection connection){
        if (connection != null) {
            try {
                connection.close();
            } catch (SQLException throwables) {
                System.err.println(throwables.getMessage());
            }
        }
    }

    public void closeConnectionAutocommit(Connection con) {
        if (con != null) {
            try {
                con.setAutoCommit(true);
                con.close();
            } catch (SQLException throwables) {
                System.err.println(throwables.getMessage());
            }
        }
    }

    public void setTeamsForUser(User user, Team team1, Team team2, Team team3) {
        String sql = "INSERT INTO users_teams VALUES (?,?),(?,?),(?,?);";
        Connection connection = null;
        PreparedStatement statement = null;
        try {
            connection = DBManager.getInstance().getConnection(dbManager.url);
            statement = connection.prepareStatement(sql);
            connection.setAutoCommit(false);
            statement.setInt(1, user.getId());
            statement.setInt(2, team1.getId());
            statement.setInt(3, user.getId());
            statement.setInt(4, team2.getId());
            statement.setInt(5, user.getId());
            statement.setInt(6, team3.getId());
            statement.executeUpdate();
            connection.commit();
        } catch (SQLException throwables) {
            if (connection != null) {
                try {
                    connection.rollback();
                } catch (SQLException e) {
                    System.err.println(e.getMessage());
                }
            }
            System.err.println(throwables.getMessage());
        } finally {
            closeStatement(statement);
            closeConnectionAutocommit(connection);
        }
    }

    public List<Team> getUserTeams(User user) {
        String sql = "SELECT users.login,teams.name FROM users JOIN users_teams ON users.id = users_teams.user_id " +
                "JOIN teams ON teams.id=users_teams.team_id WHERE users.login=?;";
        List<Team> teamList = new ArrayList<>();
        ResultSet resultSet =null;
        try (Connection connection = DBManager.getInstance().getConnection(dbManager.url);
             PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setString(1, user.getLogin());
            resultSet = statement.executeQuery();
            while (resultSet.next()) {
                Team team = Team.createTeam(resultSet.getString("name"));
                teamList.add(team);
            }
        } catch (SQLException throwables) {
            System.err.println(throwables.getMessage());
        }finally {
            closeResultSet(resultSet);
        }
        return teamList;
    }

    public void deleteTeam(Team teamA) {
        String sql = "DELETE FROM teams WHERE id=?";
        if (teamA != null) {
            try (Connection connection = DBManager.getInstance().getConnection(dbManager.url);
                 PreparedStatement statement = connection.prepareStatement(sql)) {
                statement.setInt(1, teamA.getId());
                statement.executeUpdate();
            } catch (SQLException throwables) {
                System.err.println(throwables.getMessage());
            }
        }
    }

    public void updateTeam(Team teamC) {
        String sql = "UPDATE teams SET name=? WHERE id=?;";
        try (Connection connection = DBManager.getInstance().getConnection(dbManager.url);
             PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setString(1, teamC.getName());
            statement.setInt(2, teamC.getId());
            statement.executeUpdate();
        } catch (SQLException throwables) {
            System.err.println(throwables.getMessage());
        }
    }
}
