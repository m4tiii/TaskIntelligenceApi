package pl.mati.taskintelligenceapi.dto.authDto;

public record AuthResponseDTO(
        String accessToken,
        String refreshToken
) {
}
