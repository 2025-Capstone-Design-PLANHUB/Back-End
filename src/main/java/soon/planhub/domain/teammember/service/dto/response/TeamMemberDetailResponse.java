package soon.planhub.domain.teammember.service.dto.response;

import lombok.Builder;
import soon.planhub.domain.member.entity.Member;
import soon.planhub.domain.teammember.entity.TeamMember;

@Builder
public record TeamMemberDetailResponse(

    Long teamMemberId,
    Long memberId,
    String position,
    String role,
    String nickname,
    String profileImageURL

) {

    public static TeamMemberDetailResponse from(TeamMember teamMember) {
        Member member = teamMember.getMember();
        return TeamMemberDetailResponse.builder()
            .teamMemberId(teamMember.getId())
            .memberId(member.getId())
            .position(String.valueOf(teamMember.getPosition()))
            .role(teamMember.getRole().name())
            .nickname(member.getNickname())
            .profileImageURL(member.getProfileImageURL())
            .build();
    }

}