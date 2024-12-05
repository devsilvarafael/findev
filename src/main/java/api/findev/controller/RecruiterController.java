package api.findev.controller;

import api.findev.dto.RecruiterCreateDto;
import api.findev.dto.RecruiterDto;
import api.findev.model.Company;
import api.findev.model.Recruiter;
import api.findev.service.RecruiterService;
import jakarta.validation.Valid;
import org.apache.coyote.Response;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/api/recruiters")
public class RecruiterController {

    private final RecruiterService recruiterService;

    public RecruiterController(RecruiterService recruiterService) {
        this.recruiterService = recruiterService;
    }

    @GetMapping
    public ResponseEntity<Page<RecruiterDto>> getAllRecruiters(@PageableDefault(page = 0, size = 10, sort = "company", direction = Sort.Direction.ASC) Pageable pageable) {
        return ResponseEntity.status(HttpStatus.OK).body(recruiterService.getAllRecruiters(pageable));
    }

    @GetMapping("/company/{companyId}")
    public ResponseEntity<Page<RecruiterDto>> getAllRecruitersByCompany(
            @PathVariable UUID companyId,
            @PageableDefault(page = 0, size = 10, direction = Sort.Direction.ASC) Pageable pageable) {
        return ResponseEntity.status(HttpStatus.OK).body(recruiterService.getAllRecruitersByCompanyId(companyId, pageable));
    }

    @PostMapping("")
    public ResponseEntity<RecruiterDto> createNewRecruiter(@RequestBody @Valid RecruiterCreateDto recruiter) {
        return ResponseEntity.status(HttpStatus.CREATED).body(recruiterService.createRecruiter(recruiter));
    }

    @GetMapping("/{recruiterId}")
    public ResponseEntity<RecruiterDto> getRecruiterById(@PathVariable UUID recruiterId) {
        return ResponseEntity.status(HttpStatus.OK).body(recruiterService.getRecruiterById(recruiterId));
    }

    @PutMapping("/{recruiterId}")
    public ResponseEntity<RecruiterDto> updateRecruiter(@PathVariable UUID recruiterId, @RequestBody @Valid Recruiter recruiter) {
        return ResponseEntity.status(HttpStatus.OK).body(recruiterService.updateRecruiter(recruiterId, recruiter));
    }

    @DeleteMapping("/{recruiterId}")
    public ResponseEntity<Object> deleteRecruiter(@PathVariable UUID recruiterId) {
        recruiterService.deleteById(recruiterId);
        return ResponseEntity.status(HttpStatus.OK).body("Developer deleted sucessfully.");
    }
}
