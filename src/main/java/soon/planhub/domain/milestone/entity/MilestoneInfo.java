package soon.planhub.domain.milestone.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;

@Embeddable
public record MilestoneInfo(

    @Column(nullable = false)
    String title,

    @Column(nullable = false)
    String description

) {

}