package api.findev.service;

import api.findev.dto.request.JobRequestDto;
import api.findev.dto.response.JobResponseDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Optional;
import java.util.UUID;

public interface JobService {

    Page<JobResponseDto> getAllJobs(Pageable pageable);
    Page<JobResponseDto> getAllJobsByCompany(UUID company, Pageable pageable) throws Exception;
    Page<JobResponseDto> getAllJobsByRecruiter(UUID recruiter, Pageable pageable);

    Optional<JobResponseDto> getJobById(UUID id) throws Exception;

    JobResponseDto announceNewJob(JobRequestDto jobRequestDto) throws Exception;

    void deleteJobById(UUID id) throws Exception;

    JobResponseDto updateJob(UUID id, JobRequestDto jobRequestDto) throws Exception;

    JobResponseDto addCandidateToJob(UUID developerId, UUID jobId) throws Exception;
}
