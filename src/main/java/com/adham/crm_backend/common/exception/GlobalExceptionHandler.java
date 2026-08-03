package com.adham.crm_backend.common.exception;

import com.adham.crm_backend.auth.exception.InvalidRefreshTokenException;
import com.adham.crm_backend.team.exception.InvalidTeamManagerException;
import com.adham.crm_backend.team.exception.InvalidTeamMemberException;
import com.adham.crm_backend.user.exception.InvalidRoleIdException;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
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
    @ExceptionHandler(InvalidRoleIdException.class)
    public ResponseEntity<ErrorResponse> handleInvalidRoleId(InvalidRoleIdException ex, HttpServletRequest request){
        return buildErrorResponse(HttpStatus.BAD_REQUEST, ex.getMessage(), request);
    }
    @ExceptionHandler(AccessDeniedException.class)
    public ResponseEntity<ErrorResponse> handleAccessDenied(AccessDeniedException ex,HttpServletRequest request){
        return buildErrorResponse(HttpStatus.FORBIDDEN, ex.getMessage(), request);
    }
    @ExceptionHandler(
            BusinessConflictException.class)
    public ResponseEntity<ErrorResponse> handleConflict(BusinessConflictException ex, HttpServletRequest request){
        return buildErrorResponse(HttpStatus.CONFLICT, ex.getMessage(), request);
    }
    @ExceptionHandler(MissingOwnerException.class)
    public ResponseEntity<ErrorResponse> handleMissingOwner(MissingOwnerException ex, HttpServletRequest request){
        return buildErrorResponse(HttpStatus.BAD_REQUEST, ex.getMessage(), request);
    }
    @ExceptionHandler(InvalidTeamManagerException.class)
    public ResponseEntity<ErrorResponse> handleInvalidTeamManager(InvalidTeamManagerException ex, HttpServletRequest request){
        return buildErrorResponse(HttpStatus.BAD_REQUEST,ex.getMessage(),request);
    }
    @ExceptionHandler(InvalidTeamMemberException.class)
    public ResponseEntity<ErrorResponse> handleInvalidTeamManager(InvalidTeamMemberException ex, HttpServletRequest request){
        return buildErrorResponse(HttpStatus.BAD_REQUEST,ex.getMessage(),request);}
    @ExceptionHandler(InvalidCustomerOwnerException.class)
    public ResponseEntity<ErrorResponse> handleInvalidCustomerOwner(InvalidCustomerOwnerException ex, HttpServletRequest request){
        return buildErrorResponse(HttpStatus.BAD_REQUEST,ex.getMessage(),request);
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
