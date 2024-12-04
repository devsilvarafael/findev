package api.findev.mapper;

import api.findev.dto.response.SkillExperienceDto;
import api.findev.model.DeveloperSkill;
import org.springframework.stereotype.Service;

import java.util.function.Function;

@Service
public class DeveloperSkillDTOMapper implements Function<DeveloperSkill, SkillExperienceDto> {
    @Override
    public SkillExperienceDto apply(DeveloperSkill developerSkill) {
        return new SkillExperienceDto(
                developerSkill.getSkill().getId(),
                developerSkill.getSkill().getName(),
                developerSkill.getExperienceYears()
        );
    }
}
