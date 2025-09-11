package com.thanhnb.englishlearningplatform.repository;

import com.thanhnb.englishlearningplatform.entity.UserCourseProgress;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface UserCourseProgressRepository extends JpaRepository<UserCourseProgress, Long> {
    
    // Basic queries
    Optional<UserCourseProgress> findByUserIdAndCourseId(Long userId, Long courseId);
    List<UserCourseProgress> findByUserId(Long userId);
    List<UserCourseProgress> findByUserIdAndStatus(Long userId, UserCourseProgress.Status status);
    
    // Custom queries
    @Query("SELECT p FROM UserCourseProgress p WHERE p.userId = :userId ORDER BY p.startedAt DESC")
    List<UserCourseProgress> findUserProgressOrderByDate(@Param("userId") Long userId);
    
    @Query("SELECT COUNT(p) FROM UserCourseProgress p WHERE p.userId = :userId AND p.status = :status")
    Long countByUserAndStatus(@Param("userId") Long userId, @Param("status") UserCourseProgress.Status status);
    
    @Query("SELECT AVG(p.completionPercentage) FROM UserCourseProgress p WHERE p.userId = :userId")
    Double getAverageCompletionByUser(@Param("userId") Long userId);
}
