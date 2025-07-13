package soon.planhub.domain.milestone.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import java.time.LocalDateTime;

@Embeddable
public record MilestoneSchedule(

    @Column(nullable = false)
    LocalDateTime startDateTime,

    @Column(nullable = false)
    LocalDateTime dueDateTime

) {

    public MilestoneSchedule {
        if (startDateTime != null && dueDateTime != null && startDateTime.isAfter(dueDateTime)) {
            throw new IllegalArgumentException("시작일은 마감일보다 늦을 수 없습니다.");
        }
    }

}