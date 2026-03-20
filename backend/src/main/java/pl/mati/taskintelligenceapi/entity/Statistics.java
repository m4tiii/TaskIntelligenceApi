package pl.mati.taskintelligenceapi.entity;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;


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

    @Schema(description = "User associated with these statistics")
    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    @Schema(description = "Score achieved", example = "85")
    private int score;

    @Schema(description = "ISO week number", example = "12")
    @Column(nullable = false)
    private int weekNumber;

    @Schema(description = "Year of the statistics", example = "2024")
    @Column(nullable = false)
    private int year;
}
