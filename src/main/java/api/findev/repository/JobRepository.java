package api.findev.repository;

import api.findev.dto.JobDto;
import api.findev.model.Company;
import api.findev.model.Job;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.UUID;

public interface JobRepository extends JpaRepository<Job, UUID> {

    List<Job> findJobsByCompany(UUID id, Pageable pageable);
    List<Job> findJobsByRecruiter(UUID id, Pageable pageable);
}