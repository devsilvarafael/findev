package api.findev.dto;

import java.util.UUID;

public record CompanyDto(
    UUID companyId,
    String name,
    String address,
    String website,
    String email

) {

}