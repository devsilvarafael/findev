package api.findev.service.Impl;

import api.findev.dto.DeveloperDto;
import api.findev.dto.request.DeveloperWithSkillsDto;
import api.findev.dto.response.SkillExperienceDto;
import api.findev.enums.UserType;
import api.findev.exceptions.DeveloperNotFoundException;
import api.findev.mapper.DeveloperDTOMapper;
import api.findev.model.Developer;
import api.findev.model.DeveloperSkill;
import api.findev.model.Skill;
import api.findev.model.User;
import api.findev.repository.DeveloperRepository;
import api.findev.repository.DeveloperSkillRepository;
import api.findev.repository.SkillRepository;
import api.findev.service.DeveloperService;
import api.findev.service.UserService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;
import java.util.function.Function;
import java.util.stream.Collectors;

@Service
public class DeveloperServiceImpl implements DeveloperService {

    private final DeveloperRepository developerRepository;
    private final UserService userService;
    private final DeveloperDTOMapper developerDTOMapper;
    private final SkillRepository skillRepository;
    private final DeveloperSkillRepository developerSkillRepository;
    private final PasswordEncoder passwordEncoder;

    public DeveloperServiceImpl(
            DeveloperRepository developerRepository,
            UserService userService,
            DeveloperDTOMapper developerDTOMapper,
            SkillRepository skillRepository,
            DeveloperSkillRepository developerSkillRepository,
            PasswordEncoder passwordEncoder) {
        this.developerRepository = developerRepository;
        this.userService = userService;
        this.developerDTOMapper = developerDTOMapper;
        this.skillRepository = skillRepository;
        this.developerSkillRepository = developerSkillRepository;
        this.passwordEncoder = passwordEncoder;
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
        userService.deleteUser(id);
    }

    @Override
    public DeveloperDto updateDeveloper(UUID id, DeveloperWithSkillsDto developerDto) {
        Developer existingDeveloper = developerRepository.findById(id)
                .orElseThrow(() -> new DeveloperNotFoundException("Developer not found"));

        // Update basic fields
        existingDeveloper.setFirstName(developerDto.getFirstName());
        existingDeveloper.setLastName(developerDto.getLastName());
        existingDeveloper.setEmail(developerDto.getEmail());
        existingDeveloper.setPhone(developerDto.getPhone());
        existingDeveloper.setPassword(developerDto.getPassword());
        existingDeveloper.setPortfolio(developerDto.getPortfolio());
        existingDeveloper.setSeniority(developerDto.getSeniority());
        existingDeveloper.setStatus(developerDto.isStatus());

        // Handle skills update more carefully
        if (developerDto.getSkills() != null && !developerDto.getSkills().isEmpty()) {
            updateDeveloperSkills(existingDeveloper, developerDto.getSkills());
        } else {
            // If skills array is empty, clear existing skills
            existingDeveloper.getSkills().clear();
        }

        Developer updatedDeveloper = developerRepository.save(existingDeveloper);
        return developerDTOMapper.apply(updatedDeveloper);
    }

    private void updateDeveloperSkills(Developer developer, List<SkillExperienceDto> skillsDto) {
        // Create a map of existing skills for quick lookup
        Map<Long, DeveloperSkill> existingSkillsMap = developer.getSkills().stream()
                .collect(Collectors.toMap(
                        ds -> ds.getSkill().getId(),
                        Function.identity()
                ));

        // Clear the collection first to avoid duplicates
        developer.getSkills().clear();

        // Process each skill from DTO
        for (SkillExperienceDto skillDto : skillsDto) {
            Skill skill = skillRepository.findById(skillDto.getSkillId())
                    .orElseThrow(() -> new RuntimeException("Skill not found with id: " + skillDto.getSkillId()));

            // Check if this skill already existed for the developer
            DeveloperSkill developerSkill = existingSkillsMap.get(skillDto.getSkillId());

            if (developerSkill == null) {
                // Create new DeveloperSkill if it didn't exist before
                developerSkill = new DeveloperSkill();
                developerSkill.setDeveloper(developer);
                developerSkill.setSkill(skill);
            }

            // Update experience years (whether new or existing)
            developerSkill.setExperienceYears(skillDto.getExperienceYears());
            developer.getSkills().add(developerSkill);
        }
    }


    @Override
    public Optional<Developer> findByIdWithSkills(UUID developerId) {
        return developerRepository.findByIdWithSkills(developerId);
    }


    @Override
    public DeveloperDto create(Developer developer, List<SkillExperienceDto> skillsWithExperience) {
        Optional<Developer> developerExists = developerRepository.findDeveloperByEmail(developer.getEmail());

        if (developerExists.isPresent()) {
            throw new IllegalArgumentException("Developer already exists");
        }

        User user = new User();
        user.setEmail(developer.getEmail());
        user.setPassword(developer.getPassword());
        user.setRole(UserType.DEVELOPER);
        user.setActive(true);
        User savedUser = userService.save(user);

        developer.setUser(savedUser);

        Developer savedDeveloper = developerRepository.save(developer);

        List<DeveloperSkill> developerSkills = skillsWithExperience.stream()
                .map(skillExperience -> {
                    Skill skill = skillRepository.findById(skillExperience.getSkillId())
                            .orElseThrow(() -> new IllegalArgumentException("Skill not found"));

                    DeveloperSkill developerSkill = new DeveloperSkill();
                    developerSkill.setDeveloper(savedDeveloper);
                    developerSkill.setSkill(skill);
                    developerSkill.setExperienceYears(skillExperience.getExperienceYears());

                    return developerSkill;
                })
                .collect(Collectors.toList());

        developerSkillRepository.saveAll(developerSkills);

        return developerDTOMapper.apply(savedDeveloper);
    }
}
