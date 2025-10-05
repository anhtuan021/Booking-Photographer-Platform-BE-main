package org.bookingplatform.exception;

import lombok.extern.slf4j.Slf4j;
import org.bookingplatform.dto.BaseResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.validation.FieldError;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingRequestHeaderException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.multipart.support.MissingServletRequestPartException;
import org.springframework.web.servlet.resource.NoResourceFoundException;
import com.fasterxml.jackson.databind.exc.InvalidFormatException;
import com.fasterxml.jackson.databind.exc.MismatchedInputException;
import com.fasterxml.jackson.core.JsonParseException;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(NotFoundException.class)
    public ResponseEntity<BaseResponse<Void>> handleNotFoundException(NotFoundException ex) {
        return ResponseEntity.badRequest().body(
                new BaseResponse<>(UUID.randomUUID().toString(), "BOOKING_NOT_FOUND", ex.getMessage())
        );
    }

    @ExceptionHandler(MissingRequestHeaderException.class)
    public ResponseEntity<BaseResponse<Void>> handleMissingRequestHeader(MissingRequestHeaderException ex) {
        String message = "Missing required header: " + ex.getHeaderName();
        return ResponseEntity.badRequest().body(new BaseResponse<>(
                UUID.randomUUID().toString(), "04010001", message));
    }

    @ExceptionHandler(UnauthorizedException.class)
    public ResponseEntity<BaseResponse<Void>> handleUnauthorizedException(UnauthorizedException ex) {
        BaseResponse<Void> response = BaseResponse.<Void>builder()
                .requestTrace(UUID.randomUUID().toString())
                .responseStatus(BaseResponse.ResponseStatus.builder()
                        .responseCode("UNAUTHORIZED")
                        .responseMessage(ex.getMessage())
                        .build())
                .build();
        return ResponseEntity.status(HttpStatus.FORBIDDEN).body(response);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<BaseResponse<Map<String, String>>> handleValidationExceptions(
            MethodArgumentNotValidException ex, WebRequest request) {
        
        Map<String, String> errors = new HashMap<>();
        ex.getBindingResult().getAllErrors().forEach((error) -> {
            String fieldName = ((FieldError) error).getField();
            String errorMessage = error.getDefaultMessage();
            errors.put(fieldName, errorMessage);
        });

        BaseResponse<Map<String, String>> response = BaseResponse.<Map<String, String>>builder()
                .requestTrace(UUID.randomUUID().toString())
                .responseDateTime(LocalDateTime.now())
                .responseStatus(BaseResponse.ResponseStatus.builder()
                        .responseCode("04000001")
                        .responseMessage("Validation failed")
                        .build())
                .responseData(errors)
                .build();

        log.error("Validation error: {}", errors);
        return ResponseEntity.badRequest().body(response);
    }

    @ExceptionHandler(AuthenticationException.class)
    public ResponseEntity<BaseResponse<Void>> handleAuthenticationException(
            AuthenticationException ex, WebRequest request) {

        BaseResponse<Void> response = BaseResponse.<Void>builder()
                .requestTrace(UUID.randomUUID().toString())
                .responseDateTime(LocalDateTime.now())
                .responseStatus(BaseResponse.ResponseStatus.builder()
                        .responseCode("01010001")
                        .responseMessage("Authentication failed: " + ex.getMessage())
                        .build())
                .responseData(null)
                .build();

        log.error("Authentication error: {}", ex.getMessage());
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(response);
    }

    @ExceptionHandler(BadCredentialsException.class)
    public ResponseEntity<BaseResponse<Void>> handleBadCredentialsException(
            BadCredentialsException ex, WebRequest request) {

        BaseResponse<Void> response = BaseResponse.<Void>builder()
                .requestTrace(UUID.randomUUID().toString())
                .responseDateTime(LocalDateTime.now())
                .responseStatus(BaseResponse.ResponseStatus.builder()
                        .responseCode("01010002")
                        .responseMessage("Invalid credentials")
                        .build())
                .responseData(null)
                .build();

        log.error("Bad credentials error: {}", ex.getMessage());
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(response);
    }

    @ExceptionHandler(AccessDeniedException.class)
    public ResponseEntity<BaseResponse<Void>> handleAccessDeniedException(
            AccessDeniedException ex, WebRequest request) {

        BaseResponse<Void> response = BaseResponse.<Void>builder()
                .requestTrace(UUID.randomUUID().toString())
                .responseDateTime(LocalDateTime.now())
                .responseStatus(BaseResponse.ResponseStatus.builder()
                        .responseCode("01010003")
                        .responseMessage("Access denied: " + ex.getMessage())
                        .build())
                .responseData(null)
                .build();

        log.error("Access denied error: {}", ex.getMessage());
        return ResponseEntity.status(HttpStatus.FORBIDDEN).body(response);
    }

    @ExceptionHandler(ResourceNotFoundException.class)
    public ResponseEntity<BaseResponse<Void>> handleResourceNotFoundException(
            ResourceNotFoundException ex, WebRequest request) {
        
        BaseResponse<Void> response = BaseResponse.<Void>builder()
                .requestTrace(UUID.randomUUID().toString())
                .responseDateTime(LocalDateTime.now())
                .responseStatus(BaseResponse.ResponseStatus.builder()
                        .responseCode("02000001")
                        .responseMessage("Resource not found: " + ex.getMessage())
                        .build())
                .responseData(null)
                .build();

        log.error("Resource not found error: {}", ex.getMessage());
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
    }

    @ExceptionHandler(BusinessException.class)
    public ResponseEntity<BaseResponse<Void>> handleBusinessException(
            BusinessException ex, WebRequest request) {
        
        BaseResponse<Void> response = BaseResponse.<Void>builder()
                .requestTrace(UUID.randomUUID().toString())
                .responseDateTime(LocalDateTime.now())
                .responseStatus(BaseResponse.ResponseStatus.builder()
                        .responseCode(ex.getErrorCode())
                        .responseMessage(ex.getMessage())
                        .build())
                .responseData(null)
                .build();

        log.error("Business error: {}", ex.getMessage());
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
    }

    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<BaseResponse<Void>> handleIllegalArgumentException(
            IllegalArgumentException ex, WebRequest request) {
        
        BaseResponse<Void> response = BaseResponse.<Void>builder()
                .requestTrace(UUID.randomUUID().toString())
                .responseDateTime(LocalDateTime.now())
                .responseStatus(BaseResponse.ResponseStatus.builder()
                        .responseCode("03000001")
                        .responseMessage("Invalid argument: " + ex.getMessage())
                        .build())
                .responseData(null)
                .build();

        log.error("Illegal argument error: {}", ex.getMessage());
        return ResponseEntity.badRequest().body(response);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<BaseResponse<Void>> handleGenericException(
            Exception ex, WebRequest request) {
        
        BaseResponse<Void> response = BaseResponse.<Void>builder()
                .requestTrace(UUID.randomUUID().toString())
                .responseDateTime(LocalDateTime.now())
                .responseStatus(BaseResponse.ResponseStatus.builder()
                        .responseCode("99999999")
                        .responseMessage("Internal server error")
                        .build())
                .responseData(null)
                .build();

        log.error("Unexpected error: ", ex);
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
    }

    @ExceptionHandler(NoResourceFoundException.class)
    public ResponseEntity<BaseResponse<Void>> handleNoResourceFoundException(
            NoResourceFoundException ex, WebRequest request) {

        BaseResponse<Void> response = BaseResponse.<Void>builder()
                .requestTrace(java.util.UUID.randomUUID().toString())
                .responseDateTime(java.time.LocalDateTime.now())
                .responseStatus(BaseResponse.ResponseStatus.builder()
                        .responseCode("04040400")
                        .responseMessage("Static resource not found: " + ex.getMessage())
                        .build())
                .responseData(null)
                .build();

        log.error("No static resource found: {}", ex.getMessage());
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
    }

    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ResponseEntity<BaseResponse<Void>> handleHttpMessageNotReadableException(
            HttpMessageNotReadableException ex, WebRequest request) {
        
        String errorMessage = "Invalid request format";
        String errorCode = "04000002";
        
        // Check for specific JSON parsing errors
        Throwable cause = ex.getCause();
        if (cause instanceof InvalidFormatException) {
            InvalidFormatException invalidFormatEx = (InvalidFormatException) cause;
            if (invalidFormatEx.getTargetType() == LocalDateTime.class) {
                errorMessage = "Invalid date/time format. Expected format: yyyy-MM-dd'T'HH:mm:ss or yyyy-MM-dd'T'HH:mm:ss.SSS'Z'";
                errorCode = "04000003";
            } else {
                errorMessage = "Invalid format for field: " + invalidFormatEx.getPath().get(0).getFieldName() + 
                             ". Expected type: " + invalidFormatEx.getTargetType().getSimpleName();
                errorCode = "04000004";
            }
        } else if (cause instanceof MismatchedInputException) {
            MismatchedInputException mismatchedEx = (MismatchedInputException) cause;
            errorMessage = "Invalid input type for field: " + mismatchedEx.getPath().get(0).getFieldName() + 
                          ". Expected: " + mismatchedEx.getTargetType().getSimpleName();
            errorCode = "04000005";
        } else if (cause instanceof JsonParseException) {
            JsonParseException parseEx = (JsonParseException) cause;
            errorMessage = "Invalid JSON format: " + parseEx.getOriginalMessage();
            errorCode = "04000006";
        } else if (ex.getMessage() != null) {
            if (ex.getMessage().contains("Required request body is missing")) {
                errorMessage = "Request body is required";
                errorCode = "04000007";
            } else if (ex.getMessage().contains("JSON parse error")) {
                errorMessage = "Invalid JSON format in request body";
                errorCode = "04000008";
            }
        }

        BaseResponse<Void> response = BaseResponse.<Void>builder()
                .requestTrace(UUID.randomUUID().toString())
                .responseDateTime(LocalDateTime.now())
                .responseStatus(BaseResponse.ResponseStatus.builder()
                        .responseCode(errorCode)
                        .responseMessage(errorMessage)
                        .build())
                .responseData(null)
                .build();

        log.error("HTTP message not readable error: {}", ex.getMessage());
        return ResponseEntity.badRequest().body(response);
    }

    @ExceptionHandler(InvalidFormatException.class)
    public ResponseEntity<BaseResponse<Void>> handleInvalidFormatException(
            InvalidFormatException ex, WebRequest request) {
        
        String errorMessage = "Invalid format for field: " + ex.getPath().get(0).getFieldName();
        String errorCode = "04000009";
        
        if (ex.getTargetType() == LocalDateTime.class) {
            errorMessage = "Invalid date/time format for field: " + ex.getPath().get(0).getFieldName() + 
                          ". Expected format: yyyy-MM-dd'T'HH:mm:ss or yyyy-MM-dd'T'HH:mm:ss.SSS'Z'";
            errorCode = "04000010";
        } else if (ex.getTargetType() == java.time.LocalDate.class) {
            errorMessage = "Invalid date format for field: " + ex.getPath().get(0).getFieldName() + 
                          ". Expected format: yyyy-MM-dd";
            errorCode = "04000011";
        } else if (ex.getTargetType() == java.time.LocalTime.class) {
            errorMessage = "Invalid time format for field: " + ex.getPath().get(0).getFieldName() + 
                          ". Expected format: HH:mm:ss";
            errorCode = "04000012";
        }

        BaseResponse<Void> response = BaseResponse.<Void>builder()
                .requestTrace(UUID.randomUUID().toString())
                .responseDateTime(LocalDateTime.now())
                .responseStatus(BaseResponse.ResponseStatus.builder()
                        .responseCode(errorCode)
                        .responseMessage(errorMessage)
                        .build())
                .responseData(null)
                .build();

        log.error("Invalid format error: {}", ex.getMessage());
        return ResponseEntity.badRequest().body(response);
    }

    @ExceptionHandler(MismatchedInputException.class)
    public ResponseEntity<BaseResponse<Void>> handleMismatchedInputException(
            MismatchedInputException ex, WebRequest request) {
        
        String fieldName = ex.getPath().isEmpty() ? "unknown" : ex.getPath().get(0).getFieldName();
        String errorMessage = "Invalid input type for field: " + fieldName + 
                             ". Expected: " + ex.getTargetType().getSimpleName();
        
        BaseResponse<Void> response = BaseResponse.<Void>builder()
                .requestTrace(UUID.randomUUID().toString())
                .responseDateTime(LocalDateTime.now())
                .responseStatus(BaseResponse.ResponseStatus.builder()
                        .responseCode("04000013")
                        .responseMessage(errorMessage)
                        .build())
                .responseData(null)
                .build();

        log.error("Mismatched input error: {}", ex.getMessage());
        return ResponseEntity.badRequest().body(response);
    }

    @ExceptionHandler(JsonParseException.class)
    public ResponseEntity<BaseResponse<Void>> handleJsonParseException(
            JsonParseException ex, WebRequest request) {
        
        BaseResponse<Void> response = BaseResponse.<Void>builder()
                .requestTrace(UUID.randomUUID().toString())
                .responseDateTime(LocalDateTime.now())
                .responseStatus(BaseResponse.ResponseStatus.builder()
                        .responseCode("04000014")
                        .responseMessage("Invalid JSON format: " + ex.getOriginalMessage())
                        .build())
                .responseData(null)
                .build();

        log.error("JSON parse error: {}", ex.getMessage());
        return ResponseEntity.badRequest().body(response);
    }

    @ExceptionHandler(org.springframework.web.method.annotation.MethodArgumentTypeMismatchException.class)
    public ResponseEntity<BaseResponse<Void>> handleMethodArgumentTypeMismatchException(
            org.springframework.web.method.annotation.MethodArgumentTypeMismatchException ex, WebRequest request) {
        
        String errorMessage = "Invalid parameter type for: " + ex.getName() + 
                             ". Expected: " + ex.getRequiredType().getSimpleName();
        
        BaseResponse<Void> response = BaseResponse.<Void>builder()
                .requestTrace(UUID.randomUUID().toString())
                .responseDateTime(LocalDateTime.now())
                .responseStatus(BaseResponse.ResponseStatus.builder()
                        .responseCode("04000015")
                        .responseMessage(errorMessage)
                        .build())
                .responseData(null)
                .build();

        log.error("Method argument type mismatch error: {}", ex.getMessage());
        return ResponseEntity.badRequest().body(response);
    }

    @ExceptionHandler(org.springframework.web.bind.MissingServletRequestParameterException.class)
    public ResponseEntity<BaseResponse<Void>> handleMissingServletRequestParameterException(
            org.springframework.web.bind.MissingServletRequestParameterException ex, WebRequest request) {
        
        BaseResponse<Void> response = BaseResponse.<Void>builder()
                .requestTrace(UUID.randomUUID().toString())
                .responseDateTime(LocalDateTime.now())
                .responseStatus(BaseResponse.ResponseStatus.builder()
                        .responseCode("04000016")
                        .responseMessage("Missing required parameter: " + ex.getParameterName())
                        .build())
                .responseData(null)
                .build();

        log.error("Missing request parameter error: {}", ex.getMessage());
        return ResponseEntity.badRequest().body(response);
    }

    @ExceptionHandler(MissingServletRequestPartException.class)
    public ResponseEntity<BaseResponse<Void>> handleMissingServletRequestPartException(
            MissingServletRequestPartException ex) {
        String traceId = java.util.UUID.randomUUID().toString();
        String message = "Missing required file part: " + ex.getRequestPartName();

        BaseResponse<Void> response = BaseResponse.<Void>builder()
                .requestTrace(UUID.randomUUID().toString())
                .responseDateTime(LocalDateTime.now())
                .responseStatus(BaseResponse.ResponseStatus.builder()
                        .responseCode("04000016")
                        .responseMessage("Missing required file part: " + ex.getRequestPartName())
                        .build())
                .responseData(null)
                .build();

        return ResponseEntity.badRequest().body(response);
    }

    @ExceptionHandler(org.springframework.http.converter.HttpMessageConversionException.class)
    public ResponseEntity<BaseResponse<Void>> handleHttpMessageConversionException(
            org.springframework.http.converter.HttpMessageConversionException ex, WebRequest request) {

        String errorMessage = "Serialization error: Unable to convert response object. " +
                "This may be caused by lazy-loaded entities or Hibernate proxies.";
        String errorCode = "04000017";

        BaseResponse<Void> response = BaseResponse.<Void>builder()
                .requestTrace(java.util.UUID.randomUUID().toString())
                .responseDateTime(java.time.LocalDateTime.now())
                .responseStatus(BaseResponse.ResponseStatus.builder()
                        .responseCode(errorCode)
                        .responseMessage(errorMessage)
                        .build())
                .responseData(null)
                .build();

        log.error("HttpMessageConversionException: {}", ex.getMessage(), ex);
        return ResponseEntity.badRequest().body(response);
    }

    @ExceptionHandler(org.springframework.web.HttpMediaTypeNotSupportedException.class)
    public ResponseEntity<BaseResponse<Void>> handleHttpMediaTypeNotSupportedException(
            org.springframework.web.HttpMediaTypeNotSupportedException ex, WebRequest request) {

        String errorMessage = "Content-Type 'application/octet-stream' is not supported";
        String errorCode = "04000017";

        BaseResponse<Void> response = BaseResponse.<Void>builder()
                .requestTrace(java.util.UUID.randomUUID().toString())
                .responseDateTime(java.time.LocalDateTime.now())
                .responseStatus(BaseResponse.ResponseStatus.builder()
                        .responseCode(errorCode)
                        .responseMessage(errorMessage)
                        .build())
                .responseData(null)
                .build();

        log.error("org.springframework.web.HttpMediaTypeNotSupportedException: {}", ex.getMessage(), ex);
        return ResponseEntity.badRequest().body(response);
    }
}
