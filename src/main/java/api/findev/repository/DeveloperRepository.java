package api.findev.repository;

import api.findev.dto.DeveloperDto;
import api.findev.model.Developer;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.UUID;

public interface DeveloperRepository extends JpaRepository<Developer, UUID> {
    Optional<Developer> findDeveloperByEmail(String email);
}
