package api.findev.service.Impl;

import api.findev.dto.response.CandidatureDto;
import api.findev.dto.response.JobCandidateCompleteDto;
import api.findev.enums.CandidatureStatus;
import api.findev.mapper.JobCandidatureDTOMapper;
import api.findev.model.Job;
import api.findev.model.JobCandidature;
import api.findev.repository.CandidatureRepository;
import api.findev.repository.DeveloperRepository;
import api.findev.repository.JobRepository;
import api.findev.service.JobCandidatureService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.UUID;

@Service
public class JobCandidatureServiceImpl implements JobCandidatureService {
    private final DeveloperRepository developerRepository;
    private final CandidatureRepository candidatureRepository;
    private final JobRepository jobRepository;
    private final JobCandidatureDTOMapper jobCandidatureDTOMapper;

    public JobCandidatureServiceImpl(
            DeveloperRepository developerRepository,
            CandidatureRepository candidatureRepository,
            JobRepository jobRepository,
            JobCandidatureDTOMapper jobCandidatureDTOMapper
    ) {
        this.developerRepository = developerRepository;
        this.candidatureRepository = candidatureRepository;
        this.jobRepository = jobRepository;
        this.jobCandidatureDTOMapper = jobCandidatureDTOMapper;
    }

    @Override
    public Page<JobCandidateCompleteDto> findAllDetailedCandidaturesByDeveloper(Pageable pageable, UUID developerId) {
        Page<JobCandidature> candidatures = candidatureRepository.findByDeveloperId(developerId, pageable);

        return candidatures.map(jobCandidatureDTOMapper::applyToCompleteDto);
    }

    @Override
    public Page<CandidatureDto> findAllCandidaturesByJob(Pageable pageable, UUID jobId) {
        Optional<Job> job = jobRepository.findById(jobId);

        if (job.isEmpty()) {
            return Page.empty();
        }

        Page<JobCandidature> candidatures = candidatureRepository.findByJobId(jobId, pageable);

        return candidatures.map(jobCandidatureDTOMapper::apply);
    }

    @Override
    public void updateCandidatureStatus(UUID candidatureId, String status) {
        Optional<JobCandidature> candidatureOpt = candidatureRepository.findById(candidatureId);

        if (candidatureOpt.isEmpty()) {
            throw new IllegalArgumentException("Candidature not found.");
        }

        JobCandidature candidature = candidatureOpt.get();

        try {
            candidature.setStatus(CandidatureStatus.valueOf(status.toUpperCase())); // Enum-based status
        } catch (IllegalArgumentException ex) {
            throw new IllegalArgumentException("Invalid status provided.");
        }

        candidatureRepository.save(candidature);
    }

}
