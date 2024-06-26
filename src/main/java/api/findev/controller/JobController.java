package api.findev.controller;

import api.findev.dto.JobDto;
import api.findev.model.Company;
import api.findev.model.Job;
import api.findev.service.JobService;
import jakarta.validation.Valid;
import org.apache.coyote.Response;
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
    public ResponseEntity<Page<JobDto>> getAllJobs(
            @PageableDefault(page = 0, size = 10, sort = "id", direction = Sort.Direction.ASC) Pageable pageable
    ) {
        return ResponseEntity.status(HttpStatus.OK).body(jobService.getAllJobs(pageable));
    }

    @GetMapping("/company/{id}")
    public ResponseEntity<Page<JobDto>> getAllJobsByCompany(@PathVariable UUID id, Pageable pageable) throws Exception {
        return ResponseEntity.status(HttpStatus.OK).body(jobService.getAllJobsByCompany(id, pageable));
    }

    @GetMapping("/recruiter/{id}")
    public ResponseEntity<Page<JobDto>> getAllJobsByRecruiter(@PathVariable UUID id, Pageable pageable) throws Exception {
        return ResponseEntity.status(HttpStatus.OK).body(jobService.getAllJobsByRecruiter(id, pageable));
    }

    @GetMapping("/{id}")
    public ResponseEntity<Optional<JobDto>> getJobById(@PathVariable UUID id) throws Exception {
        return ResponseEntity.status(HttpStatus.OK).body(jobService.getJobById(id));
    }

    @PostMapping()
    public ResponseEntity<JobDto> createNewJobAnnounce(@RequestBody @Valid Job job) {
        return ResponseEntity.status(HttpStatus.CREATED).body(jobService.announceNewJob(job));
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
    public ResponseEntity<JobDto> updateJob(@PathVariable UUID id, @RequestBody @Valid Job job) {
        try {
            JobDto updatedJob = jobService.updateJob(id, job);
            return ResponseEntity.status(HttpStatus.OK).body(updatedJob);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        }
    }
}
