package api.findev.dto;

import api.findev.model.Company;
import api.findev.model.Recruiter;

import java.util.Date;
import java.util.List;

public record JobDto(
        String title,
        String description,
        int status,
        double salary,
        Date expirationDate,
        Company company,
        Recruiter recruiter,
        List<JobBenefitDto> benefits

){
}
