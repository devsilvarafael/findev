package api.findev.service.Impl;

import api.findev.dto.RecruiterCreateDto;
import api.findev.dto.RecruiterDto;
import api.findev.enums.UserType;
import api.findev.exceptions.CompanyNotFoundException;
import api.findev.exceptions.DeveloperNotFoundException;
import api.findev.exceptions.RecruiterNotFoundException;
import api.findev.mapper.RecruiterDTOMapper;
import api.findev.model.Company;
import api.findev.model.Recruiter;
import api.findev.model.User;
import api.findev.repository.CompanyRepository;
import api.findev.repository.RecruiterRepository;
import api.findev.service.RecruiterService;
import api.findev.service.UserService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class RecruiterServiceImpl implements RecruiterService {

    private final RecruiterRepository recruiterRepository;
    private final CompanyRepository companyRepository;

    private final UserService userService;
    private final RecruiterDTOMapper recruiterDTOMapper;
    private final PasswordEncoder passwordEncoder;


    public RecruiterServiceImpl(RecruiterRepository recruiterRepository, UserService userService, CompanyRepository companyRepository, RecruiterDTOMapper recruiterDTOMapper, PasswordEncoder passwordEncoder) {
        this.recruiterRepository = recruiterRepository;
        this.recruiterDTOMapper = recruiterDTOMapper;
        this.userService = userService;
        this.companyRepository = companyRepository;
        this.passwordEncoder = passwordEncoder;
    }


    @Override
    public Page<RecruiterDto> getAllRecruiters(Pageable pageable) {
        List<RecruiterDto> list = recruiterRepository.findAll().stream().map(recruiterDTOMapper).collect(Collectors.toList());
        return new PageImpl<>(list);
    }

    @Override
    public Page<RecruiterDto> getAllRecruitersByCompanyId(UUID companyId, Pageable pageable) {
        boolean companyExists = companyRepository.existsById(companyId);
        if (!companyExists) {
            throw new CompanyNotFoundException("Company with ID " + companyId + " not found");
        }

        return recruiterRepository.findRecruitersByCompanyId(companyId, pageable);
    }
    @Override
    public void deleteById(UUID id) {
        Recruiter recruiter = recruiterRepository.findById(id)
                .orElseThrow(() -> new RecruiterNotFoundException("Delete failed. Recruiter not found"));

        recruiterRepository.delete(recruiter);
        userService.deleteUser(id);
    }

    @Override
    public RecruiterDto updateRecruiter(UUID id, Recruiter recruiterDto) {
        Recruiter existingRecruiter = recruiterRepository.findById(id)
                .orElseThrow(() -> new RecruiterNotFoundException("Recruiter not found"));

        if (recruiterDto.getFirstName() != null) {
            existingRecruiter.setFirstName(recruiterDto.getFirstName());
        }

        if (recruiterDto.getLastName() != null) {
            existingRecruiter.setLastName(recruiterDto.getLastName());
        }

        if (recruiterDto.getEmail() != null) {
            existingRecruiter.setEmail(recruiterDto.getEmail());
        }
        if (recruiterDto.getPassword() != null) {
            existingRecruiter.setPassword(recruiterDto.getPassword());
        }
        if (recruiterDto.getPhone() != null) {
            existingRecruiter.setPhone(recruiterDto.getPhone());
        }
        if (recruiterDto.getCompany() != null) {
            existingRecruiter.setCompany(recruiterDto.getCompany());
        }

        Recruiter updatedRecruiter = recruiterRepository.save(existingRecruiter);

        return recruiterDTOMapper.apply(updatedRecruiter);
    }

    public RecruiterDto createRecruiter(RecruiterCreateDto recruiterCreateDTO) {
        Company company = companyRepository.findById(recruiterCreateDTO.getCompany())
                .orElseThrow(() -> new RuntimeException("Company not found"));

        Optional<RecruiterDto> recruiterExists = recruiterRepository.findRecruiterByEmail(recruiterCreateDTO.getEmail());

        if (recruiterExists.isPresent()) {
            throw new IllegalArgumentException("Recruiter email already exists");
        }

        User user = new User();
        user.setEmail(recruiterCreateDTO.getEmail());
        user.setPassword(passwordEncoder.encode(recruiterCreateDTO.getPassword()));
        user.setRole(UserType.RECRUITER);

        User savedUser = userService.save(user);

        Recruiter recruiter = new Recruiter();
        recruiter.setFirstName(recruiterCreateDTO.getFirstName());
        recruiter.setLastName(recruiterCreateDTO.getLastName());
        recruiter.setEmail(recruiterCreateDTO.getEmail());
        recruiter.setPhone(recruiterCreateDTO.getPhone());
        recruiter.setPassword(recruiterCreateDTO.getPassword());
        recruiter.setUser(savedUser);
        recruiter.setCompany(company);

        Recruiter savedRecruiter = recruiterRepository.save(recruiter);

        return recruiterDTOMapper.apply(savedRecruiter);
    }

}
