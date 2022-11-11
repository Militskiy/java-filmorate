package ru.yandex.practicum.filmorate.model;

import lombok.Builder;
import lombok.Value;
import lombok.extern.jackson.Jacksonized;
import ru.yandex.practicum.filmorate.model.enums.EventType;
import ru.yandex.practicum.filmorate.model.enums.Operation;

import java.util.Date;

@Value
@Builder
@Jacksonized
public class Event {
    Integer eventId;
    Date timestamp;
    Integer userId;
    EventType eventType;
    Operation operation;
    Integer entityId;
}
