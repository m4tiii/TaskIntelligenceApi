package pl.mati.taskintelligenceapi.dto;

public record AuthResponseDTO(
        String accessToken,
        String refreshToken
) {
}
