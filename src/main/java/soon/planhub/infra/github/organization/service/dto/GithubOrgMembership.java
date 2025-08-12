package soon.planhub.infra.github.organization.service.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;

@Builder
public record GithubOrgMembership(

    OrgRole role,
    OrgState state

) {

    public boolean isActiveAdmin() {
        return role == OrgRole.ADMIN && state == OrgState.ACTIVE;
    }

    private enum OrgRole {
        @JsonProperty("admin")
        ADMIN,
        @JsonProperty("member")
        MEMBER
    }

    private enum OrgState {
        @JsonProperty("active")
        ACTIVE,
        @JsonProperty("pending")
        PENDING
    }

}