package pl.mati.taskintelligenceapi.entity;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import pl.mati.taskintelligenceapi.entity.enums.Role;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "users")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Schema(description = "Username of the user", example = "Jonn123")
    @Column(nullable = false, unique = true)
    private String username;

    @Schema(description = "Email of the user", example = "john.doe@gmail.com")
    @Column(nullable = false, unique = true)
    private String email;

    @Schema(description = "First name of the user", example = "John")
    @Column(nullable = false)
    private String firstName;

    @Schema(description = "Last name of the user", example = "Doe")
    @Column(nullable = false)
    private String lastName;

    @Schema(description = "Country of the user", example = "Poland")
    @Column(nullable = false)
    private String country;

    @Schema(description = "Password of the user", example = "password123")
    @Column(nullable = false)
    private String password;

    @Schema(description = "Tasks of the user")
    @OneToMany(mappedBy = "user")
    private List<Task> tasks = new ArrayList<>();

    @Schema(description = "Role of the user", example = "ROLE_USER")
    @Enumerated(EnumType.STRING)
    private Role role;

    @Schema(description = "Refresh token of the user", example = "eyJhbGciOiJIUzI1NiJ9.eyJzdWIi")
    @Column(unique = true, length = 1000)
    private String refreshToken;

    @Schema(description = "Expiration date of the refresh token", example = "2026-03-24 12:30")
    private LocalDateTime refreshTokenExpiration;

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Statistics> statistics;
}
