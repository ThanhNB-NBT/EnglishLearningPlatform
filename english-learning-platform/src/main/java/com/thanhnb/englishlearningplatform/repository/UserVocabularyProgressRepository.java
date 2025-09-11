package com.thanhnb.englishlearningplatform.repository;

import com.thanhnb.englishlearningplatform.entity.UserVocabularyProgress;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Repository
public interface UserVocabularyProgressRepository extends JpaRepository<UserVocabularyProgress, Long> {
    
    // Basic queries
    Optional<UserVocabularyProgress> findByUserIdAndWordId(Long userId, Long wordId);
    List<UserVocabularyProgress> findByUserId(Long userId);
    List<UserVocabularyProgress> findByUserIdAndMasteryLevel(Long userId, UserVocabularyProgress.MasteryLevel masteryLevel);
    
    // Custom queries
    @Query("SELECT p FROM UserVocabularyProgress p WHERE p.userId = :userId AND p.nextReviewAt <= :now")
    List<UserVocabularyProgress> findWordsForReview(@Param("userId") Long userId, @Param("now") LocalDateTime now);
    
    @Query("SELECT COUNT(p) FROM UserVocabularyProgress p WHERE p.userId = :userId AND p.masteryLevel = :level")
    Long countWordsByMasteryLevel(@Param("userId") Long userId, @Param("level") UserVocabularyProgress.MasteryLevel level);
    
    @Query("SELECT p FROM UserVocabularyProgress p WHERE p.userId = :userId ORDER BY p.lastReviewedAt DESC")
    List<UserVocabularyProgress> findRecentlyReviewedWords(@Param("userId") Long userId);
}
