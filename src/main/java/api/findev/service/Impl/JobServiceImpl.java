package api.findev.service.Impl;

import api.findev.dto.CompanyDto;
import api.findev.dto.JobDto;
import api.findev.dto.RecruiterDto;
import api.findev.mapper.JobDTOMapper;
import api.findev.model.Company;
import api.findev.model.Job;
import api.findev.model.Recruiter;
import api.findev.repository.CompanyRepository;
import api.findev.repository.JobRepository;
import api.findev.repository.RecruiterRepository;
import api.findev.service.JobService;
import jakarta.transaction.Transactional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@Transactional
public class JobServiceImpl implements JobService {

    private final JobRepository jobRepository;
    private final CompanyRepository companyRepository;
    private final RecruiterRepository recruiterRepository;
    private final JobDTOMapper jobDTOMapper;


    public JobServiceImpl(JobRepository jobRepository, CompanyRepository companyRepository, RecruiterRepository recruiterRepository, JobDTOMapper jobDTOMapper) {
        this.jobRepository = jobRepository;
        this.companyRepository = companyRepository;
        this.recruiterRepository = recruiterRepository;
        this.jobDTOMapper = jobDTOMapper;
    }

    @Override
    public Page<JobDto> getAllJobs(Pageable pageable) {
        List<JobDto> jobsPage = jobRepository.findAll().stream().map(jobDTOMapper).collect(Collectors.toList());

        return new PageImpl<>(jobsPage);
    }

    @Override
    public Page<JobDto> getAllJobsByCompany(UUID company, Pageable pageable) throws Exception {
        List<JobDto> jobs = jobRepository.findJobsByCompany(company, pageable).stream().map(jobDTOMapper).collect(Collectors.toList());

        return new PageImpl<>(jobs);
    }


    @Override
    public Page<JobDto> getAllJobsByRecruiter(UUID recruiter, Pageable pageable) {
        List<JobDto> jobs = jobRepository.findJobsByRecruiter(recruiter, pageable).stream().map(jobDTOMapper).collect(Collectors.toList());

        return new PageImpl<>(jobs);
    }

    @Override
    public Optional<JobDto> getJobById(UUID id) throws Exception {
        Optional<JobDto> jobExists = jobRepository.findById(id).map(jobDTOMapper);

        if(jobExists.isEmpty()) {
            throw new Exception("Vaga não encontrada.");
        }


        return jobExists;
    }

    @Override
    public JobDto announceNewJob(Job job) {
        job.getBenefits().forEach(jobBenefit -> jobBenefit.setJob(job));
        Job newJob = jobRepository.save(job);

        return jobDTOMapper.apply(newJob);
    }

    @Override
    public void deleteJobById(UUID id) throws Exception {
        Optional<Job> jobExists = jobRepository.findById(id);
        if(jobExists.isEmpty()) {
            throw new Exception("Job not found.");
        }
        jobRepository.deleteById(id);
    }
    @Override
    public JobDto updateJob(UUID id, Job job) throws Exception {
        Optional<Job> existingJobOpt = jobRepository.findById(id);
        if (existingJobOpt.isEmpty()) {
            throw new Exception("Job not found.");
        }

        Job existingJob = existingJobOpt.get();

        existingJob.setTitle(job.getTitle());
        existingJob.setDescription(job.getDescription());
        existingJob.setStatus(job.getStatus());
        existingJob.setSalary(job.getSalary());
        existingJob.setExpirationDate(job.getExpirationDate());


        if (job.getCompany() != null) {
            Optional<Company> companyOpt = companyRepository.findById(job.getCompany());
            if (companyOpt.isEmpty()) {
                throw new Exception("Company not found.");
            }
            existingJob.setCompany(companyOpt.get().getId());
        }


        if (job.getRecruiter() != null) {
            Optional<Recruiter> recruiterOpt = recruiterRepository.findById(job.getRecruiter());
            if (recruiterOpt.isEmpty()) {
                throw new Exception("Recruiter not found.");
            }
            existingJob.setRecruiter(recruiterOpt.get().getId());
        }


        existingJob.getBenefits().clear();
        if (job.getBenefits() != null) {
            existingJob.getBenefits().addAll(job.getBenefits());
        }

        Job updatedJob = jobRepository.save(existingJob);
        return jobDTOMapper.apply(updatedJob);
    }


}
