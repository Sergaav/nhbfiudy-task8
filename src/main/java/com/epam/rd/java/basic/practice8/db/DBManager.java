package com.epam.rd.java.basic.practice8.db;
import com.epam.rd.java.basic.practice8.db.entity.Team;
import com.epam.rd.java.basic.practice8.db.entity.User;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.List;

public class DBManager {

    private static DBManager dbManager;


    public static User getUser(String login) {
        return null;
    }

    public static Team getTeam(String name) {
        return null;
    }

    private DBManager() {
    }

    public static DBManager getInstance() {

        return null;
    }

    public Connection getConnection(String connectionUrl) throws SQLException {

        return null;
    }



    public static void insertUser (User user){

    }

    public static void insertTeam (Team team){

    }

    public List<User> findAllUsers() {
        return null;
    }

    public List<Team> findAllTeams() {
        return null;
    }

    public void setTeamsForUser(User user, Team team) {

    }
    public void setTeamsForUser(User user, Team team1,Team team2) {

    }
    public void setTeamsForUser(User user, Team team1,Team team2,Team team3) {

    }

    public List<Team> getUserTeams(User user) {
        return null;
    }

    public void deleteTeam(Team teamA) {
    }

    public void updateTeam(Team teamC) {
    }
}
