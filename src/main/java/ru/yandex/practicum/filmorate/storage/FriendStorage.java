package ru.yandex.practicum.filmorate.storage;

import ru.yandex.practicum.filmorate.model.Friend;
import ru.yandex.practicum.filmorate.model.User;

import java.util.Collection;

public interface FriendStorage extends Storage<Friend> {

    String FIND_ALL_QUERY =
            "SELECT * " +
                    "FROM FRIENDS";

    String FIND_FRIENDS_QUERY =
            "SELECT *\n" +
                    "FROM USERS\n" +
                    "WHERE USER_ID = ?\n" +
                    "OR USER_ID IN (\n" +
                    "    SELECT FRIEND_ID\n" +
                    "    FROM FRIENDS\n" +
                    "    WHERE USER_ID = ?\n" +
                    "    );";

    String FIND_COMMON_FRIENDS_QUERY =
            "SELECT * " +
                    "FROM USERS " +
                    "WHERE USER_ID = ? OR USER_ID = ? OR USER_ID IN " +
                    "(SELECT FRIEND_ID " +
                    "FROM FRIENDS " +
                    "WHERE USER_ID = ? OR USER_ID = ? " +
                    "GROUP BY FRIEND_ID HAVING COUNT(FRIEND_ID) > 1)";

    String DELETE_FRIEND_QUERY =
            "DELETE " +
                    "FROM FRIENDS " +
                    "WHERE USER_ID = ? AND FRIEND_ID = ?";

    String CREATE_FRIEND_QUERY =
            "INSERT INTO FRIENDS (USER_ID, FRIEND_ID) SELECT ?, ?\n" +
                    "WHERE NOT EXISTS(SELECT USER_ID FROM FRIENDS WHERE USER_ID = ? AND FRIEND_ID = ?);\n" +
                    "UPDATE FRIENDS\n" +
                    "SET CONFIRMED = CASE\n" +
                    "    WHEN (SELECT COUNT(CONFIRMED)\n" +
                    "          FROM FRIENDS\n" +
                    "          WHERE USER_ID = ? AND FRIEND_ID = ? \n" +
                    "             OR USER_ID = ? AND FRIEND_ID = ?) = 2 THEN TRUE ELSE CONFIRMED END\n" +
                    "WHERE USER_ID = ? AND FRIEND_ID = ? OR USER_ID = ? AND FRIEND_ID = ?";

    boolean delete(Integer userId, Integer friendId);

    Collection<User> findFriends(Integer userId);

    Collection<User> findCommonFriends(Integer userId, Integer otherId);

    void createFriend(Integer userId, Integer friendId);
}
