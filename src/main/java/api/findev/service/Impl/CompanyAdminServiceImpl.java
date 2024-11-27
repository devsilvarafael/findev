package api.findev.service.Impl;

import api.findev.dto.RecruiterDto;
import api.findev.dto.response.JobResponseDto;
import api.findev.repository.CompanyAdminRepository;
import api.findev.repository.JobRepository;
import api.findev.repository.RecruiterRepository;
import api.findev.service.CompanyAdminService;
import api.findev.service.JobService;
import api.findev.service.RecruiterService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class CompanyAdminServiceImpl implements CompanyAdminService {
    private final CompanyAdminRepository companyAdminRepository;
    private final RecruiterService recruiterService;
    private final JobService jobService;

    public CompanyAdminServiceImpl(CompanyAdminRepository companyAdminRepository, RecruiterService recruiterService, JobService jobService) {
        this.companyAdminRepository = companyAdminRepository;
        this.recruiterService = recruiterService;
        this.jobService = jobService;
    }

    @Override
    public Page<RecruiterDto> getRecruitersByCompany(UUID companyId, Pageable pageable) {
        return recruiterService.getAllRecruitersByCompanyId(companyId, pageable);
    }

    @Override
    public Page<JobResponseDto> getJobsByCompany(UUID companyId, Pageable pageable) throws Exception {
        return jobService.getAllJobsByCompany(companyId, pageable);
    }
}
