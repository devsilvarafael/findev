package api.findev.service.Impl;

import api.findev.dto.CompanyDto;
import api.findev.exceptions.CompanyNotFoundException;
import api.findev.mapper.CompanyDTOMapper;
import api.findev.model.Company;
import api.findev.repository.CompanyRepository;
import api.findev.service.CompanyService;
import jakarta.transaction.Transactional;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.BeanWrapper;
import org.springframework.beans.BeanWrapperImpl;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.beans.FeatureDescriptor;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;
import java.util.stream.Stream;

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
        if (Stream.of(company.getRegistrationNumber(), company.getName(), company.getAddress())
                .anyMatch(Objects::isNull)) {
            throw new IllegalArgumentException("Company registration number, name, or address cannot be null.");
        }

        Optional<Company> companyAlreadyExists = companyRepository.findCompaniesByRegistrationNumber(company.getRegistrationNumber());

        if (companyAlreadyExists.isPresent()) {
            throw new IllegalArgumentException("Company registration number already exists.");
        }

        Company savedCompany = companyRepository.save(company);
        return companyDTOMapper.apply(savedCompany);
    }

    @Override
    public void deleteCompany(UUID id) {
        Company company = companyRepository.findById(id)
                .orElseThrow(() -> new CompanyNotFoundException("Failed to delete company. Company not found."));

        companyRepository.delete(company);
    }

    @Override
    @Transactional
    public CompanyDto updateCompany(UUID id, Company companyDto) {
        if (companyDto == null) {
            throw new IllegalArgumentException("Company cannot be null");
        }

        return companyRepository.findById(id)
                .map(existingCompany -> {
                    BeanUtils.copyProperties(companyDto, existingCompany, getNullPropertyNames(companyDto));
                    Company updatedCompany = companyRepository.save(existingCompany);
                    return companyDTOMapper.apply(updatedCompany);
                })
                .orElseThrow(() -> new CompanyNotFoundException("Failed to update company. Company not found."));
    }

    private String[] getNullPropertyNames(Object source) {
        final BeanWrapper wrappedSource = new BeanWrapperImpl(source);
        return Stream.of(wrappedSource.getPropertyDescriptors())
                .map(FeatureDescriptor::getName)
                .filter(propertyName -> wrappedSource.getPropertyValue(propertyName) == null)
                .toArray(String[]::new);
    }
}

