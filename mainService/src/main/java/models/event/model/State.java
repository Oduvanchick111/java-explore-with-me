package models.event.model;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public enum State {
    PENDING("Ожидает рассмотрения"), PUBLISHED("Опубликован"), CANCELED("Отменен пользователем");

    private final String state;

}
