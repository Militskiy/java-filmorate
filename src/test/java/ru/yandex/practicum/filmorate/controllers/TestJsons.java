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

}
