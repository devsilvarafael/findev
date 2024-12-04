package api.findev.controller;

import api.findev.dto.AuthResponseDto;
import api.findev.dto.RecruiterDto;
import api.findev.dto.request.RegisterRequestDto;
import api.findev.enums.UserType;
import api.findev.infra.security.TokenService;
import api.findev.model.Developer;
import api.findev.model.LoginRequest;
import api.findev.model.Recruiter;
import api.findev.model.User;
import api.findev.repository.DeveloperRepository;
import api.findev.repository.RecruiterRepository;
import api.findev.repository.UserRepository;
import api.findev.service.DeveloperService;
import api.findev.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Objects;
import java.util.Optional;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {
    private final UserService userService;
    private final DeveloperRepository developerRepository;
    private final RecruiterRepository recruiterRepository;
    private final PasswordEncoder passwordEncoder;
    private final TokenService tokenService;

    @PostMapping("/login")
    public ResponseEntity login(@RequestBody LoginRequest body){
        User user = userService.findByEmail(body.getEmail());
        if(Objects.equals(body.getPassword(), user.getPassword())) {
            String token = this.tokenService.generateToken(user);
            System.out.println("Token: " + token);
            Optional<Developer> developer = developerRepository.findDeveloperByEmail(body.getEmail());
            Optional<RecruiterDto> recruiter = recruiterRepository.findRecruiterByEmail(body.getEmail());

            if (developer.isEmpty() && recruiter.isEmpty()) {
                throw new RuntimeException("User not found");
            }

            if (developer.isPresent() && recruiter.isEmpty()) {
                return ResponseEntity.ok(new AuthResponseDto(user.getEmail(), token, developer.get().getId()));
            }

           return ResponseEntity.ok(new AuthResponseDto(user.getEmail(), token, recruiter.get().recruiterId()));
        }
        return ResponseEntity.badRequest().build();
    }
}
