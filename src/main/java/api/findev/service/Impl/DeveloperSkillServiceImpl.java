package api.findev.service.Impl;

import api.findev.model.DeveloperSkill;
import api.findev.repository.DeveloperSkillRepository;
import api.findev.service.DeveloperSkillService;

public class DeveloperSkillServiceImpl implements DeveloperSkillService {
    private final DeveloperSkillRepository developerSkillRepository;

    public DeveloperSkillServiceImpl(DeveloperSkillRepository developerSkillRepository) {
        this.developerSkillRepository = developerSkillRepository;
    }

    @Override
    public void deleteDeveloperSkill(Long id) {
        developerSkillRepository.deleteDeveloperSkillById(id);
    }

    @Override
    public void updateDeveloperSkillById(Long id, DeveloperSkill developerSkill) {
        developerSkillRepository.updateDeveloperSkillById(id, developerSkill);
    }
}
