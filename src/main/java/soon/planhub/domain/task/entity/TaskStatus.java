package soon.planhub.domain.task.entity;

import java.util.Arrays;

public enum TaskStatus {

    OPEN,
    CLOSED;

    public static TaskStatus from(String status) {
        return Arrays.stream(values())
            .filter(s -> s.name().equalsIgnoreCase(status))
            .findFirst()
            .orElseThrow(IllegalArgumentException::new);
    }

}