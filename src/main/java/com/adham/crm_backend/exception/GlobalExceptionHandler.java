package com.adham.crm_backend.exception;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.time.LocalDateTime;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler({
            InvalidRefreshTokenException.class,
            BadCredentialsException.class
    })
    public ResponseEntity<ErrorResponse> handleUnauthorized(
            Exception ex,
            HttpServletRequest request) {

        return buildErrorResponse(
                HttpStatus.UNAUTHORIZED,
                ex.getMessage(),
                request
        );
    }
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ErrorResponse> handleArgumentNotValid(MethodArgumentNotValidException ex,
                                                                HttpServletRequest request){
        return buildErrorResponse(
                HttpStatus.BAD_REQUEST,
                ex.getBindingResult().getFieldErrors().get(0).getDefaultMessage(),
                request
        );

    }

    @ExceptionHandler(ResourceNotFoundException.class)
    public ResponseEntity<ErrorResponse> handleResource(ResourceNotFoundException ex,
                                                        HttpServletRequest request){
        return buildErrorResponse(HttpStatus.NOT_FOUND, ex.getMessage(), request);
    }

    @ExceptionHandler(UserAlreadyExistsException.class)
    public ResponseEntity<ErrorResponse> handleUserAlreadyExists(UserAlreadyExistsException ex,
                                                                 HttpServletRequest request){
        return buildErrorResponse(HttpStatus.CONFLICT, ex.getMessage(), request);
    }
    @ExceptionHandler(InvalidRoleIdException.class)
    public ResponseEntity<ErrorResponse> handleInvalidRoleId(InvalidRoleIdException ex, HttpServletRequest request){
        return buildErrorResponse(HttpStatus.BAD_REQUEST, ex.getMessage(), request);
    }

    private ResponseEntity<ErrorResponse>  buildErrorResponse(
            HttpStatus status,
            String message,
            HttpServletRequest request
    ){
        return ResponseEntity.status(status).body(new ErrorResponse(LocalDateTime.now(),status.value(),message,request.getRequestURI()));
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorResponse> handleException(
            Exception ex,
            HttpServletRequest request
    ) {

        // TODO: Replace with Logger
        ex.printStackTrace();

        return buildErrorResponse(
                HttpStatus.INTERNAL_SERVER_ERROR,
                "An unexpected error occurred.",
                request
        );
    }


}
