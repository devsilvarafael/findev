package api.findev.controller;

import api.findev.dto.CompanyDto;
import api.findev.exceptions.CompanyNotFoundException;
import api.findev.model.Company;
import api.findev.service.CompanyService;
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
@RequestMapping("/api/companies")
public class CompanyController {

    private final CompanyService companyService;

    public CompanyController(CompanyService companyService) {
        this.companyService = companyService;
    }

    @GetMapping
    public ResponseEntity<Page<CompanyDto>> getAllCompanies(@PageableDefault(page = 0, size = 10, sort = "companyId", direction = Sort.Direction.ASC) Pageable pageable) {
        return ResponseEntity.status(HttpStatus.OK).body(companyService.getAllCompanies(pageable));
    }

    @PostMapping
    public ResponseEntity<CompanyDto> createCompany(@RequestBody @Valid Company company) {
        System.out.println(company);
        return ResponseEntity.status(HttpStatus.CREATED).body(companyService.createCompany(company));
    }

    @PutMapping("/{companyId}")
    public ResponseEntity<CompanyDto> updateCompany(@PathVariable UUID companyId, @RequestBody @Valid Company company) {
        try {
            CompanyDto updatedCompany = companyService.updateCompany(companyId, company);
            return ResponseEntity.status(HttpStatus.OK).body(updatedCompany);
        } catch (CompanyNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
    }

    @DeleteMapping("/{companyId}")
    public ResponseEntity<Object> deleteCompany(@PathVariable UUID companyId) {
        try {
            companyService.deleteCompany(companyId);
            return ResponseEntity.status(HttpStatus.OK).body("Company has been deleted with successfull");
        } catch (CompanyNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        }
    }
}
