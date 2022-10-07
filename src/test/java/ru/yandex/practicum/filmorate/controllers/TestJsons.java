package ru.yandex.practicum.filmorate.controllers;

public interface TestJsons {
    String FRIEND =
            "[{\"id\":1," +
                    "\"email\":\"email1@test.com\"," +
                    "\"login\":\"login1\"," +
                    "\"name\":\"name1\"," +
                    "\"birthday\":\"2022-10-01\"}]";
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
            "[\n" +
                    "    {\n" +
                    "        \"id\": 3,\n" +
                    "        \"name\": \"film3\",\n" +
                    "        \"description\": \"description3\",\n" +
                    "        \"releaseDate\": \"2022-10-01\",\n" +
                    "        \"duration\": 120,\n" +
                    "        \"mpa\": {\n" +
                    "            \"id\": 3,\n" +
                    "            \"name\": \"PG-13\"\n" +
                    "        },\n" +
                    "        \"genres\": [\n" +
                    "            {\n" +
                    "                \"id\": 1,\n" +
                    "                \"name\": \"Комедия\"\n" +
                    "            },\n" +
                    "            {\n" +
                    "                \"id\": 2,\n" +
                    "                \"name\": \"Драма\"\n" +
                    "            }\n" +
                    "        ],\n" +
                    "        \"userLikes\": [\n" +
                    "            1,\n" +
                    "            2,\n" +
                    "            3\n" +
                    "        ]\n" +
                    "    },\n" +
                    "    {\n" +
                    "        \"id\": 2,\n" +
                    "        \"name\": \"film2\",\n" +
                    "        \"description\": \"description2\",\n" +
                    "        \"releaseDate\": \"2022-10-01\",\n" +
                    "        \"duration\": 120,\n" +
                    "        \"mpa\": {\n" +
                    "            \"id\": 2,\n" +
                    "            \"name\": \"PG\"\n" +
                    "        },\n" +
                    "        \"genres\": [\n" +
                    "            {\n" +
                    "                \"id\": 1,\n" +
                    "                \"name\": \"Комедия\"\n" +
                    "            }\n" +
                    "        ],\n" +
                    "        \"userLikes\": [\n" +
                    "            2,\n" +
                    "            3\n" +
                    "        ]\n" +
                    "    },\n" +
                    "    {\n" +
                    "        \"id\": 1,\n" +
                    "        \"name\": \"film1\",\n" +
                    "        \"description\": \"description1\",\n" +
                    "        \"releaseDate\": \"2022-10-01\",\n" +
                    "        \"duration\": 120,\n" +
                    "        \"mpa\": {\n" +
                    "            \"id\": 1,\n" +
                    "            \"name\": \"G\"\n" +
                    "        },\n" +
                    "        \"genres\": [\n" +
                    "            {\n" +
                    "                \"id\": 1,\n" +
                    "                \"name\": \"Комедия\"\n" +
                    "            },\n" +
                    "            {\n" +
                    "                \"id\": 2,\n" +
                    "                \"name\": \"Драма\"\n" +
                    "            },\n" +
                    "            {\n" +
                    "                \"id\": 3,\n" +
                    "                \"name\": \"Мультфильм\"\n" +
                    "            },\n" +
                    "            {\n" +
                    "                \"id\": 4,\n" +
                    "                \"name\": \"Триллер\"\n" +
                    "            },\n" +
                    "            {\n" +
                    "                \"id\": 5,\n" +
                    "                \"name\": \"Документальный\"\n" +
                    "            },\n" +
                    "            {\n" +
                    "                \"id\": 6,\n" +
                    "                \"name\": \"Боевик\"\n" +
                    "            }\n" +
                    "        ],\n" +
                    "        \"userLikes\": [\n" +
                    "            3\n" +
                    "        ]\n" +
                    "    }\n" +
                    "]";
}
