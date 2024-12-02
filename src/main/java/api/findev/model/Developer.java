package api.findev.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "TB_DEVELOPER")
public class Developer {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private UUID id;

    @OneToOne
    @MapsId
    private User user;



    @Column(name = "first_name", nullable = false)
    @NotNull
    private String firstName;

    @Column(name = "last_name", nullable = false)
    @NotNull
    private String lastName;

    @Column(name = "email", nullable = false)
    @NotNull
    private String email;

    @Column(name = "phone", nullable = false)
    @NotNull
    private String phone;

    @Column(name = "password", nullable = false)
    @NotNull
    private String password;

    @Column(name = "seniority", nullable = false)
    @NotNull
    private int seniority;

    @Column(name = "portfolio")
    private String portfolio;

    @Column(name = "status", nullable = false)
    private boolean status = true;

    @ManyToMany(fetch = FetchType.LAZY, cascade = {CascadeType.PERSIST, CascadeType.MERGE})
    @JoinTable(
            name = "tb_developer_skill",
            joinColumns = @JoinColumn(name = "developer_id"),
            inverseJoinColumns = @JoinColumn(name = "skill_id")
    )
    private List<Skill> skills = new ArrayList<>();

}
