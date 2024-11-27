package api.findev.service;

import api.findev.dto.DeveloperDto;
import api.findev.exceptions.DeveloperNotFoundException;
import api.findev.model.Developer;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Optional;
import java.util.UUID;

public interface DeveloperService {
    Page<DeveloperDto> getAllDevelopers(Pageable pageable);
    Optional<DeveloperDto> getDeveloperById(UUID id) throws DeveloperNotFoundException;;

    void deleteById(UUID id) throws DeveloperNotFoundException;

    DeveloperDto updateDeveloper(UUID id, Developer developer);

    DeveloperDto create(Developer developer);
}
