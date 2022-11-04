DROP TABLE IF EXISTS genres_films CASCADE;
DROP TABLE IF EXISTS genres CASCADE;
DROP TABLE IF EXISTS friends CASCADE;
DROP TABLE IF EXISTS likes CASCADE;
DROP TABLE IF EXISTS users CASCADE;
DROP TABLE IF EXISTS films CASCADE;
DROP TABLE IF EXISTS ratings CASCADE;
DROP TABLE IF EXISTS directors CASCADE;

CREATE TABLE IF NOT EXISTS ratings
(
    rating_id   INTEGER GENERATED BY DEFAULT AS IDENTITY PRIMARY KEY,
    rating_name varchar(10) NOT NULL
);

CREATE TABLE IF NOT EXISTS directors (
    director_id INTEGER GENERATED BY DEFAULT AS IDENTITY PRIMARY KEY,
    director_name varchar(50)
);

CREATE TABLE IF NOT EXISTS films
(
    film_id          INTEGER GENERATED BY DEFAULT AS IDENTITY PRIMARY KEY,
    film_name        varchar(50) NOT NULL,
    film_description varchar(200),
    release_date     date        NOT NULL,
    duration         integer     NOT NULL,
    rating_id        INTEGER REFERENCES ratings (rating_id) ON DELETE CASCADE,
    director_id      INTEGER REFERENCES directors (director_id) ON DELETE CASCADE
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