
package com.staysphere.tenantprovisioning.exception;

import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.http.HttpStatus;

import com.staysphere.tenantprovisioning.dto.ApiErrorResponse;
import com.staysphere.usermgmt.exception.EmailIdExistsException;
import com.staysphere.usermgmt.exception.UserNotFoundException;

import java.time.Instant;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.List;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ApiErrorResponse> handleValidationException(
            MethodArgumentNotValidException exception) {
        
        List<FieldError> fieldError = exception.getBindingResult().getFieldErrors();
        
        Map<String, List<String>> groupedErrors = fieldError.stream().collect(Collectors.groupingBy(
    FieldError::getField,
    Collectors.mapping(
        FieldError::getDefaultMessage,
        Collectors.toList()
    )
));
        ApiErrorResponse response = new ApiErrorResponse(Instant.now(), HttpStatus.BAD_REQUEST.value(),
        HttpStatus.BAD_REQUEST.getReasonPhrase(),
        "Validation failed",
        groupedErrors);

        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
    }

    @ExceptionHandler(CompanyNotFoundException.class)
    public ResponseEntity<ApiErrorResponse> handleCompanyNotFoundException(CompanyNotFoundException exception){
        Map<String, List<String>> groupedErrors = Map.of();
    ApiErrorResponse response = new ApiErrorResponse(Instant.now(), HttpStatus.NOT_FOUND.value(),
        HttpStatus.NOT_FOUND.getReasonPhrase(),
        exception.getMessage(), groupedErrors);
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
    }

    @ExceptionHandler (InvalidStatusUpdateException.class)
    public ResponseEntity<ApiErrorResponse> InvalidStatusUpdateException(InvalidStatusUpdateException exception){
        Map<String, List<String>> groupedErrors = Map.of();
    ApiErrorResponse response = new ApiErrorResponse(Instant.now(), HttpStatus.NOT_ACCEPTABLE.value(),
        HttpStatus.NOT_ACCEPTABLE.getReasonPhrase(),
        exception.getMessage(), groupedErrors);
        return ResponseEntity.status(HttpStatus.NOT_ACCEPTABLE).body(response);
    }

    @ExceptionHandler (EmailIdExistsException.class)
    public ResponseEntity<ApiErrorResponse> EmailIdExistsException(EmailIdExistsException exception){
        Map<String, List<String>> groupedErrors = Map.of();
    ApiErrorResponse response = new ApiErrorResponse(Instant.now(), HttpStatus.NOT_ACCEPTABLE.value(),
        HttpStatus.NOT_ACCEPTABLE.getReasonPhrase(),
        exception.getMessage(), groupedErrors);
        return ResponseEntity.status(HttpStatus.CONFLICT).body(response);
    }

    @ExceptionHandler (UserNotFoundException.class)
    public ResponseEntity<ApiErrorResponse> UserNotFoundException(UserNotFoundException exception){
        Map<String, List<String>> groupedErrors = Map.of();
    ApiErrorResponse response = new ApiErrorResponse(Instant.now(), HttpStatus.NOT_ACCEPTABLE.value(),
        HttpStatus.NOT_ACCEPTABLE.getReasonPhrase(),
        exception.getMessage(), groupedErrors);
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
    }
}
