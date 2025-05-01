package api.findev.controller;

import api.findev.dto.DeveloperDto;
import api.findev.dto.request.DeveloperWithSkillsDto;
import api.findev.dto.response.CandidatureDto;
import api.findev.dto.response.JobCandidateCompleteDto;
import api.findev.model.Developer;
import api.findev.service.DeveloperService;
import api.findev.service.JobCandidatureService;
import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;
import java.util.UUID;

@RestController
@RequestMapping(value = "/api/developers")
public class DeveloperController {
    final DeveloperService developerService;

    public DeveloperController(
            DeveloperService developerService
    ) {
        this.developerService = developerService;
    }

    @GetMapping
    ResponseEntity<Page<DeveloperDto>> getAllDevelopers(@PageableDefault(page = 0, size = 10, sort = "firstName", direction = Sort.Direction.ASC) Pageable pageable) {
        return ResponseEntity.status(HttpStatus.OK).body(developerService.getAllDevelopers(pageable));
    }

    @GetMapping("/{id}")
    ResponseEntity<Optional<DeveloperDto>> getDeveloperById(@PathVariable UUID id) {
        return ResponseEntity.status(HttpStatus.ACCEPTED).body(developerService.getDeveloperById(id));
    }

    @PostMapping
    public ResponseEntity<DeveloperDto> saveNewDeveloper(@RequestBody @Valid DeveloperWithSkillsDto developerWithSkillsDto) {
        Developer developer = new Developer();
        developer.setFirstName(developerWithSkillsDto.getFirstName());
        developer.setLastName(developerWithSkillsDto.getLastName());
        developer.setEmail(developerWithSkillsDto.getEmail());
        developer.setPhone(developerWithSkillsDto.getPhone());
        developer.setPassword(developerWithSkillsDto.getPassword());
        developer.setPortfolio(developerWithSkillsDto.getPortfolio());
        developer.setSeniority(developerWithSkillsDto.getSeniority());
        developer.setStatus(developerWithSkillsDto.isStatus());

        DeveloperDto createdDeveloperDto = developerService.create(developer, developerWithSkillsDto.getSkills());

        return ResponseEntity.status(HttpStatus.CREATED).body(createdDeveloperDto);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Object> deleteDeveloper(@PathVariable UUID id) {
        developerService.deleteById(id);
        return ResponseEntity.status(HttpStatus.OK).body("Developer deleted sucessfully.");
    }

    @PutMapping("/{developerId}")
    public ResponseEntity<DeveloperDto> updateDeveloper(
            @PathVariable UUID developerId,
            @RequestBody @Valid DeveloperWithSkillsDto developerDto) {
        return ResponseEntity.ok(developerService.updateDeveloper(developerId, developerDto));
    }
}
