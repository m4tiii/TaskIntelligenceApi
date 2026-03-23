package pl.mati.taskintelligenceapi.entity;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.*;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;
import pl.mati.taskintelligenceapi.entity.enums.TaskStatus;

import java.time.LocalDateTime;

@Entity
@Table(name = "tasks")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Task {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Schema(description = "Title of the task", example = "Buy milk")
    @Column(nullable = false, length = 100)
    private String title;

    @Schema(description = "Description of the task", example = "Milk from the fridge")
    private String description;

    @Schema(description = "Date and time when the task was created", example = "2026-03-24 12:30")
    @CreationTimestamp
    @Column(updatable = false)
    private LocalDateTime createdAt;

    @Schema(description = "Date and time when the task is due", example = "2026-03-24 12:30")
    @Column(nullable = false)
    private LocalDateTime deadlineTo;

    @Schema(description = "User who created the task")
    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    @Schema(description = "Importance of the task", example = "1")
    @Min(1) @Max(10)
    @Column(nullable = false)
    private int importance;

    @Schema(description = "Status of the task", example = "NEW")
    @Enumerated(EnumType.STRING)
    private TaskStatus taskStatus;

    @Schema(description = "How important task is in scale 1-100", example = "40")
    private double priorityScore;
}
