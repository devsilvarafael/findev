package api.findev.service.Impl;

import api.findev.dto.RecruiterDto;
import api.findev.dto.request.CompanyAdminRequestDto;
import api.findev.dto.response.JobResponseDto;
import api.findev.enums.UserType;
import api.findev.model.Company;
import api.findev.model.CompanyAdmin;
import api.findev.model.User;
import api.findev.repository.CompanyAdminRepository;
import api.findev.repository.CompanyRepository;
import api.findev.repository.UserRepository;
import api.findev.service.CompanyAdminService;
import api.findev.service.JobService;
import api.findev.service.RecruiterService;
import api.findev.service.UserService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.UUID;

@Service
public class CompanyAdminServiceImpl implements CompanyAdminService {
    private final RecruiterService recruiterService;
    private final JobService jobService;
    private final UserService userService;
    private final CompanyAdminRepository companyAdminRepository;
    private final CompanyRepository companyRepository;


    public CompanyAdminServiceImpl(RecruiterService recruiterService, JobService jobService, UserService userService, CompanyAdminRepository companyAdminRepository, CompanyRepository companyRepository) {
        this.recruiterService = recruiterService;
        this.jobService = jobService;
        this.companyAdminRepository = companyAdminRepository;
        this.companyRepository = companyRepository;
        this.userService = userService;
    }

    @Override
    public Page<RecruiterDto> getRecruitersByCompany(UUID companyId, Pageable pageable) {
        return recruiterService.getAllRecruitersByCompanyId(companyId, pageable);
    }

    @Override
    public Page<JobResponseDto> getJobsByCompany(UUID companyId, Pageable pageable) throws Exception {
        return jobService.getAllJobsByCompany(companyId, pageable);
    }

    @Override
    public CompanyAdmin createCompanyAdmin(CompanyAdminRequestDto companyAdminRequestDto) {
        Optional<CompanyAdmin> existsCompanyAdmin = companyAdminRepository.findByEmail(companyAdminRequestDto.email());
        if (existsCompanyAdmin.isPresent()) {
            throw new RuntimeException("Company admin already exists with this email");
        }

        Optional<Company> optionalCompany = companyRepository.findById(companyAdminRequestDto.companyId());
        if (optionalCompany.isEmpty()) {
            throw new RuntimeException("Company not found with ID: " + companyAdminRequestDto.companyId());
        }

        User user = new User();
        user.setEmail(companyAdminRequestDto.email());
        user.setPassword(companyAdminRequestDto.password());
        user.setRole(UserType.ADMINISTRATOR);
        user.setActive(true);
        User savedUser = userService.save(user);

        CompanyAdmin companyAdmin = new CompanyAdmin();
        companyAdmin.setUser(savedUser);
        companyAdmin.setCompany(optionalCompany.get());
        companyAdmin.setEmail(companyAdminRequestDto.email());
        companyAdmin.setPassword(companyAdminRequestDto.password());

        return companyAdminRepository.save(companyAdmin);
    }


}
