package api.findev.repository;

import api.findev.dto.DeveloperDto;
import api.findev.model.Developer;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;
import java.util.UUID;

public interface DeveloperRepository extends JpaRepository<Developer, UUID> {
    Optional<Developer> findDeveloperByEmail(String email);

    @Query("SELECT d FROM Developer d LEFT JOIN FETCH d.skills WHERE d.id = :developerId")
    Optional<Developer> findByIdWithSkills(@Param("developerId") UUID developerId);

}
