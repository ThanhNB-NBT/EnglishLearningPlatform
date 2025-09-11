package com.thanhnb.englishlearningplatform.repository;

import com.thanhnb.englishlearningplatform.entity.UserLessonProgress;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface UserLessonProgressRepository extends JpaRepository<UserLessonProgress, Long> {
    
    // Basic queries
    Optional<UserLessonProgress> findByUserIdAndLessonId(Long userId, Long lessonId);
    List<UserLessonProgress> findByUserId(Long userId);
    List<UserLessonProgress> findByUserIdAndStatus(Long userId, UserLessonProgress.Status status);
    List<UserLessonProgress> findByUserIdAndIsPassed(Long userId, Boolean isPassed);
    
    // Custom queries
    @Query("SELECT p FROM UserLessonProgress p WHERE p.userId = :userId ORDER BY p.lastAccessedAt DESC")
    List<UserLessonProgress> findUserProgressOrderByLastAccessed(@Param("userId") Long userId);
    
    @Query("SELECT COUNT(p) FROM UserLessonProgress p WHERE p.userId = :userId AND p.isPassed = true")
    Long countPassedLessonsByUser(@Param("userId") Long userId);
    
    @Query("SELECT AVG(p.score) FROM UserLessonProgress p WHERE p.userId = :userId AND p.score IS NOT NULL")
    Double getAverageScoreByUser(@Param("userId") Long userId);
}
