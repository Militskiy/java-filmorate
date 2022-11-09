package ru.yandex.practicum.filmorate.model;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Builder;
import lombok.Value;
import lombok.extern.jackson.Jacksonized;
import ru.yandex.practicum.filmorate.model.enums.EventType;
import ru.yandex.practicum.filmorate.model.enums.Operation;

import java.util.Date;

@Value
@Builder(setterPrefix = "with")
@Jacksonized
public class Event {
    Integer eventId;
    @JsonFormat(shape = JsonFormat.Shape.NUMBER)
    Date timestamp;
    Integer userId;
    EventType eventType;
    Operation operation;
    Integer entityId;
}
