package api.findev.controller;

import api.findev.dto.response.CandidatureDto;
import api.findev.dto.response.JobCandidateCompleteDto;
import api.findev.service.JobCandidatureService;
import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/api/candidature")
public class JobCandidatureController {

    private final JobCandidatureService jobCandidatureService;

    public JobCandidatureController(JobCandidatureService jobCandidatureService) {
        this.jobCandidatureService = jobCandidatureService;
    }

    @GetMapping("/developer/{developerId}")
    public ResponseEntity<Page<JobCandidateCompleteDto>> getCandidaturesByDeveloper(
            @PathVariable UUID developerId,
            @PageableDefault(size = 10) Pageable pageable) {

        Page<JobCandidateCompleteDto> candidatures =
                jobCandidatureService.findAllDetailedCandidaturesByDeveloper(pageable, developerId);

        return ResponseEntity.ok(candidatures);
    }

    @GetMapping("/job/{jobId}")
    public ResponseEntity<Page<CandidatureDto>> getCandidaturesByJob(
            @PathVariable UUID jobId,
            @PageableDefault(size = 10) Pageable pageable) {

        Page<CandidatureDto> candidatures = jobCandidatureService.findAllCandidaturesByJob(pageable, jobId);

        return ResponseEntity.ok(candidatures);
    }

    @PutMapping("/{candidatureId}/status")
    public ResponseEntity<Object> updateCandidatureStatus(
            @PathVariable UUID candidatureId,
            @RequestParam("status") String status) {

        try {
            jobCandidatureService.updateCandidatureStatus(candidatureId, status);
            return ResponseEntity.ok("Candidature status updated successfully.");
        } catch (IllegalArgumentException ex) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(ex.getMessage());
        } catch (Exception ex) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("An error occurred while updating candidature status.");
        }
    }
}
