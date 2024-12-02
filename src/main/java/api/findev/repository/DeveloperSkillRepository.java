package api.findev.repository;

import api.findev.model.DeveloperSkill;
import api.findev.model.Skill;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

public interface DeveloperSkillRepository extends JpaRepository<DeveloperSkill, Long> {

    void deleteDeveloperSkillById(Long skillId);

    @Modifying
    @Query("UPDATE DeveloperSkill ds SET ds.experienceYears = :experienceYears, ds.skill = :skill WHERE ds.id = :skillId")
    void updateDeveloperSkillById(Long skillId, DeveloperSkill skill);
}
