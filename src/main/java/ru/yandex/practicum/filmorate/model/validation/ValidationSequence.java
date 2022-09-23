package ru.yandex.practicum.filmorate.model.validation;

import javax.validation.GroupSequence;
import javax.validation.groups.Default;

@GroupSequence({NullCheckGroup.class, Default.class})
public interface ValidationSequence {
}
