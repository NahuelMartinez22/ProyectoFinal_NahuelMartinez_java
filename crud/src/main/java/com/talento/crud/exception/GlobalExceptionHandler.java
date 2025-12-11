package com.talento.crud.exception;

import com.fasterxml.jackson.databind.exc.InvalidFormatException;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import static com.talento.crud.exception.CategoryExceptions.*;
import static com.talento.crud.exception.ProductExceptions.*;
import static com.talento.crud.exception.CartExceptions.*;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler({
            CategoryNotFoundException.class,
            ProductNotFoundException.class,
            CartNotFoundException.class,
            CartItemNotFoundException.class
    })
    public ResponseEntity<ApiError> handleNotFound(
            RuntimeException ex,
            HttpServletRequest request) {

        ApiError error = new ApiError(
                HttpStatus.NOT_FOUND.value(),
                ex.getMessage(),
                request.getRequestURI()
        );
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(error);
    }


    @ExceptionHandler({
            CategoryAlreadyExistsException.class,
            ProductAlreadyExistsException.class
    })
    public ResponseEntity<ApiError> handleAlreadyExists(
            RuntimeException ex,
            HttpServletRequest request) {

        ApiError error = new ApiError(
                HttpStatus.CONFLICT.value(),
                ex.getMessage(),
                request.getRequestURI()
        );
        return ResponseEntity.status(HttpStatus.CONFLICT).body(error);
    }

    @ExceptionHandler({
            CategoryInUseException.class,
            ProductInUseException.class,
            CartInvalidStateException.class,
            IllegalArgumentException.class
    })
    public ResponseEntity<ApiError> handleBadRequest(
            RuntimeException ex,
            HttpServletRequest request) {

        ApiError error = new ApiError(
                HttpStatus.BAD_REQUEST.value(),
                ex.getMessage(),
                request.getRequestURI()
        );
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(error);
    }


    @ExceptionHandler(Exception.class)
    public ResponseEntity<ApiError> handleGenericException(
            Exception ex,
            HttpServletRequest request) {

        ApiError error = new ApiError(
                HttpStatus.INTERNAL_SERVER_ERROR.value(),
                "Ocurrió un error inesperado",
                request.getRequestURI()
        );
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(error);
    }

    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ResponseEntity<ApiError> handleHttpMessageNotReadable(
            HttpMessageNotReadableException ex,
            HttpServletRequest request) {

        String message = "Formato de datos inválido.";

        Throwable cause = ex.getCause();
        if (cause instanceof InvalidFormatException ife) {
            String fieldPath = ife.getPath().stream()
                    .map(ref -> ref.getFieldName())
                    .filter(n -> n != null && !n.isBlank())
                    .reduce((a, b) -> a + "." + b)
                    .orElse(null);

            if (fieldPath != null) {
                String expectedType = ife.getTargetType() != null
                        ? ife.getTargetType().getSimpleName()
                        : "tipo esperado";

                if (expectedType.equalsIgnoreCase("Integer")
                        || expectedType.equalsIgnoreCase("Long")
                        || expectedType.equalsIgnoreCase("Double")
                        || expectedType.equalsIgnoreCase("BigDecimal")) {

                    message = "El campo '" + fieldPath + "' debe ser numérico.";
                } else {
                    message = "El campo '" + fieldPath + "' tiene un formato inválido. Se esperaba: " + expectedType;
                }
            }
        }

        ApiError error = new ApiError(
                HttpStatus.BAD_REQUEST.value(),
                message,
                request.getRequestURI()
        );
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(error);
    }

}
