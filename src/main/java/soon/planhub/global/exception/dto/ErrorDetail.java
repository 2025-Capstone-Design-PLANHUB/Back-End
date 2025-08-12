package soon.planhub.global.exception.dto;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum ErrorDetail {

    // 공통
    INVALID_REQUEST(400, "잘못된 요청입니다."),
    INVALID_TOKEN(401, "유효하지 않은 토큰입니다."),
    UNAUTHORIZED(401, "인증되지 않은 사용자입니다."),
    EMAIL_SEND_FAILED(500, "이메일 전송에 실패했습니다."),

    // 회원 관련
    MEMBER_NOT_FOUND(404, "해당 회원을 찾을 수 없습니다."),
    GITHUB_MEMBER_EMAIL_NOT_FOUND(404, "GitHub 회원의 이메일을 찾을 수 없습니다."),

    // 팀 관련
    TEAM_LEADER_ALREADY_EXISTS(409, "팀 리더가 이미 존재합니다."),
    TEAM_NOT_FOUND(404, "해당 팀을 찾을 수 없습니다."),
    IS_NOT_ADMIN_IN_ORGANIZATION(403, "관리자 권한이 없습니다"),

    // 팀원 관련
    TEAM_MEMBER_NOT_FOUND(404, "해당 팀원을 찾을 수 없습니다."),
    ;

    private final int status;
    private final String message;

}