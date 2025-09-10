package com.thanhnb.englishlearningplatform.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Table(name = "user_vocabulary_progress", uniqueConstraints = {
    @UniqueConstraint(columnNames = {"user_id", "word_id"})
})
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UserVocabularyProgress {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "user_id", nullable = false)
    private Long userId;

    @Column(name = "word_id", nullable = false)
    private Long wordId;

    @Enumerated(EnumType.STRING)
    @Column(length = 20)
    @Builder.Default
    private MasteryLevel masteryLevel = MasteryLevel.NEW;

    @Column(name = "correct_count")
    @Builder.Default
    private Integer correctCount = 0;

    @Column(name = "incorrect_count")
    @Builder.Default
    private Integer incorrectCount = 0;

    @Column(name = "last_reviewed_at")
    private LocalDateTime lastReviewedAt;

    @Column(name = "next_review_at")
    private LocalDateTime nextReviewAt;

    // Relationships
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", insertable = false, updatable = false)
    private User user;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "word_id", insertable = false, updatable = false)
    private VocabularyWord word;

    public enum MasteryLevel {
        NEW,
        LEARNING,
        FAMILIAR,
        MASTERED
    }
}
