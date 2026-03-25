package pl.mati.taskintelligenceapi.dto;

import java.time.LocalDateTime;

public record RestResponse<T>(
        T data,
        int status,
        String message,
        LocalDateTime timestamp
) {
    public static <T> RestResponse<T> success(T data){
        return new RestResponse<>(data, 200, "Success", LocalDateTime.now());
    }

    public static <T> RestResponse<T> error(int status, String message){
        return  new RestResponse<>(null, status, message, LocalDateTime.now());
    }
}
