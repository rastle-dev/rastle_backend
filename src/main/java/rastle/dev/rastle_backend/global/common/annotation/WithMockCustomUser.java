package rastle.dev.rastle_backend.global.common.annotation;
import org.springframework.security.test.context.support.WithSecurityContext;
import rastle.dev.rastle_backend.global.util.WithMockCustomUserSecurityContextFactory;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

@Retention(RetentionPolicy.RUNTIME)
@WithSecurityContext(factory = WithMockCustomUserSecurityContextFactory.class)
public @interface WithMockCustomUser {
    String userName() default "1";

    String role() default "ROLE_USER";

}
