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
@Table(name = "speaking_topics")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class SpeakingTopic {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "Tiêu đề không được để trống")
    @Size(max = 200, message = "Tiêu đề không quá 200 ký tự")
    @Column(nullable = false, length = 200)
    private String title;

    @Column(columnDefinition = "TEXT")
    private String description;

    @NotBlank(message = "Prompt không được để trống")
    @Column(nullable = false, columnDefinition = "TEXT")
    private String prompt;

    @Column(name = "suggested_duration")
    @Builder.Default
    private Integer suggestedDuration = 60; // seconds

    @Enumerated(EnumType.STRING)
    @Column(length = 20)
    @Builder.Default
    private Difficulty difficulty = Difficulty.MEDIUM;

    @Column(name = "is_active")
    @Builder.Default
    private Boolean isActive = true;

    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;

    // Relationships
    @OneToMany(mappedBy = "speakingTopic", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<UserSpeakingAttempt> userAttempts;

    @PrePersist
    protected void onCreate() {
        this.createdAt = LocalDateTime.now();
    }

    public enum Difficulty {
        EASY,
        MEDIUM,
        HARD
    }
}
