package api.findev.service;

import api.findev.dto.CompanyDto;
import api.findev.model.Company;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.UUID;

public interface CompanyService {

    Page<CompanyDto> getAllCompanies(Pageable pageable);
    CompanyDto createCompany(Company company);

    void deleteCompany(UUID id);

    CompanyDto updateCompany(UUID id, Company company);
}
