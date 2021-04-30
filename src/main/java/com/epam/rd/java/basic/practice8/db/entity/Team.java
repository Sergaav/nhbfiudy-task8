package com.epam.rd.java.basic.practice8.db.entity;

import java.util.Objects;

public class Team {
    private String name;

    public void setName(String name) {
        this.name = name;
    }

    public static Team createTeam(String teamB) {
        return null;
    }

    public String getName() {
        return name;
    }

    @Override
    public String toString() {
        return getName();
    }

    @Override
    public boolean equals(Object o) {
        if (!(o instanceof Team)) return false;
        Team team = (Team) o;
        return Objects.equals(name, team.name);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name);
    }
}
