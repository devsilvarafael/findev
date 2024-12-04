package api.findev.dto.response;

import api.findev.dto.DeveloperDto;

public record CandidatureDto(
        Long candidatureId,
        DeveloperDto developer,
        String status
) {
}
