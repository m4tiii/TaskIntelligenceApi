package pl.mati.taskintelligenceapi.service.authService;

import jakarta.persistence.EntityNotFoundException;
import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;
import pl.mati.taskintelligenceapi.dto.authDto.AuthRegisterRequestDTO;
import pl.mati.taskintelligenceapi.dto.authDto.AuthRequestDTO;
import pl.mati.taskintelligenceapi.dto.authDto.AuthResponseDTO;
import pl.mati.taskintelligenceapi.dto.authDto.RefreshTokenRequestDTO;
import pl.mati.taskintelligenceapi.entity.User;
import pl.mati.taskintelligenceapi.entity.enums.Role;
import pl.mati.taskintelligenceapi.repository.UserRepository;
import pl.mati.taskintelligenceapi.security.JwtUtil;

import java.security.Principal;
import java.time.OffsetDateTime;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final AuthenticationManager authenticationManager;
    private final JwtUtil jwtUtil;
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    @Transactional
    public AuthResponseDTO login(AuthRequestDTO authRequestDTO){
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(authRequestDTO.username(), authRequestDTO.password())
        );

        User user = userRepository.findByUsername(authRequestDTO.username())
                .orElseThrow(() -> new EntityNotFoundException("User not found"));

        return getAuthResponse(user);
    }

    @NotNull
    private AuthResponseDTO getAuthResponse(User user){
        String access = jwtUtil.generateToken(user.getUsername());
        String refresh = jwtUtil.generateRefreshToken(user.getUsername());

        user.setRefreshToken(refresh);
        user.setRefreshTokenExpiration(OffsetDateTime.now().plusDays(7));
        userRepository.save(user);

        return new AuthResponseDTO(access, refresh);
    }

    @Transactional
    public void logout(Principal principal){
        User user = userRepository.findByUsername(principal.getName())
                .orElseThrow(() -> new EntityNotFoundException("User not found"));

        user.setRefreshToken(null);
        user.setRefreshTokenExpiration(null);
        userRepository.save(user);
    }

    @Transactional
    public AuthResponseDTO refresh(RefreshTokenRequestDTO refreshToken){
        String username = jwtUtil.extractUsername(refreshToken.refreshToken());
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new EntityNotFoundException("User not found"));

        if(
                user.getRefreshToken() != null &&
                user.getRefreshToken().equals(refreshToken.refreshToken()) &&
                        user.getRefreshTokenExpiration().isAfter(OffsetDateTime.now())
        ){
            return getAuthResponse(user);
        }
        throw new ResponseStatusException(HttpStatus.FORBIDDEN, "Invalid or expired refresh token");
    }

    @Transactional
    public AuthResponseDTO registerUser(AuthRegisterRequestDTO authRegisterRequestDTO){
        Optional<User> existingUser = userRepository.findByUsernameAndEmail(authRegisterRequestDTO.username(), authRegisterRequestDTO.email());
        if(existingUser.isPresent()){
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Username already exists");
        }

        User newUser = new User();
        newUser.setUsername(authRegisterRequestDTO.username());
        newUser.setPassword(passwordEncoder.encode(authRegisterRequestDTO.password()));
        newUser.setEmail(authRegisterRequestDTO.email());
        newUser.setFirstName(authRegisterRequestDTO.firstName());
        newUser.setLastName(authRegisterRequestDTO.lastName());
        newUser.setCountry(authRegisterRequestDTO.country());
        newUser.setRole(Role.USER);

        return getAuthResponse(newUser);
    }
}
