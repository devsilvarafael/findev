package api.findev.service.Impl;

import api.findev.dto.CompanyDto;
import api.findev.mapper.CompanyDTOMapper;
import api.findev.model.Company;
import api.findev.repository.CompanyRepository;
import api.findev.service.CompanyService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class CompanyServiceImpl implements CompanyService {

    private final CompanyRepository companyRepository;
    private final CompanyDTOMapper companyDTOMapper;

    public CompanyServiceImpl(CompanyRepository companyRepository, CompanyDTOMapper companyDTOMapper) {
        this.companyRepository = companyRepository;
        this.companyDTOMapper = companyDTOMapper;
    }

    @Override
    public Page<CompanyDto> getAllCompanies(Pageable pageable) {
        List<CompanyDto> list = companyRepository.findAll()
                .stream().map(companyDTOMapper).collect(Collectors.toList());

        return new PageImpl<>(list);
    }

    @Override
    public CompanyDto createCompany(Company company) {
        Company savedCompany = companyRepository.save(company);
        return companyDTOMapper.apply(savedCompany);
    }
}
