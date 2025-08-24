package soon.planhub.global.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 팀 멤버십 검증을 위한 어노테이션
 * 검증이 필요한 메소드의 경우 teamId, memberId를 순서대로 받아야 합니다.
 * teamIdArg: 팀 ID가 전달되는 메서드 인자의 이름 (기본값: "teamId")
 * memberIdArg: 멤버 ID가 전달되는 메서드 인자의 이름 (기본값: "memberId")
 */
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface TeamMembership {

    String teamIdArg() default "teamId";

    String memberIdArg() default "memberId";

}