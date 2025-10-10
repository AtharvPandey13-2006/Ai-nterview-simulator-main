package com.example.interviewsimulator.util;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

/**
 * Global Exception Handler for the Interview Simulator Application
 */
@ControllerAdvice
public class GlobalExceptionHandler {

    private static final Logger logger = LoggerFactory.getLogger(GlobalExceptionHandler.class);

    @ExceptionHandler(Exception.class)
    public ResponseEntity<Map<String, Object>> handleGlobalException(
            Exception ex, WebRequest request) {
        
        logger.error("Unexpected error occurred: ", ex);
        
        Map<String, Object> errorDetails = new HashMap<>();
        errorDetails.put("timestamp", LocalDateTime.now());
        errorDetails.put("message", "An unexpected error occurred");
        errorDetails.put("details", ex.getMessage());
        errorDetails.put("path", request.getDescription(false));
        errorDetails.put("status", HttpStatus.INTERNAL_SERVER_ERROR.value());
        
        return new ResponseEntity<>(errorDetails, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<Map<String, Object>> handleIllegalArgumentException(
            IllegalArgumentException ex, WebRequest request) {
        
        logger.warn("Invalid argument provided: {}", ex.getMessage());
        
        Map<String, Object> errorDetails = new HashMap<>();
        errorDetails.put("timestamp", LocalDateTime.now());
        errorDetails.put("message", "Invalid input provided");
        errorDetails.put("details", ex.getMessage());
        errorDetails.put("path", request.getDescription(false));
        errorDetails.put("status", HttpStatus.BAD_REQUEST.value());
        
        return new ResponseEntity<>(errorDetails, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(RuntimeException.class)
    public ResponseEntity<Map<String, Object>> handleRuntimeException(
            RuntimeException ex, WebRequest request) {
        
        logger.error("Runtime error occurred: ", ex);
        
        Map<String, Object> errorDetails = new HashMap<>();
        errorDetails.put("timestamp", LocalDateTime.now());
        errorDetails.put("message", "Service temporarily unavailable");
        errorDetails.put("details", "Please try again later");
        errorDetails.put("path", request.getDescription(false));
        errorDetails.put("status", HttpStatus.SERVICE_UNAVAILABLE.value());
        
        return new ResponseEntity<>(errorDetails, HttpStatus.SERVICE_UNAVAILABLE);
    }

    @ExceptionHandler(SessionExpiredException.class)
    public ResponseEntity<Map<String, Object>> handleSessionExpiredException(
            SessionExpiredException ex, WebRequest request) {
        
        logger.info("Session expired for user: {}", ex.getMessage());
        
        Map<String, Object> errorDetails = new HashMap<>();
        errorDetails.put("timestamp", LocalDateTime.now());
        errorDetails.put("message", "Session expired");
        errorDetails.put("details", "Please log in again");
        errorDetails.put("path", request.getDescription(false));
        errorDetails.put("status", HttpStatus.UNAUTHORIZED.value());
        
        return new ResponseEntity<>(errorDetails, HttpStatus.UNAUTHORIZED);
    }

    // Custom exception for session expiry
    public static class SessionExpiredException extends RuntimeException {
        public SessionExpiredException(String message) {
            super(message);
        }
    }
}

/**
 * Logging Utility for consistent application logging
 */
class LoggingUtil {
    
    public static void logUserAction(Logger logger, String userEmail, String action, String details) {
        logger.info("User Action - Email: {}, Action: {}, Details: {}", userEmail, action, details);
    }
    
    public static void logInterviewStart(Logger logger, String userEmail, String role) {
        logger.info("Interview Started - User: {}, Role: {}", userEmail, role);
    }
    
    public static void logInterviewAnswer(Logger logger, String userEmail, String question, int score) {
        logger.info("Answer Submitted - User: {}, Question: {}, Score: {}", 
                   userEmail, question.substring(0, Math.min(50, question.length())), score);
    }
    
    public static void logPerformanceMetric(Logger logger, String metric, long value) {
        logger.info("Performance Metric - {}: {} ms", metric, value);
    }
    
    public static void logSecurityEvent(Logger logger, String event, String userEmail, String details) {
        logger.warn("Security Event - Event: {}, User: {}, Details: {}", event, userEmail, details);
    }
}