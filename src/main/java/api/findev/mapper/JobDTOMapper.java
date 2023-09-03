package api.findev.mapper;

import api.findev.dto.JobBenefitDto;
import api.findev.dto.JobDto;
import api.findev.dto.RecruiterDto;
import api.findev.model.Company;
import api.findev.model.Job;
import api.findev.model.JobBenefit;
import api.findev.model.Recruiter;
import api.findev.repository.CompanyRepository;
import api.findev.repository.RecruiterRepository;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.function.Function;
import java.util.stream.Collectors;

@Service
public class JobDTOMapper implements Function<Job, JobDto> {

    private final RecruiterRepository recruiterRepository;
    private final CompanyRepository companyRepository;

    public JobDTOMapper(RecruiterRepository recruiterRepository, CompanyRepository companyRepository) {
        this.recruiterRepository = recruiterRepository;
        this.companyRepository = companyRepository;
    }

    @Override
    public JobDto apply(Job job) {
        Optional<Company> companyExists = companyRepository.findById(job.getCompany());
        Optional<Recruiter> recruiterExists = recruiterRepository.findById(job.getRecruiter());

        return new JobDto(
                job.getTitle(),
                job.getDescription(),
                job.getStatus(),
                job.getSalary(),
                job.getExpirationDate(),
                companyExists.get(),
                recruiterExists.get(),
                job.getBenefits().stream()
                        .map(this::mapToJobBenefitDto)
                        .collect(Collectors.toList())
        );
    }

    private JobBenefitDto mapToJobBenefitDto(JobBenefit jobBenefit) {
        JobBenefitDto jobBenefitDto = new JobBenefitDto();
        jobBenefitDto.setBenefit(jobBenefit.getBenefit());

        // Map other attributes if needed, e.g., jobBenefitDto.setId(jobBenefit.getId());

        return jobBenefitDto;
    }

    @Override
    public <V> Function<V, JobDto> compose(Function<? super V, ? extends Job> before) {
        return Function.super.compose(before);
    }

    @Override
    public <V> Function<Job, V> andThen(Function<? super JobDto, ? extends V> after) {
        return Function.super.andThen(after);
    }
}
