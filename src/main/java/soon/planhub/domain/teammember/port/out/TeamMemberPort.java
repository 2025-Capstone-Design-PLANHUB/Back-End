package soon.planhub.domain.teammember.port.out;

public interface TeamMemberPort {

    /**
     * 팀에 새로운 멤버를 조직에 추가합니다.
     *
     * @param teamId   팀 ID
     * @param memberId 추가할 멤버의 ID
     */
    void appendMemberToOrg(Long teamId, Long memberId);

}