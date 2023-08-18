package api.findev.mapper;

import api.findev.dto.CompanyDto;
import api.findev.model.Company;
import org.springframework.stereotype.Service;

import java.util.function.Function;

@Service
public class CompanyDTOMapper implements Function<Company, CompanyDto> {
    @Override
    public CompanyDto apply(Company company) {
        return new CompanyDto(
                company.getId(),
                company.getName(),
                company.getAddress(),
                company.getWebsite(),
                company.getEmail()
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
