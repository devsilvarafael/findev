package api.findev.dto.response;

import api.findev.dto.DeveloperDto;

import java.util.UUID;

public record CandidatureDto(
        UUID candidatureId,
        DeveloperDto developer,
        String status
) {
}
