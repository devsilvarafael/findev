package api.findev.controller;

import api.findev.dto.DeveloperDto;
import api.findev.model.Developer;
import api.findev.service.DeveloperService;
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

    public DeveloperController(DeveloperService developerService) {
        this.developerService = developerService;
    }

    @GetMapping
    ResponseEntity<Page<DeveloperDto>> getAllDevelopers(@PageableDefault(page = 0, size = 10, sort = "id", direction = Sort.Direction.ASC) Pageable pageable) {
        return ResponseEntity.status(HttpStatus.OK).body(developerService.getAllDevelopers(pageable));
    }

    @GetMapping("/{id}")
    ResponseEntity<Optional<DeveloperDto>> getDeveloperById(@PathVariable UUID id) {
        return ResponseEntity.status(HttpStatus.ACCEPTED).body(developerService.getDeveloperById(id));
    }

    @PostMapping
    public ResponseEntity<DeveloperDto> saveNewDeveloper(@RequestBody @Valid Developer developer) {
        DeveloperDto createdDeveloperDto = developerService.create(developer);
        return ResponseEntity.status(HttpStatus.CREATED).body(createdDeveloperDto);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Object> deleteDeveloper(@PathVariable UUID id) {
        developerService.deleteById(id);
        return ResponseEntity.status(HttpStatus.OK).body("Developer deleted sucessfully.");
    }
}
