package ru.yandex.practicum.filmorate.validators;

import javax.validation.GroupSequence;
import javax.validation.groups.Default;

@GroupSequence({NullCheckGroup.class, Default.class})
public interface ValidationSequence {
}
