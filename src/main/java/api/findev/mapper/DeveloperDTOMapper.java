package api.findev.mapper;

import api.findev.dto.DeveloperDto;
import api.findev.dto.SkillDto;
import api.findev.model.Developer;
import api.findev.model.Skill;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
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
                        .map(skill -> new SkillDto(skill.getName(), skill.getExperienceYears()))
                        .collect(Collectors.toList())
        );
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
