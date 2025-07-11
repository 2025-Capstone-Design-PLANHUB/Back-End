package soon.planhub.domain.task.entity;

import java.util.Arrays;

public enum TaskType {

    Feature,
    Refactor,
    Fix,
    Custom;

    public static TaskType from(String type) {
        return Arrays.stream(values())
            .filter(s -> s.name().equalsIgnoreCase(type))
            .findFirst()
            .orElseThrow(IllegalArgumentException::new);
    }

}