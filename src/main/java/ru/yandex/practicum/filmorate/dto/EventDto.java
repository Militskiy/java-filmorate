package ru.yandex.practicum.filmorate.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Value;
import lombok.extern.jackson.Jacksonized;
import ru.yandex.practicum.filmorate.model.enums.EventType;
import ru.yandex.practicum.filmorate.model.enums.Operation;

import java.util.Date;

@Value
@Builder
@Jacksonized
public class EventDto {
    @Schema(example = "1", accessMode = Schema.AccessMode.READ_ONLY)
    Integer eventId;
    @JsonFormat(shape = JsonFormat.Shape.NUMBER)
    @Schema(example = "1655798323571", accessMode = Schema.AccessMode.READ_ONLY)
    Date timestamp;
    @Schema(example = "1", accessMode = Schema.AccessMode.READ_ONLY)
    Integer userId;
    @Schema(type = "enum", accessMode = Schema.AccessMode.READ_ONLY)
    EventType eventType;
    @Schema(type = "enum",accessMode = Schema.AccessMode.READ_ONLY)
    Operation operation;
    @Schema(example = "1", accessMode = Schema.AccessMode.READ_ONLY)
    Integer entityId;
}
