package rastle.dev.rastle_backend.global.response;

import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import rastle.dev.rastle_backend.global.error.response.ErrorResponse;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;

import java.lang.annotation.ElementType;
import java.lang.annotation.Inherited;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target({ ElementType.METHOD, ElementType.TYPE, ElementType.ANNOTATION_TYPE })
@Retention(RetentionPolicy.RUNTIME)
@Inherited
@ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "생성 성공시"),
        @ApiResponse(responseCode = "401", description = "토큰 만료시", content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
        @ApiResponse(responseCode = "409", description = "클라이언트에서 잘못된 데이터 전송시", content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
        @ApiResponse(responseCode = "503", description = "서버 내부에서 핸들링되지 않은 예외 발생시", content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
})
public @interface CustomApiResponses {
    Class<?> implementation() default Void.class;
}
