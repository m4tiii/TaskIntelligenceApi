package pl.mati.taskintelligenceapi.dto;

import java.time.OffsetDateTime;

public record RestResponse<T>(
        T data,
        int status,
        String message,
        OffsetDateTime timestamp
) {
    public static <T> RestResponse<T> success(T data){
        return new RestResponse<>(data, 200, "Success", OffsetDateTime.now());
    }

    public static <T> RestResponse<T> error(int status, String message){
        return new RestResponse<>(null, status, message, OffsetDateTime.now());
    }
}
