package api.findev.repository;


import api.findev.model.Job;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.UUID;

public interface JobRepository extends JpaRepository<Job, UUID> {

    List<Job> findJobsByCompanyId(UUID id, Pageable pageable);
    List<Job> findJobsByRecruiterRecruiterId(UUID id, Pageable pageable);
}
