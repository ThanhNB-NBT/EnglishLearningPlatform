package com.thanhnb.englishlearningplatform.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "user_speaking_attempts")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UserSpeakingAttempt {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "user_id", nullable = false)
    private Long userId;

    @Column(name = "speaking_topic_id", nullable = false)
    private Long speakingTopicId;

    @Column(name = "audio_file_url")
    private String audioFileUrl;

    @Column(name = "transcription", columnDefinition = "TEXT")
    private String transcription;

    @Column(name = "pronunciation_score", precision = 5, scale = 2)
    private BigDecimal pronunciationScore;

    @Column(name = "fluency_score", precision = 5, scale = 2)
    private BigDecimal fluencyScore;

    @Column(name = "overall_score", precision = 5, scale = 2)
    private BigDecimal overallScore;

    @Column(name = "feedback", columnDefinition = "TEXT")
    private String feedback;

    @Column(name = "duration_seconds")
    private Integer durationSeconds;

    @Column(name = "attempted_at", nullable = false)
    private LocalDateTime attemptedAt;

    // Relationships
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", insertable = false, updatable = false)
    private User user;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "speaking_topic_id", insertable = false, updatable = false)
    private SpeakingTopic speakingTopic;

    @PrePersist
    protected void onCreate() {
        this.attemptedAt = LocalDateTime.now();
    }
}
