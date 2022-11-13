INSERT INTO PUBLIC.DIRECTORS (DIRECTOR_NAME)
VALUES ('Director');

INSERT INTO PUBLIC.FILMS (FILM_NAME, FILM_DESCRIPTION, RELEASE_DATE, DURATION, RATING_ID, FILM_RATE)
VALUES ('Film1', 'Test film1', '2021-11-01', 120, 1, DEFAULT);

INSERT INTO PUBLIC.FILMS (FILM_NAME, FILM_DESCRIPTION, RELEASE_DATE, DURATION, RATING_ID, FILM_RATE)
VALUES ('Film2', 'Test film2', '2020-11-01', 120, 1, DEFAULT);

INSERT INTO PUBLIC.FILMS (FILM_NAME, FILM_DESCRIPTION, RELEASE_DATE, DURATION, RATING_ID, FILM_RATE)
VALUES ('Film3', 'Test film3', '2019-11-01', 120, 1, DEFAULT);

INSERT INTO PUBLIC.FILMS (FILM_NAME, FILM_DESCRIPTION, RELEASE_DATE, DURATION, RATING_ID, FILM_RATE)
VALUES ('Film4', 'Test film4', '2019-11-01', 120, 1, DEFAULT);

INSERT INTO PUBLIC.DIRECTORS_FILMS (FILM_ID, DIRECTOR_ID)
VALUES (1, 1);

INSERT INTO PUBLIC.DIRECTORS_FILMS (FILM_ID, DIRECTOR_ID)
VALUES (2, 1);

INSERT INTO PUBLIC.DIRECTORS_FILMS (FILM_ID, DIRECTOR_ID)
VALUES (3, 1);

INSERT INTO PUBLIC.DIRECTORS_FILMS (FILM_ID, DIRECTOR_ID)
VALUES (4, 1);

INSERT INTO PUBLIC.USERS (USER_EMAIL, USER_LOGIN, USER_NAME, BIRTHDAY)
VALUES ('user1@test.com', 'user1', 'test1', '2021-11-01');

INSERT INTO PUBLIC.USERS (USER_EMAIL, USER_LOGIN, USER_NAME, BIRTHDAY)
VALUES ('user2@test.com', 'user2', 'test2', '2020-11-01');

INSERT INTO PUBLIC.USERS (USER_EMAIL, USER_LOGIN, USER_NAME, BIRTHDAY)
VALUES ('user3@test.com', 'user3', 'test3', '2019-11-01');

INSERT INTO PUBLIC.LIKES (USER_ID, FILM_ID, LIKE_RATE)
VALUES (1, 1, 10);

INSERT INTO PUBLIC.LIKES (USER_ID, FILM_ID, LIKE_RATE)
VALUES (1, 2, 9);

INSERT INTO PUBLIC.LIKES (USER_ID, FILM_ID, LIKE_RATE)
VALUES (1, 3, 3);

INSERT INTO PUBLIC.LIKES (USER_ID, FILM_ID, LIKE_RATE)
VALUES (1, 4, 3);

INSERT INTO PUBLIC.LIKES (USER_ID, FILM_ID, LIKE_RATE)
VALUES (2, 1, 8);

INSERT INTO PUBLIC.LIKES (USER_ID, FILM_ID, LIKE_RATE)
VALUES (2, 2, 9);

INSERT INTO PUBLIC.LIKES (USER_ID, FILM_ID, LIKE_RATE)
VALUES (2, 4, 2);

INSERT INTO PUBLIC.LIKES (USER_ID, FILM_ID, LIKE_RATE)
VALUES (3, 2, 4);

INSERT INTO PUBLIC.LIKES (USER_ID, FILM_ID, LIKE_RATE)
VALUES (3, 3, 10);

UPDATE FILMS
SET FILM_RATE = (SELECT COALESCE(ROUND(AVG(LIKE_RATE), 1), 4) as AVERAGE_RATE
                 FROM LIKES
                 WHERE FILM_ID = 1)
WHERE FILM_ID = 1;

UPDATE FILMS
SET FILM_RATE = (SELECT COALESCE(ROUND(AVG(LIKE_RATE), 1), 4) as AVERAGE_RATE
                 FROM LIKES
                 WHERE FILM_ID = 2)
WHERE FILM_ID = 2;

UPDATE FILMS
SET FILM_RATE = (SELECT COALESCE(ROUND(AVG(LIKE_RATE), 1), 4) as AVERAGE_RATE
                 FROM LIKES
                 WHERE FILM_ID = 3)
WHERE FILM_ID = 3;

UPDATE FILMS
SET FILM_RATE = (SELECT COALESCE(ROUND(AVG(LIKE_RATE), 1), 4) as AVERAGE_RATE
                 FROM LIKES
                 WHERE FILM_ID = 4)
WHERE FILM_ID = 4;