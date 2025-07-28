package soon.planhub.global.exception.dto;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum ErrorDetail {

    // 공통
    INVALID_REQUEST(400, "잘못된 요청입니다."),
    INVALID_TOKEN(401, "유효하지 않은 토큰입니다."),

    // 회원 관련
    MEMBER_NOT_FOUND(404, "해당 회원을 찾을 수 없습니다."),
    GITHUB_MEMBER_EMAIL_NOT_FOUND(404, "GitHub 회원의 이메일을 찾을 수 없습니다."),
    ;

    private final int status;
    private final String message;

}