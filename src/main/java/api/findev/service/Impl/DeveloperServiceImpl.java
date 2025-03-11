package api.findev.service.Impl;

import api.findev.dto.DeveloperDto;
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
import java.util.Optional;
import java.util.UUID;
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
    public DeveloperDto updateDeveloper(UUID developerId, Developer developer, List<SkillExperienceDto> updatedSkills) {
        Developer existingDeveloper = developerRepository.findById(developerId)
                .orElseThrow(() -> new IllegalArgumentException("Developer not found with id: " + developerId));

        // Update basic developer fields
        existingDeveloper.setFirstName(developer.getFirstName());
        existingDeveloper.setLastName(developer.getLastName());
        existingDeveloper.setEmail(developer.getEmail());
        existingDeveloper.setPhone(developer.getPhone());
        existingDeveloper.setPassword(developer.getPassword());
        existingDeveloper.setPortfolio(developer.getPortfolio());
        existingDeveloper.setSeniority(developer.getSeniority());
        existingDeveloper.setStatus(developer.isStatus());

        updateDeveloperSkills(existingDeveloper, updatedSkills);

        Developer updatedDeveloper = developerRepository.save(existingDeveloper);
        return developerDTOMapper.apply(updatedDeveloper);
    }

    private void updateDeveloperSkills(Developer developer, List<SkillExperienceDto> updatedSkills) {
        developer.getSkills().clear();

        for (SkillExperienceDto skillDto : updatedSkills) {
            Skill skill = skillRepository.findById(skillDto.getSkillId())
                    .orElseThrow(() -> new IllegalArgumentException("Skill not found"));

            DeveloperSkill developerSkill = new DeveloperSkill();
            developerSkill.setDeveloper(developer);
            developerSkill.setSkill(skill);
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
