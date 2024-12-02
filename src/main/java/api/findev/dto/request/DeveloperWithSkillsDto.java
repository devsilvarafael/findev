package api.findev.dto.request;

import api.findev.dto.response.SkillExperienceDto;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;


@Data
@AllArgsConstructor
@NoArgsConstructor
public class DeveloperWithSkillsDto {

    private String firstName;
    private String lastName;
    private String email;
    private String phone;
    private String password;
    private String portfolio;
    private int seniority;
    private boolean status;
    private List<SkillExperienceDto> skills;

}

