package models.participationRequest.model;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum Status {
    PENDING("Ожидает рассмотрения"), CONFIRMED("Подтвержден"), CANCELED("Отменен");

    private final String status;

    public static Status fromDescription(String text) {
        for (Status status : Status.values()) {
            if (status.status.equals(text)) {
                return status;
            }
        }
        throw new IllegalArgumentException("Unknown description: " + text);
    }
}
