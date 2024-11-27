package api.findev.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity(name = "TB_COMPANY_ADMIN")
public class CompanyAdmin {

    @Id
    @GeneratedValue
    private UUID companyAdminId;

    @OneToOne
    private Company company;

    @OneToMany
    private List<Recruiter> recruiter = new ArrayList<>();

    @OneToMany
    private List<Job> job = new ArrayList<>();

    @Column(name = "email")
    private String email;
}
