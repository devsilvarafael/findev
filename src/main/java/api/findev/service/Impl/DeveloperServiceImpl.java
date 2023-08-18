package api.findev.service.Impl;

import api.findev.dto.DeveloperDto;
import api.findev.exceptions.DeveloperNotFoundException;
import api.findev.mapper.DeveloperDTOMapper;
import api.findev.model.Developer;
import api.findev.repository.DeveloperRepository;
import api.findev.service.DeveloperService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class DeveloperServiceImpl implements DeveloperService {

    private final DeveloperRepository developerRepository;
    private final DeveloperDTOMapper developerDTOMapper;

    public DeveloperServiceImpl(DeveloperRepository developerRepository, DeveloperDTOMapper developerDTOMapper) {
        this.developerRepository = developerRepository;
        this.developerDTOMapper = developerDTOMapper;
    }

    @Override
    public Page<DeveloperDto> getAllDevelopers(Pageable pageable) {
        List<DeveloperDto> list = developerRepository.findAll(pageable).stream().map(developerDTOMapper).collect(Collectors.toList());

        return new PageImpl<>(list);
    }

    @Override
    public Optional<DeveloperDto> getDeveloperById(UUID id) {
        Optional<DeveloperDto> developerExists = developerRepository.findById(id).map(developerDTOMapper);

        if(developerExists.isEmpty()) {
            throw new DeveloperNotFoundException("Developer not found");
        }

        return developerExists;
    }

    @Override
    public void deleteById(UUID id) throws DeveloperNotFoundException {
        Developer developer = developerRepository.findById(id)
                .orElseThrow(() -> new DeveloperNotFoundException("Delete failed. Developer not found"));

        developerRepository.delete(developer);
    }

    @Override
    public DeveloperDto create(Developer developer) {
        Developer savedDeveloper = developerRepository.save(developer);
        return developerDTOMapper.apply(savedDeveloper);
    }
}
