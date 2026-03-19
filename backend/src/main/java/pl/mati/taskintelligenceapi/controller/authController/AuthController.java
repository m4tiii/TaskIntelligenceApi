package pl.mati.taskintelligenceapi.controller.authController;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import pl.mati.taskintelligenceapi.dto.authDto.AuthRegisterRequestDTO;
import pl.mati.taskintelligenceapi.dto.authDto.AuthRequestDTO;
import pl.mati.taskintelligenceapi.dto.authDto.AuthResponseDTO;
import pl.mati.taskintelligenceapi.dto.authDto.RefreshTokenRequestDTO;
import pl.mati.taskintelligenceapi.entity.Role;
import pl.mati.taskintelligenceapi.entity.User;
import pl.mati.taskintelligenceapi.repository.UserRepository;
import pl.mati.taskintelligenceapi.security.JwtUtil;
import pl.mati.taskintelligenceapi.service.authService.AuthService;

import java.security.Principal;
import java.time.LocalDateTime;
import java.util.Optional;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
@Tag(name = "Authentication", description = "Endpoints for user registration and authentication")
public class AuthController {

    private final AuthService authService;

    @Operation(summary = "Register user", description = "Registers a new user.",  security = {})
    @PostMapping("/register")
    public ResponseEntity<?> registerUser(@RequestBody AuthRegisterRequestDTO authRequestDTO){

        return ResponseEntity.ok(authService.registrerUser(authRequestDTO));
    }

    @Operation(summary = "Login user", description = "Logs in an existing user.", security = {})
    @PostMapping("/login")
    public ResponseEntity<AuthResponseDTO> login(@RequestBody AuthRequestDTO authRequestDTO){
        return ResponseEntity.ok(authService.login(authRequestDTO));
    }

    @Operation(summary = "Logout user", description = "Invalidates the refresh token by removing it from database")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Successfully logged out"),
            @ApiResponse(responseCode = "401", description = "Unauthorized - invalid or missing JWT")
    })
    @PostMapping("/logout")
    @Transactional
    public ResponseEntity<Void> logout(Principal principal){
        authService.logout(principal);
        return ResponseEntity.noContent().build();
    }

    @Operation(summary = "Refresh token", description = "Refreshes the access token.")
    @PostMapping("/refresh")
    public ResponseEntity<AuthResponseDTO> refresh(@RequestBody RefreshTokenRequestDTO refreshToken){

        return ResponseEntity.ok(authService.refresh(refreshToken));
    }

}
