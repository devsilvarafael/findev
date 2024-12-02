package api.findev.dto;

import lombok.Data;

@Data
public class SkillDto {
    private String name;
    private int experienceYears;

    public SkillDto(String name, int experienceYears) {
        this.name = name;
        this.experienceYears = experienceYears;
    }
}
