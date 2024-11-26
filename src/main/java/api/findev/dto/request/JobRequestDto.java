package api.findev.dto.request;

import api.findev.enums.ContractType;
import api.findev.enums.WorkModality;

import java.util.Date;
import java.util.List;
import java.util.UUID;

public record JobRequestDto(
        String title,
        String description,
        int status,
        double salary,
        Date expirationDate,
        UUID companyId,
        UUID recruiterId,
        ContractType contractType,
        int minWeekHours,
        int maxWeekHours,
        WorkModality workModality,
        String workLocation,
        List<JobBenefitRequestDto> benefits,
        List<UUID> requirements,
        List<UUID> candidates
) {
}
