package api.findev.dto;

import api.findev.model.Skill;

import java.util.List;
import java.util.UUID;

public record DeveloperDto (
        UUID developerId,
        String firstName,
        String lastName,
        String email,
        String phone,
        String portfolio,
        int seniority,
        List<SkillDto> skills
){

}
