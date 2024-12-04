package api.findev.service;

import api.findev.dto.response.CandidatureDto;
import api.findev.dto.response.JobCandidateCompleteDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.UUID;

public interface JobCandidatureService {
    Page<JobCandidateCompleteDto> findAllDetailedCandidaturesByDeveloper(Pageable pageable, UUID developerId);
    Page<CandidatureDto> findAllCandidaturesByJob(Pageable pageable, UUID jobId);
    void updateCandidatureStatus(UUID candidatureId, String status);
}
