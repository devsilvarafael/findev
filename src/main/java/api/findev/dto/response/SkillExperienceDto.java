package api.findev.dto.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class SkillExperienceDto {

    private Long skillId;
    private String skillName;
    private int experienceYears;
}
