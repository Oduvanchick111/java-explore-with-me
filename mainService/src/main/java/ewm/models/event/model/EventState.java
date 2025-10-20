package ewm.models.event.model;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public enum EventState {
    PENDING("Ожидает рассмотрения"), PUBLISHED("Опубликован"), CANCELED("Отменен пользователем");

    private final String state;

}
