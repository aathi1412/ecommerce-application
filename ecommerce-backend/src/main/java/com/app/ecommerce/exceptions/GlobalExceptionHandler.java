package com.app.ecommerce.exceptions;

import com.app.ecommerce.dto.ErrorResponse;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.time.Instant;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(UserNotFoundException.class)
    public ResponseEntity<ErrorResponse> handleUserNotFoundException(UserNotFoundException ex,
                                                                     HttpServletRequest request) {
        return ResponseEntity
                .status(HttpStatus.NOT_FOUND)
                .body(buildErrorResponse(
                                ex.getMessage(),
                                HttpStatus.NOT_FOUND,
                                request
                        )
                );
    }

    @ExceptionHandler(ProductNotFoundException.class)
    public ResponseEntity<ErrorResponse> handleProductNotFoundException(ProductNotFoundException ex,
                                                                        HttpServletRequest request) {
        return ResponseEntity
                .status(HttpStatus.NOT_FOUND)
                .body(buildErrorResponse(
                            ex.getMessage(),
                            HttpStatus.NOT_FOUND,
                            request
                        )
                );
    }

    @ExceptionHandler(CartItemNotFoundException.class)
    public ResponseEntity<ErrorResponse> handleCartItemNotFoundException(CartItemNotFoundException ex,
                                                                        HttpServletRequest request) {
        return ResponseEntity
                .status(HttpStatus.NOT_FOUND)
                .body(buildErrorResponse(
                                ex.getMessage(),
                                HttpStatus.NOT_FOUND,
                                request
                        )
                );
    }

    @ExceptionHandler(OutOfStockException.class)
    public ResponseEntity<ErrorResponse> handleOutOfStockException(OutOfStockException ex,
                                                                        HttpServletRequest request) {
        return ResponseEntity
                .status(HttpStatus.CONFLICT)
                .body(buildErrorResponse(
                                ex.getMessage(),
                                HttpStatus.CONFLICT,
                                request
                        )
                );
    }

    @ExceptionHandler(InsufficientStockException.class)
    public ResponseEntity<ErrorResponse> handleInsufficientStockException(InsufficientStockException ex,
                                                                   HttpServletRequest request) {
        return ResponseEntity
                .status(HttpStatus.CONFLICT)
                .body(buildErrorResponse(
                                ex.getMessage(),
                                HttpStatus.CONFLICT,
                                request
                        )
                );
    }

    public ErrorResponse buildErrorResponse(String exception, HttpStatus status, HttpServletRequest request) {
        return ErrorResponse.builder()
                .timeStamp(Instant.now())
                .status(status.value())
                .error(status.getReasonPhrase())
                .message(exception)
                .path(request.getRequestURI())
                .build();
    }
}
