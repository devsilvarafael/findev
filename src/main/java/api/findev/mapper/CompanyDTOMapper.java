package api.findev.mapper;

import api.findev.dto.CompanyDto;
import api.findev.dto.RecruiterDto;
import api.findev.model.Company;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.function.Function;
import java.util.stream.Collectors;

@Service
public class CompanyDTOMapper implements Function<Company, CompanyDto> {
    private final RecruiterDTOMapper recruiterDTOMapper;

    private CompanyDTOMapper(RecruiterDTOMapper recruiterDTOMapper) {
        this.recruiterDTOMapper = recruiterDTOMapper;
    }

    @Override
    public CompanyDto apply(Company company) {
        List<RecruiterDto> recruiters = (company.getRecruiters() == null ?
                List.of() :
                company.getRecruiters().stream()
                        .map(recruiterDTOMapper::apply)
                        .collect(Collectors.toList()));


        return new CompanyDto(
                company.getId(),
                company.getRegistrationNumber(),
                company.getName(),
                company.getAddress(),
                company.getWebsite(),
                company.getEmail(),
                company.getCompanyLogo(),
                company.getIsActive(),
                recruiters
        );
    }

    @Override
    public <V> Function<V, CompanyDto> compose(Function<? super V, ? extends Company> before) {
        return Function.super.compose(before);
    }

    @Override
    public <V> Function<Company, V> andThen(Function<? super CompanyDto, ? extends V> after) {
        return Function.super.andThen(after);
    }
}
