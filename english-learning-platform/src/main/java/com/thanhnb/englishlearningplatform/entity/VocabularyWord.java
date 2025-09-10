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
@Table(name = "vocabulary_words")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class VocabularyWord {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "Từ vựng không được để trống")
    @Size(max = 100, message = "Từ vựng không quá 100 ký tự")
    @Column(nullable = false, length = 100)
    private String word;

    @Size(max = 100, message = "Phiên âm không quá 100 ký tự")
    @Column(length = 100)
    private String pronunciation;

    @NotBlank(message = "Loại từ không được để trống")
    @Column(name = "part_of_speech", nullable = false, length = 20)
    private String partOfSpeech;

    @NotBlank(message = "Nghĩa tiếng Việt không được để trống")
    @Column(name = "meaning_vietnamese", nullable = false, columnDefinition = "TEXT")
    private String meaningVietnamese;

    @Column(name = "example_sentence", columnDefinition = "TEXT")
    private String exampleSentence;

    @Column(name = "audio_url")
    private String audioUrl;

    @Column(name = "topic_id")
    private Long topicId;

    @Enumerated(EnumType.STRING)
    @Column(length = 20)
    @Builder.Default
    private Difficulty difficulty = Difficulty.MEDIUM;

    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;

    // Relationships
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "topic_id", insertable = false, updatable = false)
    private VocabularyTopic topic;

    @OneToMany(mappedBy = "word", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<UserVocabularyProgress> userProgresses;

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
