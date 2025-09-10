package com.thanhnb.englishlearningplatform.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import jakarta.validation.constraints.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "reading_passages")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ReadingPassage {
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
    @Column(length = 20)
    @Builder.Default
    private Level level = Level.BEGINNER;

    @Size(max = 50, message = "Thể loại không quá 50 ký tự")
    @Column(length = 50)
    private String genre;

    @Column(name = "is_active")
    @Builder.Default
    private Boolean isActive = true;

    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @PrePersist
    protected void onCreate() {
        this.createdAt = LocalDateTime.now();
    }

    public enum Level {
        BEGINNER,
        INTERMEDIATE,
        ADVANCED
    }
}
