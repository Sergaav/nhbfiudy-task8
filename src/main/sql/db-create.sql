CREATE DATABASE [IF NOT EXISTS] practice8;

USE paractice8;

CREATE TABLE users (

                       id INT PRIMARY KEY GENERATED ALWAYS AS IDENTITY,

                       login VARCHAR(10) UNIQUE

);

CREATE TABLE teams (

                       id INT PRIMARY KEY GENERATED ALWAYS AS IDENTITY,

                       name VARCHAR(10)

);

CREATE TABLE users_teams (

                             user_id INT REFERENCES users(id) ON DELETE CASCADE,

                             team_id INT REFERENCES teams(id) ON DELETE CASCADE,

                             UNIQUE (user_id, team_id)

);

INSERT INTO users VALUES (id,'ivanov');

INSERT INTO teams VALUES (id,'TeamA');

