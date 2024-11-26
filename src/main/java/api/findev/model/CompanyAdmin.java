package api.findev.model;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.OneToOne;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

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
}
