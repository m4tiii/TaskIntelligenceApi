package pl.mati.taskintelligenceapi.controller.authController;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;
import pl.mati.taskintelligenceapi.dto.RestResponse;
import pl.mati.taskintelligenceapi.dto.authDto.AuthRegisterRequestDTO;
import pl.mati.taskintelligenceapi.dto.authDto.AuthRequestDTO;
import pl.mati.taskintelligenceapi.dto.authDto.AuthResponseDTO;
import pl.mati.taskintelligenceapi.dto.authDto.RefreshTokenRequestDTO;
import pl.mati.taskintelligenceapi.service.authService.AuthService;

import java.security.Principal;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
@Tag(name = "Authentication", description = "Endpoints for user registration and authentication")
public class AuthController {

    private final AuthService authService;

    @Operation(summary = "Register user", description = "Registers a new user.",  security = {})
    @PostMapping("/register")
    public ResponseEntity<RestResponse<?>> registerUser(@RequestBody AuthRegisterRequestDTO authRequestDTO){
        AuthResponseDTO authResponseDTO = authService.registerUser(authRequestDTO);
        return ResponseEntity.ok(RestResponse.success(authResponseDTO));
    }

    @Operation(summary = "Login user", description = "Logs in an existing user.", security = {})
    @PostMapping("/login")
    public ResponseEntity<RestResponse<AuthResponseDTO>> login(@RequestBody AuthRequestDTO authRequestDTO){
        AuthResponseDTO authResponseDTO = authService.login(authRequestDTO);
        return ResponseEntity.ok(RestResponse.success(authResponseDTO));
    }

    @Operation(summary = "Logout user", description = "Invalidates the refresh token by removing it from database")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Successfully logged out"),
            @ApiResponse(responseCode = "401", description = "Unauthorized - invalid or missing JWT")
    })
    @PostMapping("/logout")
    @Transactional
    public ResponseEntity<RestResponse<Void>> logout(Principal principal){
        authService.logout(principal);
        return ResponseEntity.ok(RestResponse.success(null));
    }

    @Operation(summary = "Refresh token", description = "Refreshes the access token.")
    @PostMapping("/refresh")
    public ResponseEntity<RestResponse<AuthResponseDTO>> refresh(@RequestBody RefreshTokenRequestDTO refreshToken){

        AuthResponseDTO authResponseDTO = authService.refresh(refreshToken);
        return ResponseEntity.ok(RestResponse.success(authResponseDTO));
    }

}
