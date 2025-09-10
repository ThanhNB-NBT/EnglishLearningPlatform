package com.thanhnb.englishlearningplatform.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalTime;

@Entity
@Table(name = "user_settings")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UserSetting {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "user_id", nullable = false)
    private Long userId;

    @Column(name = "daily_goal_words")
    @Builder.Default
    private Integer dailyGoalWords = 10;

    @Column(name = "daily_goal_lessons")
    @Builder.Default
    private Integer dailyGoalLessons = 1;

    @Column(name = "reminder_time")
    @Builder.Default
    private LocalTime reminderTime = LocalTime.of(19, 0);

    @Column(name = "notification_enabled")
    @Builder.Default
    private Boolean notificationEnabled = true;

    // Relationship
    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", insertable = false, updatable = false)
    private User user;
}
