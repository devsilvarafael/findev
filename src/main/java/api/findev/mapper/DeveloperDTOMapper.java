package api.findev.mapper;

import api.findev.dto.DeveloperDto;
import api.findev.dto.SkillDto;
import api.findev.model.Developer;
import api.findev.model.Skill;
import org.springframework.stereotype.Service;

import java.util.function.Function;
import java.util.stream.Collectors;

@Service
public class DeveloperDTOMapper implements Function<Developer, DeveloperDto> {

    @Override
    public DeveloperDto apply(Developer developer) {
        return new DeveloperDto(
                developer.getId(),
                developer.getFirstName(),
                developer.getLastName(),
                developer.getEmail(),
                developer.getPhone(),
                developer.getPortfolio(),
                developer.getSeniority(),
                developer.getSkills().stream()
                        .map(this::mapToSkillDto)  // Convert Skill to SkillDto
                        .collect(Collectors.toList())
        );
    }

    private SkillDto mapToSkillDto(Skill skill) {
        SkillDto skillDto = new SkillDto();
        skillDto.setName(skill.getName());
        skillDto.setExperienceYears(skill.getExperienceYears());
        return skillDto;
    }

    @Override
    public <V> Function<V, DeveloperDto> compose(Function<? super V, ? extends Developer> before) {
        return Function.super.compose(before);
    }

    @Override
    public <V> Function<Developer, V> andThen(Function<? super DeveloperDto, ? extends V> after) {
        return Function.super.andThen(after);
    }
}
