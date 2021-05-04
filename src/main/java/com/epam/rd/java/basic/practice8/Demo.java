package com.epam.rd.java.basic.practice8;

import java.util.List;

import com.epam.rd.java.basic.practice8.db.DBManager;

import com.epam.rd.java.basic.practice8.db.entity.Team;

import com.epam.rd.java.basic.practice8.db.entity.User;

public class Demo {

    private static void printList(List<?> list) {
        System.out.println(list);
    }

    public static void main(String[] args) {

        DBManager dbManager = DBManager.getInstance();

        // Part 1

        DBManager.insertUser(User.createUser("petrov"));
        DBManager.insertUser(User.createUser("obama"));

        printList(dbManager.findAllUsers());

        // users  ==> [ivanov, petrov, obama]

        System.out.println("===========================");

        // Part 2

        DBManager.insertTeam(Team.createTeam("teamB"));
        DBManager.insertTeam(Team.createTeam("teamC"));

        printList(dbManager.findAllTeams());

        // teams ==> [teamA, teamB, teamC]
        System.out.println("++++++++++++++++++++++++++++");

        // Part 3

        User userPetrov = DBManager.getUser("petrov");

        User userIvanov = DBManager.getUser("ivanov");
        System.out.println(userIvanov);

        User userObama = DBManager.getUser("obama");

       Team teamA = DBManager.getTeam("teamA");

       Team teamB = DBManager.getTeam("teamB");

        Team teamC = DBManager.getTeam("teamC");

   //      method setTeamsForUser must implement transaction!
        dbManager.setTeamsForUser(userIvanov, teamA);
        dbManager.setTeamsForUser(userPetrov, teamA, teamB);
        dbManager.setTeamsForUser(userObama, teamA, teamB, teamC);

        for (User user : dbManager.findAllUsers()) {

            printList(dbManager.getUserTeams(user));
            System.out.println("~~~~~");

        }

        // teamA

        // teamA teamB

        // teamA teamB teamC
        System.out.println("_____________________________");

// Part 4

// on delete cascade!

        dbManager.deleteTeam(teamA);

 //Part 5

        teamC.setName("teamX");

        dbManager.updateTeam(teamC);

        printList(dbManager.findAllTeams());

        // teams ==> [teamB, teamX]

    }


}

