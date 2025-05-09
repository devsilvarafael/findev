package api.findev.dto.response;

import api.findev.dto.CompanyDto;
import api.findev.dto.RecruiterDto;
import api.findev.enums.ContractType;
import api.findev.enums.JobPriority;
import api.findev.enums.WorkModality;

import java.util.Date;
import java.util.List;
import java.util.UUID;

public record JobResponseDto(
        UUID id,
        String title,
        String description,
        int status,
        double salary,
        Date expirationDate,
        Date createdAt,
        ContractType contractType,
        int minWeekHours,
        int maxWeekHours,
        WorkModality workModality,
        String workLocation,
        CompanyDto company,
        RecruiterDto recruiter,
        JobPriority priority,
        List<JobBenefitDto> benefits,
        List<CandidatureDto> candidatures,
        List<JobRequirementDto> requirements
) {
}
