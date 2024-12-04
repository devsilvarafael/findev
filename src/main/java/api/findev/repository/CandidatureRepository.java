package api.findev.repository;

import api.findev.model.JobCandidature;
import api.findev.model.Developer;
import api.findev.model.Job;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface CandidatureRepository extends JpaRepository<JobCandidature, UUID> {
    boolean existsByJobAndDeveloper(Job job, Developer developer);
    Optional<JobCandidature> findByJobAndDeveloper(Job job, Developer developer);
    Page<JobCandidature> findByJobId(UUID jobId, Pageable pageable);
    Page<JobCandidature> findByDeveloperId(UUID developerId, Pageable pageable);
}
