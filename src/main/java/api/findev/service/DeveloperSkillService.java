package api.findev.service;

import api.findev.model.DeveloperSkill;

public interface DeveloperSkillService {
    void deleteDeveloperSkill(Long id);
    void updateDeveloperSkillById(Long id, DeveloperSkill developerSkill);
}
