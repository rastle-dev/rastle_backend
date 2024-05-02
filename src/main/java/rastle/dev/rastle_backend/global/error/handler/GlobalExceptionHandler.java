package rastle.dev.rastle_backend.global.error.handler;

import com.fasterxml.jackson.core.JsonProcessingException;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import rastle.dev.rastle_backend.global.error.exception.GlobalException;
import rastle.dev.rastle_backend.global.error.response.ErrorResponse;

import static org.springframework.http.HttpStatus.CONFLICT;
import static org.springframework.http.HttpStatus.INTERNAL_SERVER_ERROR;

@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {
    private void logException(Exception exception,HttpServletRequest webRequest) {
        log.warn("{} {}", webRequest.getMethod(), webRequest.getRequestURI());
        StackTraceElement[] stackTrace = exception.getStackTrace();
        log.warn(exception.getClass().getName(), stackTrace[0]);
        log.warn(exception.getMessage(), stackTrace[0]);
    }

    @ExceptionHandler(GlobalException.class)
    protected final ResponseEntity<ErrorResponse> handleGlobalException(
        GlobalException ex, HttpServletRequest request
    ) {
        logException(ex, request);
        return new ResponseEntity<>(ErrorResponse.builder()
            .errorCode((long) ex.getStatus().value())
            .message(ex.getMessage())
            .build(), ex.getStatus());
    }



    @ExceptionHandler(MethodArgumentNotValidException.class)
    protected final ResponseEntity<ErrorResponse> handleMethodArgumentNotValidException(
        MethodArgumentNotValidException ex, HttpServletRequest request) {
        logException(ex, request);
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
            CONFLICT);

    }

    @ExceptionHandler(JsonProcessingException.class)
    protected final ResponseEntity<ErrorResponse> handleJsonException(
        JsonProcessingException ex, HttpServletRequest request
    ) {
        logException(ex, request);
        return new ResponseEntity<>(ErrorResponse.builder()
            .errorCode(409L)
            .message(ex.getMessage())
            .build(),
            CONFLICT);
    }


    @ExceptionHandler(Exception.class)
    protected final ResponseEntity<ErrorResponse> handleException(
        Exception ex, HttpServletRequest request
    ) {
        logException(ex, request);
        return new ResponseEntity<>(ErrorResponse.builder()
            .errorCode(500L)
            .message(ex.getMessage())
            .build(),
            INTERNAL_SERVER_ERROR);
    }

    @ExceptionHandler(RuntimeException.class)
    protected final ResponseEntity<ErrorResponse> handleRuntimeException(
        RuntimeException ex, HttpServletRequest request
    ) {
        logException(ex, request);
        return new ResponseEntity<>(ErrorResponse.builder()
            .errorCode(500L)
            .message(ex.getMessage())
            .build(),
            INTERNAL_SERVER_ERROR);
    }


}
