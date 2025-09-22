package io.github.christosmouzouris.urlshortener.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

@ControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(UrlNotFoundException.class)
    public ResponseEntity<Map<String, Object>> handleUrlNotFoundException(UrlNotFoundException ex){
        Map<String, Object> response = new HashMap<>();
        response.put("timestamp", LocalDateTime.now());
        response.put("error", "Not found");
        response.put("message", ex.getMessage());

        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
    }

    @ExceptionHandler(UrlUpdateFailedException.class)
    public ResponseEntity<Map<String, Object>> handleUrlUpdateFailedException(UrlUpdateFailedException ex){
        Map<String, Object> response = new HashMap<>();
        response.put("timestamp", LocalDateTime.now());
        response.put("error", "Update failed");
        response.put("message", ex.getMessage());

        return  ResponseEntity.status(HttpStatus.CONFLICT).body(response);
    }
}
