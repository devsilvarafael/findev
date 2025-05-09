package api.findev.model;

import api.findev.enums.ContractType;
import api.findev.enums.JobPriority;
import api.findev.enums.WorkModality;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.UUID;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "TB_JOB")
public class Job {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private UUID id;

    @Column(name = "job_title", nullable = false)
    private String title;

    @Column(name = "description", nullable = false)
    private String description;

    @Column(name = "status", nullable = false)
    private int status;

    @Column(name = "salary", nullable = false)
    private double salary;

    @Column(name = "expiration_date", nullable = false)
    private Date expirationDate;

    @Column(name = "created_at" , nullable = false)
    private Date createdAt;

    @ManyToOne(fetch = FetchType.LAZY)
    private Company company;

    @ManyToOne(fetch = FetchType.LAZY)
    private Recruiter recruiter;

    @Column(name = "contract_type", nullable = false)
    @Enumerated(EnumType.STRING)
    private ContractType contractType;

    @Column(name = "minWeekHours", nullable = false)
    private int minWeekHours;

    @Column(name = "maxWeekHours", nullable = false)
    private int maxWeekHours;

    @Column(name = "workModality", nullable = false)
    private WorkModality workModality;

    @Column(name = "workLocation", nullable = false)
    private String workLocation;

    @Column(name = "priority", nullable = false)
    private JobPriority priority;

    @OneToMany(mappedBy = "job", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<JobBenefit> benefits = new ArrayList<>();

    @OneToMany(mappedBy = "job", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<JobRequirement> requirements = new ArrayList<>();

    @OneToMany(mappedBy = "job", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<JobCandidature> candidates = new ArrayList<>();
}
