package api.findev.repository;

import api.findev.dto.CompanyDto;
import api.findev.model.Company;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.UUID;

public interface CompanyRepository extends JpaRepository<Company, UUID> {
    Optional<Company> findCompaniesByRegistrationNumber(String registrationNumber);
}
