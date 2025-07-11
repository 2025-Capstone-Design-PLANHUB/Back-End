package soon.planhub.domain.task.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import java.util.regex.Pattern;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Embeddable
public class LabelColor {

    @Column(nullable = false, length = 6)
    private String color;

    private static final Pattern HEX_COLOR_PATTERN = Pattern.compile("^[A-Fa-f0-9]{6}$");

    public LabelColor(String rawColor) {
        if (rawColor == null || rawColor.isBlank()) {
            throw new IllegalArgumentException("색상 코드는 null 또는 빈 값일 수 없습니다.");
        }

        String cleaned = rawColor.startsWith("#") ? rawColor.substring(1) : rawColor;
        if (!HEX_COLOR_PATTERN.matcher(cleaned).matches()) {
            throw new IllegalArgumentException("유효하지 않은 색상 코드입니다: " + rawColor);
        }

        this.color = cleaned.toUpperCase();
    }

    public String asHexString() {
        return "#" + this.color;
    }

}