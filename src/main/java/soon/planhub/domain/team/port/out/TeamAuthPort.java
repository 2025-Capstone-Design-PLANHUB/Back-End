package soon.planhub.domain.team.port.out;

public interface TeamAuthPort {

    /**
     * 사용자가 특정 조직의 관리자인지 확인합니다.
     *
     * @param organizationName 검증할 조직 이름
     * @param memberId         사용자 ID
     * @return 관리자 여부
     */
    boolean isActiveAdmin(String organizationName, Long memberId);

}