package pl.mati.taskintelligenceapi.exception;

import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import jakarta.persistence.EntityNotFoundException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import org.springframework.security.access.AccessDeniedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import pl.mati.taskintelligenceapi.dto.RestResponse;

@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {

    @ApiResponse(
            responseCode = "404",
            description = "Entity not found",
            content = @Content(schema = @Schema(implementation = RestResponse.class))
    )
    @ExceptionHandler(EntityNotFoundException.class)
    public ResponseEntity<RestResponse<Void>> handleErrorNotFoundException(EntityNotFoundException ex){
        log.warn("Resource not found: {}", ex.getMessage());
        return ResponseEntity
                .status(HttpStatus.NOT_FOUND)
                .body(RestResponse.error(HttpStatus.NOT_FOUND.value(), ex.getMessage()));

    }
    @ApiResponse(
            responseCode = "400",
            description = "Validation error",
            content = @Content(schema = @Schema(implementation = RestResponse.class))
    )
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<RestResponse<Void>> handleErrorValidationException(MethodArgumentNotValidException ex) {
        log.warn("Validation error: {}", ex.getMessage());
       String errorMessage = ex.getBindingResult().getFieldErrors().stream()
               .map(er -> er.getDefaultMessage())
               .findFirst()
               .orElse("Validation error occurred");

       return ResponseEntity
               .status(HttpStatus.BAD_REQUEST)
               .body(RestResponse.error(HttpStatus.BAD_REQUEST.value(), errorMessage));
    }

    @ApiResponse(
            responseCode = "500",
            description = "Internal server error",
            content = @Content(schema = @Schema(implementation = RestResponse.class))
    )
    @ExceptionHandler(Exception.class)
    public ResponseEntity<RestResponse<Void>> handleErrorGeneralException(Exception ex) {
        log.error("Unhandled exception caught: ", ex);
        return ResponseEntity
                .status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(RestResponse.error(HttpStatus.INTERNAL_SERVER_ERROR.value(), "Internal server error"));
    }

    @ApiResponse(
            responseCode = "403",
            description = "Access denied",
            content = @Content(schema = @Schema(implementation = RestResponse.class))
    )
    @ExceptionHandler(AccessDeniedException.class)
    public ResponseEntity<RestResponse<Void>> handleAccessDeniedException(Exception ex){
        log.warn("Access denied: {}", ex.getMessage());
        return ResponseEntity
                .status(HttpStatus.FORBIDDEN)
                .body(RestResponse.error(HttpStatus.FORBIDDEN.value(), ex.getMessage()));
    }
}
