package api.findev.repository;

import api.findev.dto.RecruiterDto;
import api.findev.model.Company;
import api.findev.model.Recruiter;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface RecruiterRepository extends JpaRepository<Recruiter, UUID> {

    Page<RecruiterDto> findRecruiterByCompany(Company company, Pageable pageable);
}
