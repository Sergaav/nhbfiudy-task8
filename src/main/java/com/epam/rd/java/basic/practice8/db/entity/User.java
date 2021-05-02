package com.epam.rd.java.basic.practice8.db.entity;

import java.util.Objects;

public class User {
    private String login;
    private int id;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getLogin() {
        return login;
    }

    public static User createUser(String login) {
            User user = new User();
            user.login = login;
        return user;
    }

    private User() {
    }

    @Override
    public String toString() {
        return getLogin();
    }

    @Override
    public boolean equals(Object o) {
        if (!(o instanceof User)) return false;
        User user = (User) o;
        return Objects.equals(login, user.login);
    }

    @Override
    public int hashCode() {
        return Objects.hash(login);
    }
}
