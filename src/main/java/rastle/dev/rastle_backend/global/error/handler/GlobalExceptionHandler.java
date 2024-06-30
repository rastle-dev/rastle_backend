package rastle.dev.rastle_backend.global.error.handler;

import com.fasterxml.jackson.core.JsonProcessingException;
import io.jsonwebtoken.JwtException;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.AuthenticationException;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import rastle.dev.rastle_backend.global.error.exception.GlobalException;
import rastle.dev.rastle_backend.global.error.response.ErrorResponse;

import static org.springframework.http.HttpStatus.*;

@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {
        private void logException(Exception exception, HttpServletRequest webRequest) {
                log.warn("{} {} {} {}", webRequest.getMethod(), webRequest.getRequestURI(), exception.getClass().getName(), exception.getMessage());
        }

        private void logExceptionDetail(Exception exception, HttpServletRequest webRequest) {
                StringBuilder sb = new StringBuilder();
                for (int i = 0; i < exception.getStackTrace().length; i++) {
                        StackTraceElement element = exception.getStackTrace()[i];
                        sb.append("\t")
                            .append(element.getClassName())
                            .append(" ")
                            .append(element.getMethodName())
                            .append(" ")
                            .append(element.getLineNumber())
                            .append("\n");
                }
                log.warn("{} {} {} {} \n{}", webRequest.getMethod(), webRequest.getRequestURI(), exception.getClass().getName(), exception.getMessage(), sb.toString());
        }

        @ExceptionHandler(GlobalException.class)
        protected final ResponseEntity<ErrorResponse> handleGlobalException(
                        GlobalException ex, HttpServletRequest request) {
                logException(ex, request);
                return new ResponseEntity<>(ErrorResponse.builder()
                                .errorCode((long) ex.getStatus().value())
                                .message(ex.getMessage())
                                .build(), ex.getStatus());
        }

        @ExceptionHandler(AuthenticationException.class)
        protected final ResponseEntity<ErrorResponse> handleAuthenticationException(
            AuthenticationException ex, HttpServletRequest request
        ) {
                logException(ex, request);
                return new ResponseEntity<>(ErrorResponse.builder()
                    .errorCode(404L)
                    .message(ex.getMessage())
                    .build(), NOT_FOUND);
        }

        @ExceptionHandler(JwtException.class)
        protected final ResponseEntity<ErrorResponse> handleJwtException(
            JwtException ex, HttpServletRequest request
        ) {
                logException(ex, request);
                return new ResponseEntity<>(ErrorResponse.builder()
                    .errorCode(400L)
                    .message(ex.getMessage())
                    .build(), BAD_REQUEST);
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
                        JsonProcessingException ex, HttpServletRequest request) {
                logException(ex, request);
                return new ResponseEntity<>(ErrorResponse.builder()
                                .errorCode(409L)
                                .message(ex.getMessage())
                                .build(),
                                CONFLICT);
        }

        @ExceptionHandler(Exception.class)
        protected final ResponseEntity<ErrorResponse> handleException(
                        Exception ex, HttpServletRequest request) {
                logExceptionDetail(ex, request);
                return new ResponseEntity<>(ErrorResponse.builder()
                                .errorCode(500L)
                                .message(ex.getMessage())
                                .build(),
                                INTERNAL_SERVER_ERROR);
        }

        @ExceptionHandler(RuntimeException.class)
        protected final ResponseEntity<ErrorResponse> handleRuntimeException(
                        RuntimeException ex, HttpServletRequest request) {
                logExceptionDetail(ex, request);
                return new ResponseEntity<>(ErrorResponse.builder()
                                .errorCode(500L)
                                .message(ex.getMessage())
                                .build(),
                                INTERNAL_SERVER_ERROR);
        }

        // @ExceptionHandler(PaymentErrorException.class)
        // public ResponseEntity<Object>
        // handlePaymentErrorException(PaymentErrorException ex,
        // UriComponentsBuilder uriComponentsBuilder) {
        // String redirectUrl = UriComponentsBuilder
        // .fromUriString("https://www.recordyslow.com/orderConfirm")
        // .queryParam("error", ex.getMessage())
        // .queryParam("errorCode", ex.getErrorCode())
        // .build()
        // .toUriString();

        // HttpHeaders httpHeaders = new HttpHeaders();
        // httpHeaders.setLocation(URI.create(redirectUrl));
        // return new ResponseEntity<>(httpHeaders, HttpStatus.SEE_OTHER);
        // }
}
