package api.findev.mapper;

import api.findev.dto.CompanyDto;
import api.findev.dto.DeveloperDto;
import api.findev.dto.RecruiterDto;
import api.findev.dto.response.JobBenefitDto;
import api.findev.dto.response.JobRequirementDto;
import api.findev.dto.response.JobResponseDto;
import api.findev.dto.response.SkillExperienceDto;
import api.findev.model.*;
import api.findev.repository.CompanyRepository;
import api.findev.repository.RecruiterRepository;
import org.springframework.stereotype.Service;

import java.util.function.Function;
import java.util.stream.Collectors;

@Service
public class JobDTOMapper implements Function<Job, JobResponseDto> {

    private final RecruiterRepository recruiterRepository;
    private final CompanyRepository companyRepository;

    public JobDTOMapper(RecruiterRepository recruiterRepository, CompanyRepository companyRepository) {
        this.recruiterRepository = recruiterRepository;
        this.companyRepository = companyRepository;
    }

    @Override
    public JobResponseDto apply(Job job) {
        CompanyDto companyDto = companyRepository.findById(job.getCompany().getId())
                .map(company -> new CompanyDto(
                        company.getId(),
                        null,
                        company.getName(),
                        company.getAddress(),
                        company.getWebsite(),
                        company.getEmail(),
                        company.getIsActive(),
                        null
                ))
                .orElseThrow(() -> new IllegalStateException("Company not found for Job ID: " + job.getId()));

        RecruiterDto recruiterDto = recruiterRepository.findById(job.getRecruiter().getRecruiterId())
                .map(recruiter -> new RecruiterDto(
                        recruiter.getRecruiterId(),
                        recruiter.getFirstName(),
                        recruiter.getLastName(),
                        recruiter.getEmail(),
                        recruiter.getPhone(),
                        recruiter.getCompany().getId()
                ))
                .orElseThrow(() -> new IllegalStateException("Recruiter not found for Job ID: " + job.getId()));

        return new JobResponseDto(
                job.getId(),
                job.getTitle(),
                job.getDescription(),
                job.getStatus(),
                job.getSalary(),
                job.getExpirationDate(),
                job.getContractType(),
                job.getMinWeekHours(),
                job.getMaxWeekHours(),
                job.getWorkModality(),
                job.getWorkLocation(),
                companyDto,
                recruiterDto,
                job.getBenefits().stream()
                        .map(this::mapToJobBenefitDto)
                        .collect(Collectors.toList()),
                job.getCandidates().stream()
                        .map(this::mapToDeveloperDto)
                        .collect(Collectors.toList()),
                job.getRequirements().stream()
                        .map(this::mapToRequirementDto)
                        .collect(Collectors.toList())
        );
    }

    private JobBenefitDto mapToJobBenefitDto(JobBenefit jobBenefit) {
        return new JobBenefitDto(jobBenefit.getBenefit());
    }

    private DeveloperDto mapToDeveloperDto(Developer developer) {
        return new DeveloperDto(
                developer.getId(),
                developer.getFirstName(),
                developer.getLastName(),
                developer.getEmail(),
                developer.getPhone(),
                developer.getPortfolio(),
                developer.getSeniority(),
                developer.getSkills().stream()
                        .map(this::mapToSkillExperienceDto)
                        .collect(Collectors.toList())
                );
    }

    private JobRequirementDto mapToRequirementDto(JobRequirement jobRequirement) {
        return new JobRequirementDto(jobRequirement.getName(), jobRequirement.getExperienceYears());
    }

    private SkillExperienceDto mapToSkillExperienceDto(DeveloperSkill developerSkill) {
        return new SkillExperienceDto(
                developerSkill.getSkill().getId(),
                developerSkill.getSkill().getName(),
                developerSkill.getExperienceYears()
        );
    }

    @Override
    public <V> Function<V, JobResponseDto> compose(Function<? super V, ? extends Job> before) {
        return Function.super.compose(before);
    }

    @Override
    public <V> Function<Job, V> andThen(Function<? super JobResponseDto, ? extends V> after) {
        return Function.super.andThen(after);
    }
}
