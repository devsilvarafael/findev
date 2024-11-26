package api.findev.dto.request;

import api.findev.dto.CompanyDto;
import api.findev.dto.UserDto;

public record CompanyAdminRequestDto(
        UserDto user,
        CompanyDto company
) {
}
