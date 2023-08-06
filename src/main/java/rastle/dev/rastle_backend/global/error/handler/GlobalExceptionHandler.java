package rastle.dev.rastle_backend.global.error.handler;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;
import rastle.dev.rastle_backend.global.error.exception.InvalidRequestException;
import rastle.dev.rastle_backend.global.error.exception.NotAuthorizedException;
import rastle.dev.rastle_backend.global.error.exception.NotFoundByIdException;
import rastle.dev.rastle_backend.global.error.response.ErrorResponse;

import static org.springframework.http.HttpStatus.NOT_FOUND;
import static org.springframework.http.HttpStatus.UNAUTHORIZED;

@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {
    @ExceptionHandler(NotFoundByIdException.class)
    protected final ResponseEntity<ErrorResponse> handleNotFoundByIdException(
            NotFoundByIdException ex, WebRequest request
    ) {
        log.warn(ex.getMessage());
        return new ResponseEntity<>(ErrorResponse.builder()
                .errorCode(404L)
                .message(ex.getMessage())
                .build()
                , NOT_FOUND);
    }

    @ExceptionHandler(NotAuthorizedException.class)
    protected final ResponseEntity<ErrorResponse> handleNotAuthorizedException(
            NotAuthorizedException ex, WebRequest request
    ) {
        log.warn(ex.getMessage());
        return new ResponseEntity<>(ErrorResponse.builder()
                .errorCode(401L)
                .message(ex.getMessage())
                .build()
                , UNAUTHORIZED);
    }


    @ExceptionHandler(InvalidRequestException.class)
    protected final ResponseEntity<ErrorResponse> handleInvalidRequestException(
            InvalidRequestException ex, WebRequest request
    ) {
        log.warn(ex.getMessage());
        return new ResponseEntity<>(ErrorResponse.builder()
                .errorCode(409L)
                .message(ex.getMessage())
                .build()
                , UNAUTHORIZED);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    protected final ResponseEntity<ErrorResponse> handleMethodArgumentNotValidException(
            MethodArgumentNotValidException ex, WebRequest request
    ) {
        log.warn(ex.getLocalizedMessage());
        BindingResult bindingResult = ex.getBindingResult();
        StringBuilder builder = new StringBuilder();
        for (FieldError fieldError : bindingResult.getFieldErrors()) {
            builder.append("[");
            builder.append(fieldError.getField());
            builder.append("](은)는 ");
            builder.append(fieldError.getDefaultMessage());
            builder.append(" 입력된 값: [");
            builder.append(fieldError.getRejectedValue());
            builder.append("]");
        }

        return new ResponseEntity<>(
                ErrorResponse.builder()
                        .errorCode(409L)
                        .message(builder.toString()).build(),
                HttpStatus.CONFLICT
        );

    }
}
