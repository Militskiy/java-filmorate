DROP TABLE IF EXISTS genres_films, genres, friends, likes, users, films,
    ratings, reviews, users_reviews, directors_films, directors CASCADE;

CREATE TABLE IF NOT EXISTS ratings
(
    rating_id   INTEGER GENERATED BY DEFAULT AS IDENTITY PRIMARY KEY,
    rating_name varchar(10) NOT NULL
);

CREATE TABLE IF NOT EXISTS films
(
    film_id          INTEGER GENERATED BY DEFAULT AS IDENTITY PRIMARY KEY,
    film_name        varchar(50) NOT NULL,
    film_description varchar(200),
    release_date     date        NOT NULL,
    duration         integer     NOT NULL,
    rating_id        INTEGER REFERENCES ratings (rating_id) ON DELETE CASCADE
);

CREATE TABLE IF NOT EXISTS users
(
    user_id    INTEGER GENERATED BY DEFAULT AS IDENTITY PRIMARY KEY,
    user_email varchar(50) NOT NULL,
    user_login varchar(50) NOT NULL,
    user_name  varchar(50),
    birthday   date        NOT NULL
);

CREATE TABLE IF NOT EXISTS likes
(
    user_id INTEGER REFERENCES users (user_id) ON DELETE CASCADE,
    film_id INTEGER REFERENCES films (film_id) ON DELETE CASCADE,
    PRIMARY KEY (user_id, film_id)
);

CREATE TABLE IF NOT EXISTS friends
(
    link_id   INTEGER GENERATED BY DEFAULT AS IDENTITY PRIMARY KEY,
    user_id   INTEGER REFERENCES users (user_id) ON DELETE CASCADE,
    friend_id INTEGER REFERENCES users (user_id) ON DELETE CASCADE,
    confirmed BOOLEAN NOT NULL DEFAULT FALSE
    CONSTRAINT validation CHECK (user_id <> friends.friend_id)
);

CREATE TABLE IF NOT EXISTS genres
(
    genre_id   INTEGER GENERATED BY DEFAULT AS IDENTITY PRIMARY KEY,
    genre_name varchar(20) NOT NULL,
    CONSTRAINT genres_naming CHECK (genre_name <> '')
);

CREATE TABLE IF NOT EXISTS genres_films
(
    film_id  INTEGER REFERENCES films (film_id) ON DELETE CASCADE,
    genre_id INTEGER REFERENCES genres (genre_id) ON DELETE CASCADE,
    PRIMARY KEY (film_id, genre_id)
);

CREATE TABLE IF NOT EXISTS directors (
    director_id INTEGER GENERATED BY DEFAULT AS IDENTITY PRIMARY KEY,
    director_name varchar(50)
);

CREATE TABLE IF NOT EXISTS directors_films (
    film_id INTEGER REFERENCES films(film_id) ON DELETE CASCADE,
    director_id INTEGER REFERENCES directors(director_id) ON DELETE CASCADE,
    PRIMARY KEY (film_id, director_id)
);

INSERT INTO PUBLIC.RATINGS (RATING_NAME)
VALUES ('G'),
       ('PG'),
       ('PG-13'),
       ('R'),
       ('NC-17');

INSERT INTO PUBLIC.GENRES (GENRE_NAME)
VALUES ('Комедия'),
       ('Драма'),
       ('Мультфильм'),
       ('Триллер'),
       ('Документальный'),
       ('Боевик');

INSERT INTO PUBLIC.USERS (USER_EMAIL, USER_LOGIN, USER_NAME, BIRTHDAY)
VALUES ('email1@test.com', 'login1', 'name1', '2022-10-01');

INSERT INTO PUBLIC.USERS (USER_EMAIL, USER_LOGIN, USER_NAME, BIRTHDAY)
VALUES ('email2@test.com', 'login2', 'name2', '2022-10-01');

INSERT INTO PUBLIC.USERS (USER_EMAIL, USER_LOGIN, USER_NAME, BIRTHDAY)
VALUES ('email3@test.com', 'login3', 'name3', '2022-10-01');

INSERT INTO PUBLIC.DIRECTORS (DIRECTOR_NAME)
VALUES ('Director 1');

INSERT INTO PUBLIC.FILMS (FILM_NAME, FILM_DESCRIPTION, RELEASE_DATE, DURATION, RATING_ID)
VALUES ('film1', 'description1', '2022-10-03', 120, 1);

INSERT INTO PUBLIC.DIRECTORS_FILMS (FILM_ID, DIRECTOR_ID)
VALUES (1, 1);

INSERT INTO PUBLIC.FILMS (FILM_NAME, FILM_DESCRIPTION, RELEASE_DATE, DURATION, RATING_ID)
VALUES ('film2', 'description2', '2022-10-02', 120, 2);

INSERT INTO PUBLIC.DIRECTORS_FILMS (FILM_ID, DIRECTOR_ID)
VALUES (2, 1);

INSERT INTO PUBLIC.FILMS (FILM_NAME, FILM_DESCRIPTION, RELEASE_DATE, DURATION, RATING_ID)
VALUES ('film3', 'description3', '2022-10-01', 120, 3);

INSERT INTO PUBLIC.DIRECTORS_FILMS (FILM_ID, DIRECTOR_ID)
VALUES (3, 1);

INSERT INTO PUBLIC.LIKES (USER_ID, FILM_ID)
VALUES (1, 3);

INSERT INTO PUBLIC.LIKES (USER_ID, FILM_ID)
VALUES (2, 2);

INSERT INTO PUBLIC.LIKES (USER_ID, FILM_ID)
VALUES (2, 3);

INSERT INTO PUBLIC.LIKES (USER_ID, FILM_ID)
VALUES (3, 1);

INSERT INTO PUBLIC.LIKES (USER_ID, FILM_ID)
VALUES (3, 2);

INSERT INTO PUBLIC.LIKES (USER_ID, FILM_ID)
VALUES (3, 3);

INSERT INTO PUBLIC.GENRES_FILMS (FILM_ID, GENRE_ID)
VALUES (1, 1);

INSERT INTO PUBLIC.GENRES_FILMS (FILM_ID, GENRE_ID)
VALUES (1, 2);

INSERT INTO PUBLIC.GENRES_FILMS (FILM_ID, GENRE_ID)
VALUES (1, 3);

INSERT INTO PUBLIC.GENRES_FILMS (FILM_ID, GENRE_ID)
VALUES (1, 4);

INSERT INTO PUBLIC.GENRES_FILMS (FILM_ID, GENRE_ID)
VALUES (1, 5);

INSERT INTO PUBLIC.GENRES_FILMS (FILM_ID, GENRE_ID)
VALUES (1, 6);

INSERT INTO PUBLIC.GENRES_FILMS (FILM_ID, GENRE_ID)
VALUES (2, 1);

INSERT INTO PUBLIC.GENRES_FILMS (FILM_ID, GENRE_ID)
VALUES (3, 1);

INSERT INTO PUBLIC.GENRES_FILMS (FILM_ID, GENRE_ID)
VALUES (3, 2);

INSERT INTO PUBLIC.FRIENDS (USER_ID, FRIEND_ID, CONFIRMED)
VALUES (1, 2, TRUE);

INSERT INTO PUBLIC.FRIENDS (USER_ID, FRIEND_ID, CONFIRMED)
VALUES (2, 1, TRUE);

INSERT INTO PUBLIC.FRIENDS (USER_ID, FRIEND_ID, CONFIRMED)
VALUES (1, 3, DEFAULT);