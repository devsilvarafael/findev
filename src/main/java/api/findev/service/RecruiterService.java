package api.findev.service;

import api.findev.dto.RecruiterDto;
import api.findev.model.Company;
import api.findev.model.Recruiter;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Optional;
import java.util.UUID;

public interface RecruiterService {

    Page<RecruiterDto> getAllRecruiters(Pageable pageable);
    Page<RecruiterDto> getAllRecruitersByCompany(Company company, Pageable pageable);

    RecruiterDto createRecruiter(Recruiter recruiterDto);
}
