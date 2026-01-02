package com.example.loopa.exception;

import com.example.loopa.dto.ApiResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(DataNotFoundException.class)
    public ResponseEntity<ApiResponse<String>> dataNotFound(DataNotFoundException ex){
        return ResponseEntity.status(404).body(
                ApiResponse.fail(ex.getMessage())
        );
    }

    public ResponseEntity<ApiResponse<String>> serverError(Exception ex){
        return ResponseEntity.status(500).body(
                ApiResponse.fail("Serverda xatolik: " + ex.getMessage())
        );
    }

    @ExceptionHandler(AccessDeniedException.class)
    public ResponseEntity<ApiResponse<String>> handleAccessDenied(AccessDeniedException ex) {
        return ResponseEntity
                .status(HttpStatus.FORBIDDEN)
                .body(ApiResponse.fail(ex.getMessage()));
    }
}
