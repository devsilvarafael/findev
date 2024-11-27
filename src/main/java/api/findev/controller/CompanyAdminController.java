package api.findev.controller;

import api.findev.dto.RecruiterDto;

import api.findev.service.CompanyAdminService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/api/admin")
public class CompanyAdminController {
    private final CompanyAdminService companyAdminService;

    public CompanyAdminController(CompanyAdminService companyAdminService) {
        this.companyAdminService = companyAdminService;
    }

    @GetMapping("/recruiters/{companyId}")
    public ResponseEntity<Page<RecruiterDto>> getAllRecruiters(@PathVariable UUID companyId, @PageableDefault(page = 0, size = 10, sort = "recruiterId", direction = Sort.Direction.ASC) Pageable pageable) {
        return ResponseEntity.status(HttpStatus.OK).body(companyAdminService.getRecruitersByCompany(companyId, pageable));
    }
}
