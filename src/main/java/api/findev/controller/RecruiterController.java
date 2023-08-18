package api.findev.controller;

import api.findev.dto.RecruiterDto;
import api.findev.model.Company;
import api.findev.model.Recruiter;
import api.findev.service.RecruiterService;
import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/recruiters")
public class RecruiterController {

    private final RecruiterService recruiterService;

    public RecruiterController(RecruiterService recruiterService) {
        this.recruiterService = recruiterService;
    }

    @GetMapping
    public ResponseEntity<Page<RecruiterDto>> getAllRecruiters(@PageableDefault(page = 0, size = 10, sort = "recruiterId", direction = Sort.Direction.ASC) Pageable pageable) {
        return ResponseEntity.status(HttpStatus.OK).body(recruiterService.getAllRecruiters(pageable));
    }

    @GetMapping("/{company}")
    public ResponseEntity<Page<RecruiterDto>> getAllRecruitersByCompany(
            @PathVariable Company company,
            @PageableDefault(page = 0, size = 10, sort = "recruiterId", direction = Sort.Direction.ASC) Pageable pageable) {
        return ResponseEntity.status(HttpStatus.OK).body(recruiterService.getAllRecruitersByCompany(company, pageable));
    }

    @PostMapping("")
    public ResponseEntity<RecruiterDto> createNewRecruiter(@RequestBody @Valid Recruiter recruiter) {
        return ResponseEntity.status(HttpStatus.CREATED).body(recruiterService.createRecruiter(recruiter));
    }
}
