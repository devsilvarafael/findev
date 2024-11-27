package api.findev.controller;

import api.findev.dto.request.JobRequestDto;
import api.findev.dto.response.JobResponseDto;
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
    public ResponseEntity<JobResponseDto> applyJob(@PathVariable UUID jobId, @RequestParam UUID developerId ) throws Exception {
        JobResponseDto updatedJob = jobService.addCandidateToJob(jobId, developerId);
        return ResponseEntity.status(HttpStatus.CREATED).body(updatedJob);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteJobById(@PathVariable UUID id) {
        try {
            jobService.deleteJobById(id);
            return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
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
}
