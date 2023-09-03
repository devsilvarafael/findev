package api.findev.mapper;

import api.findev.dto.CompanyDto;
import api.findev.dto.RecruiterDto;
import api.findev.model.Company;
import api.findev.model.Recruiter;
import api.findev.repository.CompanyRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.function.Function;

@Service
public class RecruiterDTOMapper implements Function<Recruiter, RecruiterDto> {

    @Autowired
    CompanyRepository companyRepository;

    @Override
    public RecruiterDto apply(Recruiter recruiter) {
        Optional<Company> companyOptional = companyRepository.findById(recruiter.getCompany());

        // Check if the company is present in the Optional
        if (companyOptional.isPresent()) {
            Company company = companyOptional.get();
            return new RecruiterDto(
                    recruiter.getId(),
                    recruiter.getFirstName(),
                    recruiter.getLastName(),
                    recruiter.getEmail(),
                    recruiter.getPhone(),
                    company
            );
        } else {
            // Handle the case where no company was found
            // You can return a default value or throw an exception as needed
            return new RecruiterDto(
                    recruiter.getId(),
                    recruiter.getFirstName(),
                    recruiter.getLastName(),
                    recruiter.getEmail(),
                    recruiter.getPhone(),
                    null // or another default value
            );
        }
    }

    @Override
    public <V> Function<V, RecruiterDto> compose(Function<? super V, ? extends Recruiter> before) {
        return Function.super.compose(before);
    }

    @Override
    public <V> Function<Recruiter, V> andThen(Function<? super RecruiterDto, ? extends V> after) {
        return Function.super.andThen(after);
    }
}
