package com.thanhnb.englishlearningplatform.repository;

import com.thanhnb.englishlearningplatform.entity.VocabularyWord;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface VocabularyWordRepository extends JpaRepository<VocabularyWord, Long> {
    
    // Basic queries
    List<VocabularyWord> findByTopicId(Long topicId);
    List<VocabularyWord> findByDifficulty(VocabularyWord.Difficulty difficulty);
    Optional<VocabularyWord> findByWord(String word);
    
    // Custom queries
    @Query("SELECT w FROM VocabularyWord w WHERE w.topicId = :topicId ORDER BY w.difficulty ASC, w.word ASC")
    List<VocabularyWord> findWordsByTopicOrdered(@Param("topicId") Long topicId);
    
    @Query("SELECT w FROM VocabularyWord w WHERE w.word LIKE %:searchTerm% OR w.meaningVietnamese LIKE %:searchTerm%")
    List<VocabularyWord> searchWords(@Param("searchTerm") String searchTerm);
    
    @Query("SELECT COUNT(w) FROM VocabularyWord w WHERE w.topicId = :topicId")
    Long countWordsByTopic(@Param("topicId") Long topicId);
    
    @Query("SELECT w FROM VocabularyWord w WHERE w.difficulty = :difficulty ORDER BY RANDOM() LIMIT :limit")
    List<VocabularyWord> findRandomWordsByDifficulty(@Param("difficulty") VocabularyWord.Difficulty difficulty, @Param("limit") int limit);
}
