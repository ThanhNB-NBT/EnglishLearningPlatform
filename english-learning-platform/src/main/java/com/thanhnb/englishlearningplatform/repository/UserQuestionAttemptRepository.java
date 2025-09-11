package com.thanhnb.englishlearningplatform.repository;

import com.thanhnb.englishlearningplatform.entity.UserQuestionAttempt;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface UserQuestionAttemptRepository extends JpaRepository<UserQuestionAttempt, Long> {
    
    // Basic queries
    List<UserQuestionAttempt> findByUserIdAndQuestionId(Long userId, Long questionId);
    List<UserQuestionAttempt> findByUserIdOrderByAttemptedAtDesc(Long userId);
    List<UserQuestionAttempt> findByQuestionId(Long questionId);
    
    // Custom queries
    @Query("SELECT COUNT(a) FROM UserQuestionAttempt a WHERE a.userId = :userId AND a.isCorrect = true")
    Long countCorrectAnswersByUser(@Param("userId") Long userId);
    
    @Query("SELECT COUNT(a) FROM UserQuestionAttempt a WHERE a.userId = :userId")
    Long countTotalAttemptsByUser(@Param("userId") Long userId);
    
    @Query("SELECT a FROM UserQuestionAttempt a WHERE a.userId = :userId AND a.attemptedAt >= :fromDate")
    List<UserQuestionAttempt> findUserAttemptsFromDate(@Param("userId") Long userId, @Param("fromDate") LocalDateTime fromDate);
}
