package api.findev.controller;

import api.findev.dto.request.ApplyJobRequest;
import api.findev.dto.request.JobRequestDto;
import api.findev.dto.response.JobResponseDto;
import api.findev.model.Job;
import api.findev.service.JobService;
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
@RequestMapping(value = "/api/jobs")
public class JobController {

    private final JobService jobService;

    public JobController(JobService jobService) {
        this.jobService = jobService;
    }

    @GetMapping("")
    public ResponseEntity<Page<JobResponseDto>> getAllJobs(
            @PageableDefault(page = 0, size = 10, sort = "id", direction = Sort.Direction.ASC) Pageable pageable
    ) {
        return ResponseEntity.status(HttpStatus.OK).body(jobService.getAllJobs(pageable));
    }

    @GetMapping("/company/{id}")
    public ResponseEntity<Page<JobResponseDto>> getAllJobsByCompany(@PathVariable UUID id, Pageable pageable) throws Exception {
        return ResponseEntity.status(HttpStatus.OK).body(jobService.getAllJobsByCompany(id, pageable));
    }

    @GetMapping("/recruiter/{id}")
    public ResponseEntity<Page<JobResponseDto>> getAllJobsByRecruiter(@PathVariable UUID id, Pageable pageable) throws Exception {
        return ResponseEntity.status(HttpStatus.OK).body(jobService.getAllJobsByRecruiter(id, pageable));
    }

    @GetMapping("/{id}")
    public ResponseEntity<Optional<JobResponseDto>> getJobById(@PathVariable UUID id) throws Exception {
        return ResponseEntity.status(HttpStatus.OK).body(jobService.getJobById(id));
    }

    @PostMapping()
    public ResponseEntity<JobResponseDto> createNewJobAnnounce(@RequestBody @Valid JobRequestDto jobRequestDto) throws Exception {
        JobResponseDto createdJob = jobService.announceNewJob(jobRequestDto);
        return ResponseEntity.status(HttpStatus.CREATED).body(createdJob);
    }

    @PostMapping("/apply/{jobId}")
    public ResponseEntity<JobResponseDto> applyJob(
            @PathVariable UUID jobId,
            @RequestBody ApplyJobRequest applyJobRequest) throws Exception {

        UUID developerId = applyJobRequest.getDeveloperId();
        JobResponseDto updatedJob = jobService.addCandidateToJob(developerId, jobId);

        return ResponseEntity.status(HttpStatus.CREATED).body(updatedJob);
    }


    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteJobById(@PathVariable UUID id) {
        try {
            jobService.deleteJobById(id);
            return ResponseEntity.status(HttpStatus.OK).body("Vaga excluída com sucesso!");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<JobResponseDto> updateJob(@PathVariable UUID id, @RequestBody @Valid JobRequestDto job) {
        try {
            JobResponseDto updatedJob = jobService.updateJob(id, job);
            return ResponseEntity.status(HttpStatus.OK).body(updatedJob);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        }
    }

    @GetMapping("/matching")
    public ResponseEntity<Page<JobResponseDto>> getMatchingJobs(
            @RequestParam UUID developerId,
            Pageable pageable) {
        try {
            Page<JobResponseDto> matchingJobs = jobService.getMatchingJobsForDeveloper(developerId, pageable);
            return ResponseEntity.ok(matchingJobs);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(Page.empty());
        }
    }

    @DeleteMapping("/{jobId}/candidates/{developerId}")
    public ResponseEntity<Void> removeCandidateFromJob(
            @PathVariable UUID jobId,
            @PathVariable UUID developerId) {
        try {
            jobService.removeCandidateFromJob(developerId, jobId);
            return ResponseEntity.noContent().build(); // HTTP 204
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);
        }
    }

}
