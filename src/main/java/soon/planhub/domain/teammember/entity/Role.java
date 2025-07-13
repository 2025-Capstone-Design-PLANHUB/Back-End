package soon.planhub.domain.teammember.entity;

import java.util.Arrays;

public enum Role {

    ROLE_LEADER,
    ROLE_MEMBER;

    public boolean isLeader() {
        return this == ROLE_LEADER;
    }

    public static Role from(String role) {
        return Arrays.stream(Role.values())
            .filter(r -> r.name().equals(role))
            .findFirst()
            .orElseThrow(IllegalArgumentException::new);
    }

}