package com.thanhnb.englishlearningplatform.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "vocabulary_review_sessions")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class VocabularyReviewSession {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "user_id", nullable = false)
    private Long userId;

    @Column(name = "session_date", nullable = false)
    private LocalDateTime sessionDate;

    @Column(name = "words_reviewed")
    @Builder.Default
    private Integer wordsReviewed = 0;

    @Column(name = "correct_answers")
    @Builder.Default
    private Integer correctAnswers = 0;

    @Column(name = "accuracy_rate", precision = 5, scale = 2)
    private BigDecimal accuracyRate;

    @Column(name = "duration_minutes")
    private Integer durationMinutes;

    // Relationship
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", insertable = false, updatable = false)
    private User user;

    @PrePersist
    protected void onCreate() {
        this.sessionDate = LocalDateTime.now();
        if (wordsReviewed > 0) {
            this.accuracyRate = BigDecimal.valueOf(correctAnswers)
                    .divide(BigDecimal.valueOf(wordsReviewed), 2, java.math.RoundingMode.HALF_UP)
                    .multiply(BigDecimal.valueOf(100));
        }
    }
}
