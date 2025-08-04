package soon.planhub.global.annotation;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import org.springframework.security.test.context.support.WithSecurityContext;
import soon.planhub.TestSecurityContext;

@Retention(RetentionPolicy.RUNTIME)
@WithSecurityContext(factory = TestSecurityContext.class)
public @interface TestMemberId {

    long id() default 1L;

    String[] roles() default {"USER"};

}