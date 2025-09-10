package com.thanhnb.englishlearningplatform.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import jakarta.validation.constraints.*;

import java.time.LocalDateTime;
import java.util.List;

@Entity
@Table(name = "writing_exercises")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class WritingExercise {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "Tiêu đề không được để trống")
    @Size(max = 200, message = "Tiêu đề không quá 200 ký tự")
    @Column(nullable = false, length = 200)
    private String title;

    @NotBlank(message = "Nội dung không được để trống")
    @Column(nullable = false, columnDefinition = "TEXT")
    private String content;

    @Enumerated(EnumType.STRING)
    @Column(name = "exercise_type", nullable = false, length = 20)
    private ExerciseType exerciseType;

    @Enumerated(EnumType.STRING)
    @Column(length = 20)
    @Builder.Default
    private Level level = Level.BEGINNER;

    @Column(name = "is_active")
    @Builder.Default
    private Boolean isActive = true;

    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;

    // Relationships
    @OneToMany(mappedBy = "writingExercise", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<UserWritingAttempt> userAttempts;

    @PrePersist
    protected void onCreate() {
        this.createdAt = LocalDateTime.now();
    }

    public enum ExerciseType {
        VI_TO_EN,  // Dịch từ tiếng Việt sang tiếng Anh
        EN_TO_VI   // Dịch từ tiếng Anh sang tiếng Việt
    }

    public enum Level {
        BEGINNER,
        INTERMEDIATE,
        ADVANCED
    }
}
