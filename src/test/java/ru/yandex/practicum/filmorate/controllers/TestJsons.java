package ru.yandex.practicum.filmorate.controllers;

public interface TestJsons {
    String FRIEND =
            "[{\"id\":1,\"email\":\"email1@test.com\",\"login\":\"login1\",\"name\":\"name1\"," +
                    "\"birthday\":\"2022-10-01\",\"friends\":[{\"id\":3,\"email\":\"email3@test.com\"," +
                    "\"login\":\"login3\",\"name\":\"name3\",\"birthday\":\"2022-10-01\",\"friends\":[]}," +
                    "{\"id\":2,\"email\":\"email2@test.com\",\"login\":\"login2\",\"name\":\"name2\"," +
                    "\"birthday\":\"2022-10-01\",\"friends\":[]}]}]";
     String MPA =
            "[{\"id\":1,\"name\":\"G\"}," +
                    "{\"id\":2,\"name\":\"PG\"}," +
                    "{\"id\":3,\"name\":\"PG-13\"}," +
                    "{\"id\":4,\"name\":\"R\"}," +
                    "{\"id\":5,\"name\":\"NC-17\"}]";

    String GENRES =
            "[{\"id\":1,\"name\":\"Комедия\"}," +
                    "{\"id\":2,\"name\":\"Драма\"}," +
                    "{\"id\":3,\"name\":\"Мультфильм\"}," +
                    "{\"id\":4,\"name\":\"Триллер\"}," +
                    "{\"id\":5,\"name\":\"Документальный\"}," +
                    "{\"id\":6,\"name\":\"Боевик\"}]";

    String POPULAR =
            "[{\"id\":3,\"name\":\"film3\",\"description\":\"description3\",\"releaseDate\":\"2022-10-01\"," +
                    "\"duration\":120,\"mpa\":{\"id\":3,\"name\":\"PG-13\"},\"genres\":[{\"id\":1,\"name\":\"Комедия\"}" +
                    ",{\"id\":2,\"name\":\"Драма\"}],\"userLikes\":[{\"id\":3,\"email\":\"email3@test.com\"," +
                    "\"login\":\"login3\",\"name\":\"name3\",\"birthday\":\"2022-10-01\",\"friends\":[]}," +
                    "{\"id\":2,\"email\":\"email2@test.com\",\"login\":\"login2\",\"name\":\"name2\"," +
                    "\"birthday\":\"2022-10-01\",\"friends\":[]},{\"id\":1,\"email\":\"email1@test.com\"," +
                    "\"login\":\"login1\",\"name\":\"name1\",\"birthday\":\"2022-10-01\",\"friends\":[]}]}," +
                    "{\"id\":2,\"name\":\"film2\",\"description\":\"description2\",\"releaseDate\":\"2022-10-02\"," +
                    "\"duration\":120,\"mpa\":{\"id\":2,\"name\":\"PG\"},\"genres\":[{\"id\":1,\"name\":\"Комедия\"}]," +
                    "\"userLikes\":[{\"id\":3,\"email\":\"email3@test.com\",\"login\":\"login3\",\"name\":\"name3\"," +
                    "\"birthday\":\"2022-10-01\",\"friends\":[]},{\"id\":2,\"email\":\"email2@test.com\"," +
                    "\"login\":\"login2\",\"name\":\"name2\",\"birthday\":\"2022-10-01\",\"friends\":[]}]}," +
                    "{\"id\":1,\"name\":\"film1\",\"description\":\"description1\",\"releaseDate\":\"2022-10-03\"," +
                    "\"duration\":120,\"mpa\":{\"id\":1,\"name\":\"G\"},\"genres\":[{\"id\":1,\"name\":\"Комедия\"}," +
                    "{\"id\":2,\"name\":\"Драма\"},{\"id\":3,\"name\":\"Мультфильм\"},{\"id\":4,\"name\":\"Триллер\"}," +
                    "{\"id\":5,\"name\":\"Документальный\"},{\"id\":6,\"name\":\"Боевик\"}]," +
                    "\"userLikes\":[{\"id\":3,\"email\":\"email3@test.com\",\"login\":\"login3\",\"name\":\"name3\"," +
                    "\"birthday\":\"2022-10-01\",\"friends\":[]}]}]";

    String USERS =
            "[{\"id\":1,\"email\":\"email1@test.com\",\"login\":\"login1\",\"name\":\"name1\"," +
                    "\"birthday\":\"2022-10-01\",\"friends\":[{\"id\":3,\"email\":\"email3@test.com\"," +
                    "\"login\":\"login3\",\"name\":\"name3\",\"birthday\":\"2022-10-01\",\"friends\":[]}," +
                    "{\"id\":2,\"email\":\"email2@test.com\",\"login\":\"login2\",\"name\":\"name2\"," +
                    "\"birthday\":\"2022-10-01\",\"friends\":[]}]},{\"id\":2,\"email\":\"email2@test.com\"," +
                    "\"login\":\"login2\",\"name\":\"name2\",\"birthday\":\"2022-10-01\"," +
                    "\"friends\":[{\"id\":1,\"email\":\"email1@test.com\",\"login\":\"login1\"," +
                    "\"name\":\"name1\",\"birthday\":\"2022-10-01\",\"friends\":[]}]},{\"id\":3," +
                    "\"email\":\"email3@test.com\",\"login\":\"login3\",\"name\":\"name3\"," +
                    "\"birthday\":\"2022-10-01\",\"friends\":[]}]";
   String TEST_FILM_1_WITH_LIKE =
           "{\"id\":1,\"name\":\"film1\",\"description\":\"description1\",\"releaseDate\":\"2022-10-03\"," +
                   "\"duration\":120,\"mpa\":{\"id\":1,\"name\":\"G\"},\"genres\":[{\"id\":1,\"name\":\"Комедия\"}," +
                   "{\"id\":2,\"name\":\"Драма\"},{\"id\":3,\"name\":\"Мультфильм\"},{\"id\":4,\"name\":\"Триллер\"}," +
                   "{\"id\":5,\"name\":\"Документальный\"},{\"id\":6,\"name\":\"Боевик\"}],\"userLikes\":[{\"id\":3," +
                   "\"email\":\"email3@test.com\",\"login\":\"login3\",\"name\":\"name3\",\"birthday\":\"2022-10-01\"," +
                   "\"friends\":[]},{\"id\":1,\"email\":\"email1@test.com\",\"login\":\"login1\",\"name\":\"name1\"," +
                   "\"birthday\":\"2022-10-01\",\"friends\":[]}],\"directors\":[{\"id\":1,\"name\":\"Director 1\"}]}";

   String TEST_FILM_1_REMOVED_LIKE =
           "{\"id\":1,\"name\":\"film1\",\"description\":\"description1\",\"releaseDate\":\"2022-10-03\"," +
                   "\"duration\":120,\"mpa\":{\"id\":1,\"name\":\"G\"},\"genres\":[{\"id\":1,\"name\":\"Комедия\"}," +
                   "{\"id\":2,\"name\":\"Драма\"},{\"id\":3,\"name\":\"Мультфильм\"},{\"id\":4,\"name\":\"Триллер\"}," +
                   "{\"id\":5,\"name\":\"Документальный\"},{\"id\":6,\"name\":\"Боевик\"}],\"userLikes\":[]," +
                   "\"directors\":[{\"id\":1,\"name\":\"Director 1\"}]}";

   String UPDATED_TEST_FILM =
           "{\"id\":2,\"name\":\"updated\",\"description\":\"description\",\"releaseDate\":\"1895-12-28\"," +
                   "\"duration\":1,\"mpa\":{\"id\":1,\"name\":\"G\"},\"genres\":[]," +
                   "\"userLikes\":[{\"id\":3,\"email\":\"email3@test.com\",\"login\":\"login3\",\"name\":\"name3\"," +
                   "\"birthday\":\"2022-10-01\",\"friends\":[]},{\"id\":2,\"email\":\"email2@test.com\"," +
                   "\"login\":\"login2\",\"name\":\"name2\",\"birthday\":\"2022-10-01\",\"friends\":[]}]," +
                   "\"directors\":[{\"id\":1,\"name\":\"Director 1\"}]}";

   String FILMS_BY_YEAR =
           "[{\"id\":3,\"name\":\"film3\",\"description\":\"description3\",\"releaseDate\":\"2022-10-01\"," +
                   "\"duration\":120,\"mpa\":{\"id\":3,\"name\":\"PG-13\"},\"genres\":[{\"id\":1,\"name\":\"Комедия\"}," +
                   "{\"id\":2,\"name\":\"Драма\"}],\"userLikes\":[{\"id\":3,\"email\":\"email3@test.com\"," +
                   "\"login\":\"login3\",\"name\":\"name3\",\"birthday\":\"2022-10-01\",\"friends\":[]}," +
                   "{\"id\":2,\"email\":\"email2@test.com\",\"login\":\"login2\",\"name\":\"name2\"," +
                   "\"birthday\":\"2022-10-01\",\"friends\":[]},{\"id\":1,\"email\":\"email1@test.com\"," +
                   "\"login\":\"login1\",\"name\":\"name1\",\"birthday\":\"2022-10-01\",\"friends\":[]}]," +
                   "\"directors\":[{\"id\":1,\"name\":\"Director 1\"}]},{\"id\":2,\"name\":\"film2\"," +
                   "\"description\":\"description2\",\"releaseDate\":\"2022-10-02\",\"duration\":120," +
                   "\"mpa\":{\"id\":2,\"name\":\"PG\"},\"genres\":[{\"id\":1,\"name\":\"Комедия\"}]," +
                   "\"userLikes\":[{\"id\":3,\"email\":\"email3@test.com\",\"login\":\"login3\"," +
                   "\"name\":\"name3\",\"birthday\":\"2022-10-01\",\"friends\":[]},{\"id\":2," +
                   "\"email\":\"email2@test.com\",\"login\":\"login2\",\"name\":\"name2\"," +
                   "\"birthday\":\"2022-10-01\",\"friends\":[]}],\"directors\":[{\"id\":1," +
                   "\"name\":\"Director 1\"}]},{\"id\":1,\"name\":\"film1\",\"description\":\"description1\"," +
                   "\"releaseDate\":\"2022-10-03\",\"duration\":120,\"mpa\":{\"id\":1,\"name\":\"G\"}," +
                   "\"genres\":[{\"id\":1,\"name\":\"Комедия\"},{\"id\":2,\"name\":\"Драма\"},{\"id\":3," +
                   "\"name\":\"Мультфильм\"},{\"id\":4,\"name\":\"Триллер\"},{\"id\":5,\"name\":\"Документальный\"}," +
                   "{\"id\":6,\"name\":\"Боевик\"}],\"userLikes\":[{\"id\":3,\"email\":\"email3@test.com\"," +
                   "\"login\":\"login3\",\"name\":\"name3\",\"birthday\":\"2022-10-01\",\"friends\":[]}]," +
                   "\"directors\":[{\"id\":1,\"name\":\"Director 1\"}]}]";

   String FILMS_BY_LIKES =
           "[{\"id\":3,\"name\":\"film3\",\"description\":\"description3\",\"releaseDate\":\"2022-10-01\"," +
                   "\"duration\":120,\"mpa\":{\"id\":3,\"name\":\"PG-13\"},\"genres\":[{\"id\":1," +
                   "\"name\":\"Комедия\"},{\"id\":2,\"name\":\"Драма\"}],\"userLikes\":[{\"id\":3," +
                   "\"email\":\"email3@test.com\",\"login\":\"login3\",\"name\":\"name3\"," +
                   "\"birthday\":\"2022-10-01\",\"friends\":[]},{\"id\":2,\"email\":\"email2@test.com\"," +
                   "\"login\":\"login2\",\"name\":\"name2\",\"birthday\":\"2022-10-01\",\"friends\":[]}," +
                   "{\"id\":1,\"email\":\"email1@test.com\",\"login\":\"login1\",\"name\":\"name1\"," +
                   "\"birthday\":\"2022-10-01\",\"friends\":[]}],\"directors\":[{\"id\":1," +
                   "\"name\":\"Director 1\"}]},{\"id\":2,\"name\":\"film2\",\"description\":\"description2\"," +
                   "\"releaseDate\":\"2022-10-02\",\"duration\":120,\"mpa\":{\"id\":2,\"name\":\"PG\"}," +
                   "\"genres\":[{\"id\":1,\"name\":\"Комедия\"}],\"userLikes\":[{\"id\":3," +
                   "\"email\":\"email3@test.com\",\"login\":\"login3\",\"name\":\"name3\"," +
                   "\"birthday\":\"2022-10-01\",\"friends\":[]},{\"id\":2,\"email\":\"email2@test.com\"," +
                   "\"login\":\"login2\",\"name\":\"name2\",\"birthday\":\"2022-10-01\",\"friends\":[]}]," +
                   "\"directors\":[{\"id\":1,\"name\":\"Director 1\"}]},{\"id\":1,\"name\":\"film1\"," +
                   "\"description\":\"description1\",\"releaseDate\":\"2022-10-03\",\"duration\":120," +
                   "\"mpa\":{\"id\":1,\"name\":\"G\"},\"genres\":[{\"id\":1,\"name\":\"Комедия\"}," +
                   "{\"id\":2,\"name\":\"Драма\"},{\"id\":3,\"name\":\"Мультфильм\"},{\"id\":4," +
                   "\"name\":\"Триллер\"},{\"id\":5,\"name\":\"Документальный\"},{\"id\":6,\"name\":\"Боевик\"}]," +
                   "\"userLikes\":[{\"id\":3,\"email\":\"email3@test.com\",\"login\":\"login3\",\"name\":\"name3\"," +
                   "\"birthday\":\"2022-10-01\",\"friends\":[]}],\"directors\":[{\"id\":1,\"name\":\"Director 1\"}]}]";
   String FIND_ALL_REVIEWS =
           "[" +
                   "{" +
                   "\"reviewId\":2," +
                   "\"content\":\"Very bad review\"," +
                   "\"userId\":2," +
                   "\"filmId\":1," +
                   "\"useful\":99," +
                   "\"isPositive\":false" +
                   "}," +
                   "{" +
                   "\"reviewId\":1," +
                   "\"content\":\"Very good review\"," +
                   "\"userId\":1," +
                   "\"filmId\":1," +
                   "\"useful\":0," +
                   "\"isPositive\":true" +
                   "}," +
                   "{" +
                   "\"reviewId\":3," +
                   "\"content\":\"Very good review\"," +
                   "\"userId\":3," +
                   "\"filmId\":1," +
                   "\"useful\":0," +
                   "\"isPositive\":true" +
                   "}" +
                   "]";

   String FIND_ALL_REVIEWS_AFTER_ADD_LIKE =
           "[" +
                   "{" +
                   "\"reviewId\":2," +
                   "\"content\":\"Very bad review\"," +
                   "\"userId\":2," +
                   "\"filmId\":1," +
                   "\"useful\":99," +
                   "\"isPositive\":false" +
                   "}," +
                   "{" +
                   "\"reviewId\":3," +
                   "\"content\":\"Very good review\"," +
                   "\"userId\":3," +
                   "\"filmId\":1," +
                   "\"useful\":1," +
                   "\"isPositive\":true" +
                   "}," +
                   "{" +
                   "\"reviewId\":1," +
                   "\"content\":\"Very good review\"," +
                   "\"userId\":1," +
                   "\"filmId\":1," +
                   "\"useful\":0," +
                   "\"isPositive\":true" +
                   "}" +
                   "]";

   String RECOMMENDATIONS =
           "[{\"id\":1,\"name\":\"Film1\",\"description\":\"Test film1\",\"releaseDate\":\"2021-11-01\"," +
                   "\"duration\":120,\"mpa\":{\"id\":1,\"name\":\"G\"},\"rate\":9.0,\"genres\":[]," +
                   "\"userLikes\":[{\"id\":1,\"email\":\"user1@test.com\",\"login\":\"user1\",\"name\":\"test1\"," +
                   "\"birthday\":\"2021-11-01\",\"friends\":[]},{\"id\":2,\"email\":\"user2@test.com\"," +
                   "\"login\":\"user2\",\"name\":\"test2\",\"birthday\":\"2020-11-01\",\"friends\":[]}]," +
                   "\"directors\":[{\"id\":1,\"name\":\"Director\"}]}]";
}
