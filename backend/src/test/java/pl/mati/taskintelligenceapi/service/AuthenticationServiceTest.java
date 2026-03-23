package pl.mati.taskintelligenceapi.service;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import pl.mati.taskintelligenceapi.dto.authDto.AuthRegisterRequestDTO;
import pl.mati.taskintelligenceapi.dto.authDto.AuthRequestDTO;
import pl.mati.taskintelligenceapi.dto.authDto.AuthResponseDTO;
import pl.mati.taskintelligenceapi.dto.authDto.RefreshTokenRequestDTO;
import pl.mati.taskintelligenceapi.entity.User;
import pl.mati.taskintelligenceapi.repository.UserRepository;
import pl.mati.taskintelligenceapi.security.JwtUtil;
import pl.mati.taskintelligenceapi.service.authService.AuthService;

import java.security.Principal;
import java.util.Optional;

@ExtendWith(MockitoExtension.class)
public class AuthenticationServiceTest {

    @Mock
    UserRepository userRepository;
    @Mock
    PasswordEncoder passwordEncoder;
    @Mock
    JwtUtil jwtUtil;
    @Mock
    AuthenticationManager authenticationManager;
    @InjectMocks
    AuthService authService;

    @Test
    void shouldRegisterUserSuccessfully() {
        // Given
        AuthRegisterRequestDTO request = new AuthRegisterRequestDTO(
                "testUser",
                "password",
                "test@email.com",
                "John",
                "Doe",
                "Poland"
        );

        Mockito.when(userRepository.findByUsernameAndEmail(request.username(), request.email()))
                .thenReturn(Optional.empty());
        Mockito.when(passwordEncoder.encode(request.password())).thenReturn("encodedPassword");
        Mockito.when(jwtUtil.generateToken(request.username())).thenReturn("accessToken");
        Mockito.when(jwtUtil.generateRefreshToken(request.username())).thenReturn("refreshToken");

        // When
        AuthResponseDTO response = authService.registrerUser(request);

        // Then
        Assertions.assertNotNull(response);
        Assertions.assertEquals("accessToken", response.accessToken());
        Assertions.assertEquals("refreshToken", response.refreshToken());
        Mockito.verify(userRepository, Mockito.times(1)).save(Mockito.any(User.class));
    }

    @Test
    void shouldLoginSuccessfully() {
        // Given
        AuthRequestDTO request = new AuthRequestDTO("testUser", "password");
        User user = new User();
        user.setUsername("testUser");

        Mockito.when(userRepository.findByUsername(request.username())).thenReturn(Optional.of(user));
        Mockito.when(jwtUtil.generateToken(user.getUsername())).thenReturn("accessToken");
        Mockito.when(jwtUtil.generateRefreshToken(user.getUsername())).thenReturn("refreshToken");

        // When
        AuthResponseDTO response = authService.login(request);

        // Then
        Assertions.assertNotNull(response);
        Assertions.assertEquals("accessToken", response.accessToken());
        Mockito.verify(authenticationManager).authenticate(Mockito.any(UsernamePasswordAuthenticationToken.class));
        Mockito.verify(userRepository).save(user);
    }

    @Test
    void shouldLogoutSuccessfully() {
        // Given
        Principal principal = Mockito.mock(Principal.class);
        Mockito.when(principal.getName()).thenReturn("testUser");
        User user = new User();
        user.setUsername("testUser");
        Mockito.when(userRepository.findByUsername(principal.getName())).thenReturn(Optional.of(user));
        // When
        authService.logout(principal);
        // Then
        Mockito.verify(userRepository).save(user);
    }

    @Test
    void shouldRefreshSuccessfully() {
        // Given
        String refreshToken = "refreshToken";
        User user = new User();
        user.setUsername("testUser");
        user.setRefreshToken(refreshToken);
        user.setRefreshTokenExpiration(java.time.LocalDateTime.now().plusDays(1));
        Mockito.when(userRepository.findByUsername(Mockito.anyString())).thenReturn(Optional.of(user));
        Mockito.when(jwtUtil.extractUsername(refreshToken)).thenReturn("testUser");
        Mockito.when(jwtUtil.generateToken(Mockito.anyString())).thenReturn("newAccessToken");
        Mockito.when(jwtUtil.generateRefreshToken(Mockito.anyString())).thenReturn("newRefreshToken");
        // When
        AuthResponseDTO response = authService.refresh(new RefreshTokenRequestDTO(refreshToken));
        // Then
        Assertions.assertNotNull(response);
        Assertions.assertEquals("newAccessToken", response.accessToken());
    }
}
