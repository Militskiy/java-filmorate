package ru.yandex.practicum.filmorate.dao;

import ru.yandex.practicum.filmorate.model.Event;
import ru.yandex.practicum.filmorate.model.enums.EventType;
import ru.yandex.practicum.filmorate.model.enums.Operation;

import java.util.Collection;

public interface EventDao {
    String FIND_FEED =
            "SELECT * FROM EVENTS WHERE USER_ID = ?";

    String CREATE_EVENT =
            "INSERT INTO EVENTS (USER_ID, EVENT_TYPE, OPERATION, ENTITY_ID) VALUES (?, ?, ?, ?)";

    Collection<Event> findFeed(Integer id);

    void createEvent(Integer userId, EventType eventType, Operation operation, Integer entityId);
}
