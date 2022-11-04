package ru.yandex.practicum.filmorate.dao;

import ru.yandex.practicum.filmorate.model.Director;

import java.util.List;
import java.util.Optional;

public interface DirectorDAO {

    String FIND_BY_ID_QUERY =
            "SELECT DIRECTORS.DIRECTOR_ID,\n" +
                    "DIRECTORS.DIRECTOR_NAME\n" +
                    "FROM DIRECTORS\n" +
                    "WHERE DIRECTORS.DIRECTOR_ID = ?";

    String FIND_ALL_QUERY =
            "SELECT DIRECTORS.DIRECTOR_ID,\n" +
                    "DIRECTORS.DIRECTOR_NAME\n" +
                    "FROM DIRECTORS";

    String CREATE_QUERY = "INSERT INTO DIRECTORS (DIRECTOR_NAME) VALUES (?)";

    String UPDATE_QUERY =
            "UPDATE DIRECTORS SET DIRECTOR_NAME = ? WHERE DIRECTOR_ID = ?";

    String DELETE_QUERY =
            "DELETE FROM DIRECTORS WHERE DIRECTORS.DIRECTOR_ID = ?";

    List<Director> findAll();

    Optional<Director> findById(int id);

    Director create(Director director);

    Director update(Director director);

    void delete(int id);
}
