package api.findev.service;

import api.findev.dto.RecruiterDto;
import api.findev.dto.request.CompanyAdminRequestDto;
import api.findev.dto.response.JobResponseDto;
import api.findev.model.CompanyAdmin;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.UUID;

public interface CompanyAdminService {

    Page<RecruiterDto> getRecruitersByCompany(UUID companyId, Pageable pageable);
    Page<JobResponseDto> getJobsByCompany(UUID companyId, Pageable pageable) throws Exception;
    CompanyAdmin createCompanyAdmin(CompanyAdminRequestDto companyAdmin);
}
