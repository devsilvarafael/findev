package api.findev.dto.response;

import api.findev.dto.DeveloperDto;
import api.findev.model.Company;

import java.util.UUID;

public record JobCandidateCompleteDto(
        UUID candidateId,
        UUID jobId,
        String jobDescription,
        CompanyCandidatureDto company,
        DeveloperDto developerDto,
        String status
) {
}
