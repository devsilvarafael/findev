package api.findev.repository;

import api.findev.model.CompanyAdmin;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface CompanyAdminRepository extends JpaRepository<CompanyAdmin, UUID> {
    Optional<CompanyAdmin> findByEmail(String email);
}
