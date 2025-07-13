package soon.planhub.domain.teammember.entity;

import java.util.Arrays;

public enum Position {

    NONE, // 초기 상태
    BACKEND,
    FRONTEND,
    FULLSTACK,
    MOBILE,
    ANDROID,
    IOS,
    DEVOPS,
    DBA,
    PLANNER,
    PM,
    MARKETER,
    DESIGNER,
    QA,
    ETC;

    public static Position from(String position) {
        return Arrays.stream(values())
            .filter(value -> value.name().equalsIgnoreCase(position))
            .findFirst()
            .orElseThrow(IllegalArgumentException::new);
    }

}