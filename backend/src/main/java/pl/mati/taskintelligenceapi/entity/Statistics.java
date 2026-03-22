package pl.mati.taskintelligenceapi.entity;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;


@Entity
@Table(name = "statistics")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Statistics {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Schema(description = "Score achieved", example = "85")
    @Column(nullable = false)
    private double score;

    @Column(nullable = false)
    @Schema(description = "Date of task's completion", example = "2026-03-22")
    private LocalDate completionDate;

    @Schema(description = "User associated with these statistics")
    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;
}
