package soon.planhub.domain.milestone.entity;

import java.util.Arrays;

public enum MilestoneStatus {

    NOT_STARTED,
    IN_PROGRESS,
    DONE;

    public static MilestoneStatus from(String status) {
        return Arrays.stream(values())
            .filter(s -> s.name().equalsIgnoreCase(status))
            .findFirst()
            .orElseThrow(IllegalArgumentException::new);
    }

    public String toState() {
        return (this == DONE) ? "closed" : "open";
    }

}