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
        private void logException(Exception exception, HttpServletRequest webRequest) {
                log.warn("{} {} \n {} {}", webRequest.getMethod(), webRequest.getRequestURI(), exception.getClass().getName(), exception.getMessage());
        }

        private void logExceptionDetail(Exception exception, HttpServletRequest webRequest) {
                StringBuilder sb = new StringBuilder();
                for (int i = exception.getStackTrace().length-3; i < exception.getStackTrace().length; i++) {
                        StackTraceElement element = exception.getStackTrace()[i];
                        sb.append(element.getClassName());
                        sb.append(" ");
                        sb.append(element.getMethodName());
                        sb.append(" ");
                        sb.append(element.getLineNumber());
                        sb.append("\n");
                }
                log.warn("{} {} \n {} {} \n {}", webRequest.getMethod(), webRequest.getRequestURI(), exception.getClass().getName(), exception.getMessage(), sb.toString());
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
