package api.findev.controller;

import api.findev.dto.AuthResponseDto;
import api.findev.dto.RecruiterDto;
import api.findev.enums.UserType;
import api.findev.infra.security.TokenService;
import api.findev.model.*;
import api.findev.repository.CompanyAdminRepository;
import api.findev.repository.DeveloperRepository;
import api.findev.repository.RecruiterRepository;
import api.findev.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.util.Objects;
import java.util.Optional;
import java.util.UUID;

import org.springframework.http.HttpStatus;


@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {
    private final UserService userService;
    private final DeveloperRepository developerRepository;
    private final RecruiterRepository recruiterRepository;
    private final PasswordEncoder passwordEncoder;
    private final TokenService tokenService;
    private final CompanyAdminRepository companyAdminRepository;

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody LoginRequest body) {
        try {
            User user = userService.findByEmail(body.getEmail());

            if (user == null) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Usuário não encontrado.");
            }

            if (!user.isActive()) {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Usuário não  está ativo. Contate o administrador de sua empresa.");
            }

            if (!Objects.equals(body.getPassword(), user.getPassword())) {
                System.out.println(body.getPassword());
                System.out.println(user.getPassword());
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Senha incorreta.");
            }

            String token = tokenService.generateToken(user);
            Optional<Developer> developer = developerRepository.findDeveloperByEmail(body.getEmail());
            Optional<RecruiterDto> recruiter = recruiterRepository.findRecruiterByEmail(body.getEmail());
            Optional<CompanyAdmin> companyAdmin = companyAdminRepository.findByEmail(body.getEmail());

            if (developer.isEmpty() && recruiter.isEmpty() && companyAdmin.isEmpty()) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Tipo de usuário não encontrado");
            }

            if (developer.isPresent() && recruiter.isEmpty()) {
                return ResponseEntity.ok(new AuthResponseDto(
                        user.getEmail(), token, developer.get().getId(), UserType.DEVELOPER, developer.get().getAvatar()
                ));
            }

            if (companyAdmin.isPresent() && recruiter.isEmpty()) {
                return ResponseEntity.ok(new AuthResponseDto(
                        user.getEmail(), token, companyAdmin.get().getCompanyAdminId(), UserType.ADMINISTRATOR, null
                ));
            }

            return ResponseEntity.ok(new AuthResponseDto(
                    user.getEmail(), token, recruiter.get().recruiterId(), UserType.RECRUITER, recruiter.get().avatar()
            ));

        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Ocorreu um erro inesperado " + e.getMessage());
        }

    }

    @PutMapping("/{id}")
    public ResponseEntity<?> updateUserStatus(@PathVariable UUID id, @RequestParam boolean status) {
        try {
            User user = userService.findById(id);
            Recruiter recruiter = recruiterRepository.findRecruiterByRecruiterId(id);



            user.setActive(status);
            recruiter.setIsActive(status);
            userService.save(user);
            recruiterRepository.save(recruiter);

            return ResponseEntity.ok("User status updated successfully.");
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("An unexpected error occurred: " + e.getMessage());
        }
    }

    @ExceptionHandler(RuntimeException.class)
    public ResponseEntity<?> handleRuntimeException(RuntimeException ex) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(ex.getMessage());
    }
}

