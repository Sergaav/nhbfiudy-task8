package com.epam.rd.java.basic.practice8;

import com.epam.rd.java.basic.practice8.db.DBManager;
import com.epam.rd.java.basic.practice8.db.entity.Team;
import com.epam.rd.java.basic.practice8.db.entity.User;
import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;
import org.mockito.Spy;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Properties;

public class Part3StudentTest {
    private static final String JDBC_DRIVER = "org.h2.Driver";
    private static final String DB_URL = "jdbc:h2:~/test";
    private static final String URL_CONNECTION = "jdbc:h2:~/test;user=youruser;password=yourpassword;";
    private static final String USER = "youruser";
    private static final String PASS = "yourpassword";

    @Spy //actually this annotation is not necessary here
    private static DBManager dbManager;

    @BeforeClass
    public static void beforeTest() throws SQLException, ClassNotFoundException {
        Class.forName(JDBC_DRIVER);

        try (OutputStream output = new FileOutputStream("app.properties")) {
            Properties prop = new Properties();
            prop.setProperty("connection.url", URL_CONNECTION);
            prop.store(output, null);
        } catch (IOException io) {
            io.printStackTrace();
        }

        dbManager = DBManager.getInstance();

        try (Connection con = DriverManager.getConnection(DB_URL, USER, PASS);
             Statement statement = con.createStatement()) {
            String sql = "CREATE TABLE IF NOT EXISTS users (\n" +
                    "  id INTEGER(11) NOT NULL AUTO_INCREMENT,\n" +
                    " login VARCHAR(20) NOT NULL, \n" +
                    "  PRIMARY KEY (id));";

            statement.executeUpdate(sql);
        }
    }

    @Test
    public void shouldReturnListOfUsers() {
        dbManager = DBManager.getInstance();
        DBManager.insertUser(User.createUser("ivanov"));
        User userPetrov = DBManager.getUser("petrov");
        User userObama = DBManager.getUser("obama");
        User userIvanov = DBManager.getUser("ivanov");
        DBManager.insertTeam(Team.createTeam("teamA"));
        Team teamA = DBManager.getTeam("teamA");
        Team teamB = DBManager.getTeam("teamB");
        Team teamC = DBManager.getTeam("teamC");
        dbManager.setTeamsForUser(userIvanov, teamA);
        dbManager.setTeamsForUser(userPetrov, teamA, teamB);
        dbManager.setTeamsForUser(userObama, teamA, teamB, teamC);
        Assert.assertEquals(3, dbManager.getUserTeams(userObama).size());
        Assert.assertEquals(2, dbManager.getUserTeams(userPetrov).size());
        Assert.assertEquals(1, dbManager.getUserTeams(userIvanov).size());
    }
}