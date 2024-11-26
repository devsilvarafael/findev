package api.findev.dto;

import java.util.List;
import java.util.UUID;

public record CompanyDto(
        UUID companyId,
        String registerNumber,
        String name,
        String address,
        String website,
        String email,
        boolean isActive,
        List<RecruiterDto> recruiters
) {

}
