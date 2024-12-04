package api.findev.controller;

import api.findev.dto.AuthResponseDto;
import api.findev.dto.request.RegisterRequestDto;
import api.findev.enums.UserType;
import api.findev.infra.security.TokenService;
import api.findev.model.LoginRequest;
import api.findev.model.User;
import api.findev.repository.UserRepository;
import api.findev.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Optional;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {
    private final UserService userService;
    private final PasswordEncoder passwordEncoder;
    private final TokenService tokenService;

    @PostMapping("/login")
    public ResponseEntity login(@RequestBody LoginRequest body){
        System.out.println("estou aqui");
        User user = userService.findByEmail(body.getEmail());
        System.out.println(user.getEmail());
        System.out.println(passwordEncoder.encode(body.getPassword()));
        System.out.println(user.getPassword());
        if(passwordEncoder.matches(body.getPassword(), user.getPassword())) {
            String token = this.tokenService.generateToken(user);
            System.out.println("Token: " + token);
            return ResponseEntity.ok(new AuthResponseDto(user.getEmail(), token));
        }
        return ResponseEntity.badRequest().build();
    }


    @PostMapping("/register")
    public ResponseEntity<?> register(@RequestBody RegisterRequestDto body) {
        User newUser = new User();
        newUser.setEmail(body.email());
        newUser.setPassword(passwordEncoder.encode(body.password()));
        newUser.setRole(body.userType());

        User savedUser = userService.save(newUser);

        String token = this.tokenService.generateToken(savedUser);

        return ResponseEntity.ok(new AuthResponseDto(savedUser.getEmail(), token));
    }

}
