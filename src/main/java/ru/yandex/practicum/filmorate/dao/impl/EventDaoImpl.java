package ru.yandex.practicum.filmorate.dao.impl;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;
import ru.yandex.practicum.filmorate.dao.EventDao;
import ru.yandex.practicum.filmorate.dao.UserDao;
import ru.yandex.practicum.filmorate.model.Event;
import ru.yandex.practicum.filmorate.model.enums.EventType;
import ru.yandex.practicum.filmorate.model.enums.Operation;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Collection;

@Repository
@AllArgsConstructor
@Slf4j
public class EventDaoImpl implements EventDao {

    private final JdbcTemplate jdbcTemplate;
    private final UserDao userStorage;

    @Override
    public Collection<Event> findFeed(Integer id) {
        userStorage.findById(id);
        return jdbcTemplate.query(FIND_FEED, this::makeEvent, id);
    }

    @Override
    public void createEvent(Integer userId, EventType eventType, Operation operation, Integer entityId) {
        log.debug("Creating event with user id: {} event type: {} operation: {} entity id: {}",
                userId, eventType, operation, entityId);
        jdbcTemplate.update(CREATE_EVENT, userId, eventType.name(), operation.name(), entityId);
    }

    private Event makeEvent(ResultSet rs, int rowNum) throws SQLException {
        return Event.builder()
                .withEventId(rs.getInt("event_id"))
                .withTimestamp(rs.getTimestamp("timestamp"))
                .withUserId(rs.getInt("user_id"))
                .withEventType(EventType.valueOf(rs.getString("event_type")))
                .withOperation(Operation.valueOf(rs.getString("operation")))
                .withEntityId(rs.getInt("entity_id"))
                .build();
    }
}
