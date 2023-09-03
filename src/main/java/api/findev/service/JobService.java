package api.findev.service;

import api.findev.dto.CompanyDto;
import api.findev.dto.JobDto;
import api.findev.dto.RecruiterDto;
import api.findev.model.Company;
import api.findev.model.Job;
import api.findev.model.Recruiter;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Optional;
import java.util.UUID;

public interface JobService {

    Page<JobDto> getAllJobs(Pageable pageable);
    Page<JobDto> getAllJobsByCompany(UUID company, Pageable pageable) throws Exception;
    Page<JobDto> getAllJobsByRecruiter(UUID recruiter, Pageable pageable);

    Optional<JobDto> getJobById(UUID id) throws Exception;
    JobDto announceNewJob(Job job);
}
