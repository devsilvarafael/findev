package api.findev.mapper;

import api.findev.dto.CompanyDto;
import api.findev.dto.DeveloperDto;
import api.findev.dto.response.CandidatureDto;
import api.findev.dto.response.CompanyCandidatureDto;
import api.findev.dto.response.JobCandidateCompleteDto;
import api.findev.model.Company;
import api.findev.model.Developer;
import api.findev.model.JobCandidature;
import org.springframework.stereotype.Service;

import java.util.stream.Collectors;

@Service
public class JobCandidatureDTOMapper {

    private final DeveloperSkillDTOMapper developerSkillDTOMapper;

    public JobCandidatureDTOMapper(DeveloperSkillDTOMapper developerSkillDTOMapper) {
        this.developerSkillDTOMapper = developerSkillDTOMapper;
    }

    public CandidatureDto apply(JobCandidature jobCandidature) {
        Developer developer = jobCandidature.getDeveloper();

        DeveloperDto developerDto = new DeveloperDto(
                developer.getId(),
                developer.getFirstName(),
                developer.getLastName(),
                developer.getEmail(),
                developer.getPhone(),
                developer.getPortfolio(),
                developer.getSeniority(),
                developer.getSkills().stream()
                        .map(developerSkillDTOMapper)
                        .collect(Collectors.toList())
        );

        return new CandidatureDto(jobCandidature.getId(), developerDto, jobCandidature.getStatus().toString());
    }

    public JobCandidateCompleteDto applyToCompleteDto(JobCandidature jobCandidature) {
        Company company = jobCandidature.getJob().getCompany();
        if (company == null) {
            throw new IllegalStateException("Company is not initialized");
        }

        Developer developer = jobCandidature.getDeveloper();

        DeveloperDto developerDto = new DeveloperDto(
                developer.getId(),
                developer.getFirstName(),
                developer.getLastName(),
                developer.getEmail(),
                developer.getPhone(),
                developer.getPortfolio(),
                developer.getSeniority(),
                developer.getSkills().stream()
                        .map(developerSkillDTOMapper)
                        .collect(Collectors.toList())
        );

        return new JobCandidateCompleteDto(
                jobCandidature.getId(),
                new CompanyCandidatureDto(company.getName(), company.getIsActive()), // Avoid full entity
                developerDto,
                jobCandidature.getStatus().toString()
        );
    }

}
