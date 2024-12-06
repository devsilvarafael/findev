package api.findev.dto.request;

import api.findev.dto.CompanyDto;
import api.findev.dto.UserDto;
import api.findev.enums.UserType;

import java.util.UUID;

public record CompanyAdminRequestDto(
        String email,
        String password,
        UUID companyId
) {
}
