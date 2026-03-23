package pl.mati.taskintelligenceapi.dto.authDto;

import jakarta.validation.Valid;

public record AuthRequestDTO(
        @Valid String username,
        String password
) {
}
