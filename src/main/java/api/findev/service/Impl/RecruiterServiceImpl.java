package api.findev.service.Impl;

import api.findev.dto.RecruiterDto;
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


    public RecruiterServiceImpl(RecruiterRepository recruiterRepository, UserService userService, CompanyRepository companyRepository, RecruiterDTOMapper recruiterDTOMapper) {
        this.recruiterRepository = recruiterRepository;
        this.recruiterDTOMapper = recruiterDTOMapper;
        this.userService = userService;
        this.companyRepository = companyRepository;
    }


    @Override
    public Page<RecruiterDto> getAllRecruiters(Pageable pageable) {
        List<RecruiterDto> list = recruiterRepository.findAll().stream().map(recruiterDTOMapper).collect(Collectors.toList());
        return new PageImpl<>(list);
    }

    @Override
    public Page<RecruiterDto> getAllRecruitersByCompany(Company company, Pageable pageable) {
        Page<RecruiterDto> recruitersPage = recruiterRepository.findRecruiterByCompany(company, pageable);

        List<RecruiterDto> recruiterDtos = recruitersPage.getContent().stream().toList();

        return new PageImpl<>(recruiterDtos, pageable, recruitersPage.getTotalElements());
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

    @Override
    public RecruiterDto createRecruiter(Recruiter recruiter) {

        User user = new User();
        user.setEmail(recruiter.getEmail());
        user.setPassword(recruiter.getPassword());
        user.setRole("RECRUITER");
        User savedUser = userService.save(user);


        recruiter.setUser(savedUser);


        Recruiter savedRecruiter = recruiterRepository.save(recruiter);

        return recruiterDTOMapper.apply(savedRecruiter);
    }

}
