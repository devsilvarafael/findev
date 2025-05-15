package api.findev.service.Impl;

import api.findev.dto.request.JobRequestDto;
import api.findev.dto.response.JobResponseDto;
import api.findev.enums.CandidatureStatus;
import api.findev.mapper.JobDTOMapper;
import api.findev.model.*;
import api.findev.repository.*;
import api.findev.service.JobService;
import jakarta.transaction.Transactional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.Date;
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
    private final DeveloperRepository developerRepository;
    private final JobDTOMapper jobDTOMapper;
    private final CandidatureRepository candidatureRepository;
    private final SkillRepository skillRepository;

    public JobServiceImpl(JobRepository jobRepository, CompanyRepository companyRepository,
                          RecruiterRepository recruiterRepository, DeveloperRepository developerRepository,
                          CandidatureRepository candidatureRepository, JobDTOMapper jobDTOMapper, SkillRepository skillRepository) {
        this.jobRepository = jobRepository;
        this.companyRepository = companyRepository;
        this.recruiterRepository = recruiterRepository;
        this.developerRepository = developerRepository;
        this.candidatureRepository = candidatureRepository;
        this.jobDTOMapper = jobDTOMapper;
        this.skillRepository = skillRepository;
    }

    @Override
    public Page<JobResponseDto> getAllJobs(Pageable pageable) {
        List<JobResponseDto> jobsPage = jobRepository.findAll().stream().map(jobDTOMapper).collect(Collectors.toList());
        return new PageImpl<>(jobsPage);
    }

    @Override
    public Page<JobResponseDto> getAllJobsByCompany(UUID company, Pageable pageable) throws Exception {
        List<JobResponseDto> jobs = jobRepository.findJobsByCompanyId(company, pageable).stream().map(jobDTOMapper).collect(Collectors.toList());
        return new PageImpl<>(jobs);
    }

    @Override
    public Page<JobResponseDto> getAllJobsByRecruiter(UUID recruiter, Pageable pageable) {
        List<JobResponseDto> jobs = jobRepository.findJobsByRecruiterRecruiterId(recruiter, pageable).stream().map(jobDTOMapper).collect(Collectors.toList());
        return new PageImpl<>(jobs);
    }



    @Override
    public Optional<JobResponseDto> getJobById(UUID id) throws Exception {
        Optional<JobResponseDto> jobExists = jobRepository.findById(id).map(jobDTOMapper);
        if (jobExists.isEmpty()) {
            throw new Exception("Job not found.");
        }
        return jobExists;
    }

    @Override
    public JobResponseDto announceNewJob(JobRequestDto jobRequestDto) throws Exception {
        if (jobRequestDto.companyId() == null || jobRequestDto.recruiterId() == null) {
            throw new IllegalArgumentException("Company ID or Recruiter ID cannot be null.");
        }

        Optional<Company> companyOpt = companyRepository.findById(jobRequestDto.companyId());
        if (companyOpt.isEmpty()) {
            throw new Exception("Company not found.");
        }

        Optional<Recruiter> recruiterOpt = recruiterRepository.findById(jobRequestDto.recruiterId());
        if (recruiterOpt.isEmpty()) {
            throw new Exception("Recruiter not found.");
        }

        Job job = new Job();
        job.setTitle(jobRequestDto.title());
        job.setDescription(jobRequestDto.description());
        job.setStatus(jobRequestDto.status());
        job.setSalary(jobRequestDto.salary());
        job.setExpirationDate(jobRequestDto.expirationDate());
        job.setCompany(companyOpt.get());
        job.setRecruiter(recruiterOpt.get());
        job.setCreatedAt(new Date());
        job.setContractType(jobRequestDto.contractType());
        job.setMinWeekHours(jobRequestDto.minWeekHours());
        job.setMaxWeekHours(jobRequestDto.maxWeekHours());
        job.setWorkModality(jobRequestDto.workModality());
        job.setWorkLocation(jobRequestDto.workLocation());
        job.setPriority(jobRequestDto.priority());

        if (jobRequestDto.benefits() != null) {
            jobRequestDto.benefits().forEach(benefitDto -> {
                JobBenefit jobBenefit = new JobBenefit();
                jobBenefit.setBenefit(benefitDto.toString());
                jobBenefit.setJob(job);
                job.getBenefits().add(jobBenefit);
            });
        }

        if (jobRequestDto.requirements() != null) {
            jobRequestDto.requirements().forEach(requirementDto -> {
                if (requirementDto.getName() == null) {
                    throw new IllegalArgumentException("Requirement name cannot be null.");
                }

                if (requirementDto.getExperienceYears() == 0) {
                    throw new IllegalArgumentException("Experience years cannot be zero.");
                }

                Optional<Skill> existingSkill = skillRepository.findByName(requirementDto.getName());

                Skill skill = existingSkill.get();

                JobRequirement jobRequirement = new JobRequirement();
                jobRequirement.setName(requirementDto.getName());
                jobRequirement.setExperienceYears(requirementDto.getExperienceYears());
                jobRequirement.setJob(job);
                jobRequirement.setName(skill.getName());
                job.getRequirements().add(jobRequirement);
            });
        }

        Job newJob = jobRepository.save(job);

        return jobDTOMapper.apply(newJob);
    }


    @Override
    public void deleteJobById(UUID id) throws Exception {
        Optional<Job> jobExists = jobRepository.findById(id);
        if (jobExists.isEmpty()) {
            throw new Exception("Job not found.");
        }
        jobRepository.deleteById(id);
    }

    @Override
    public JobResponseDto updateJob(UUID id, JobRequestDto jobRequestDto) throws Exception {
        Optional<Job> existingJobOpt = jobRepository.findById(id);
        if (existingJobOpt.isEmpty()) {
            throw new Exception("Job not found.");
        }

        Job existingJob = existingJobOpt.get();

        existingJob.setTitle(jobRequestDto.title());
        existingJob.setDescription(jobRequestDto.description());
        existingJob.setStatus(jobRequestDto.status());
        existingJob.setSalary(jobRequestDto.salary());
        existingJob.setExpirationDate(jobRequestDto.expirationDate());
        existingJob.setPriority(jobRequestDto.priority());
        existingJob.setContractType(jobRequestDto.contractType());
        existingJob.setMinWeekHours(jobRequestDto.minWeekHours());
        existingJob.setMaxWeekHours(jobRequestDto.maxWeekHours());
        existingJob.setWorkModality(jobRequestDto.workModality());
        existingJob.setWorkLocation(jobRequestDto.workLocation());


        if (jobRequestDto.companyId() != null) {
            Optional<Company> companyOpt = companyRepository.findById(jobRequestDto.companyId());
            if (companyOpt.isEmpty()) {
                throw new Exception("Company not found.");
            }
            existingJob.setCompany(companyOpt.get());
        }

        if (jobRequestDto.recruiterId() != null) {
            Optional<Recruiter> recruiterOpt = recruiterRepository.findById(jobRequestDto.recruiterId());
            if (recruiterOpt.isEmpty()) {
                throw new Exception("Recruiter not found.");
            }
            existingJob.setRecruiter(recruiterOpt.get());
        }

        existingJob.getBenefits().clear();
        if (jobRequestDto.benefits() != null) {
            jobRequestDto.benefits().forEach(benefitDto -> {
                JobBenefit jobBenefit = new JobBenefit();
                jobBenefit.setBenefit(benefitDto.toString());
                jobBenefit.setJob(existingJob);
                existingJob.getBenefits().add(jobBenefit);
            });
        }

        existingJob.getRequirements().clear();
        if (jobRequestDto.requirements() != null) {
            jobRequestDto.requirements().forEach(requirementDto -> {
                JobRequirement jobRequirement = new JobRequirement();
                jobRequirement.setName(requirementDto.getName());
                jobRequirement.setExperienceYears(requirementDto.getExperienceYears());
                jobRequirement.setJob(existingJob);
                existingJob.getRequirements().add(jobRequirement);
            });
        }

        Job updatedJob = jobRepository.save(existingJob);

        return jobDTOMapper.apply(updatedJob);
    }

    @Override
    public void applyToJob(UUID developerId, UUID jobId) throws Exception {
        Optional<Job> jobOpt = jobRepository.findById(jobId);
        Optional<Developer> developerOpt = developerRepository.findById(developerId);

        if (jobOpt.isEmpty()) {
            throw new Exception("Job not found.");
        }

        if (developerOpt.isEmpty()) {
            throw new Exception("Developer not found.");
        }

        Job job = jobOpt.get();
        Developer developer = developerOpt.get();

        boolean alreadyApplied = candidatureRepository.existsByJobAndDeveloper(job, developer);
        if (alreadyApplied) {
            throw new Exception("Você já se candidatou a essa vaga.");
        }

        JobCandidature candidature = new JobCandidature();
        candidature.setJob(job);
        candidature.setDeveloper(developer);
        candidature.setStatus(CandidatureStatus.PENDING_REVIEW);

        candidatureRepository.save(candidature);
    }


    @Override
    public void unapplyFromJob(UUID developerId, UUID jobId) throws Exception {
        Optional<Job> jobOpt = jobRepository.findById(jobId);
        Optional<Developer> developerOpt = developerRepository.findById(developerId);

        if (jobOpt.isEmpty()) {
            throw new Exception("Job not found.");
        }

        if (developerOpt.isEmpty()) {
            throw new Exception("Developer not found.");
        }

        Job job = jobOpt.get();
        Developer developer = developerOpt.get();

        Optional<JobCandidature> candidatureOpt = candidatureRepository.findByJobAndDeveloper(job, developer);
        if (candidatureOpt.isEmpty()) {
            throw new Exception("Candidature not found.");
        }

        candidatureRepository.delete(candidatureOpt.get());
    }


    @Override
    public Page<JobResponseDto> getMatchingJobsForDeveloper(UUID developerId, Pageable pageable) throws Exception {
        Optional<Developer> developerOpt = developerRepository.findByIdWithSkills(developerId);

        if (developerOpt.isEmpty()) {
            throw new Exception("Developer not found.");
        }

        Developer developer = developerOpt.get();
        List<DeveloperSkill> developerSkills = developer.getSkills();

        List<JobResponseDto> matchingJobs = jobRepository.findAll().stream()
                .filter(job -> job.getRequirements().stream()
                        .allMatch(requirement ->
                                developerSkills.stream().anyMatch(skill ->
                                        skill.getSkill().getName().equalsIgnoreCase(requirement.getName()) &&
                                                skill.getExperienceYears() >= requirement.getExperienceYears())))
                .map(jobDTOMapper)
                .collect(Collectors.toList());

        return new PageImpl<>(matchingJobs, pageable, matchingJobs.size());
    }
}
