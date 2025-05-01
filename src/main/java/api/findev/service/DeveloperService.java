package api.findev.service;

import api.findev.dto.DeveloperDto;
import api.findev.dto.request.DeveloperWithSkillsDto;
import api.findev.dto.response.SkillExperienceDto;
import api.findev.exceptions.DeveloperNotFoundException;
import api.findev.model.Developer;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface DeveloperService {
    Page<DeveloperDto> getAllDevelopers(Pageable pageable);
    Optional<DeveloperDto> getDeveloperById(UUID id) throws DeveloperNotFoundException;;

    void deleteById(UUID id) throws DeveloperNotFoundException;

    DeveloperDto updateDeveloper(UUID id, DeveloperWithSkillsDto developer);

    @Query("SELECT d FROM Developer d LEFT JOIN FETCH d.skills ds LEFT JOIN FETCH ds.skill WHERE d.id = :developerId")
    Optional<Developer> findByIdWithSkills(UUID developerId);

    DeveloperDto create(Developer developer, List<SkillExperienceDto> skillsWithExperience);
}
