package pl.mati.taskintelligenceapi.dto.authDto;

public record AuthRegisterRequestDTO(
        String username,
        String email,
        String firstName,
        String lastName,
        String country,
        String password
) {
}
