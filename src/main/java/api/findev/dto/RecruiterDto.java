package api.findev.dto;

import api.findev.model.Company;

import java.util.UUID;

public record RecruiterDto(
    UUID recruiterId,
    String firstName,
    String lastName,
    String email,
    String phone,
    UUID companyId
){

}
