package api.findev.dto;

import api.findev.model.Developer;
import api.findev.model.DeveloperSkill;
import jakarta.validation.constraints.NotBlank;
import lombok.*;

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
        List<DeveloperSkill> skills
){

}
