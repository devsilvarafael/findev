package api.findev.repository;

import api.findev.dto.RecruiterDto;
import api.findev.model.Company;
import api.findev.model.Recruiter;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;
import java.util.UUID;

public interface RecruiterRepository extends JpaRepository<Recruiter, UUID> {
    Page<RecruiterDto> findRecruitersByCompanyId(UUID companyId, Pageable pageable);
    Optional<RecruiterDto> findRecruiterByEmail(String email);
}
