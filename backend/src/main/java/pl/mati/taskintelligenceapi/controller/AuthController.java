package pl.mati.taskintelligenceapi.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import pl.mati.taskintelligenceapi.dto.AuthRequestDTO;
import pl.mati.taskintelligenceapi.dto.AuthResponseDTO;
import pl.mati.taskintelligenceapi.entity.Role;
import pl.mati.taskintelligenceapi.entity.User;
import pl.mati.taskintelligenceapi.repository.UserRepository;
import pl.mati.taskintelligenceapi.security.JwtUtil;

import java.util.Optional;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthenticationManager authenticationManager;
    private final JwtUtil jwtUtil;
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    @PostMapping("/register")
    public ResponseEntity<?> registerUser(@RequestBody AuthRequestDTO authRequestDTO){
        Optional<User> existingUser = userRepository.findByUsername(authRequestDTO.username());
        if(existingUser.isPresent()){
            return ResponseEntity.badRequest().build();
        }

        User newUser = new User();
        newUser.setUsername(authRequestDTO.username());
        newUser.setPassword(passwordEncoder.encode(authRequestDTO.password()));
        newUser.setRole(Role.ROLE_USER);

        userRepository.save(newUser);

        String token = jwtUtil.generateToken(newUser.getUsername());
        return ResponseEntity.ok(new AuthResponseDTO(token));
    }

    @PostMapping("/login")
    public ResponseEntity<AuthResponseDTO> login(@RequestBody AuthRequestDTO authRequestDTO){
        authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(authRequestDTO.username(), authRequestDTO.password()));
        String token = jwtUtil.generateToken(authRequestDTO.username());
        return ResponseEntity.ok(new AuthResponseDTO(token));
    }

}
