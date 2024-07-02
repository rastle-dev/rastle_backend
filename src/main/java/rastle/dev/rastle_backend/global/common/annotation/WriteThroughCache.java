package rastle.dev.rastle_backend.global.common.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface WriteThroughCache {
    String identifier() default "id";

    Class<?> paramClassType() default Object.class;

    boolean singleUpdate() default false;
}
