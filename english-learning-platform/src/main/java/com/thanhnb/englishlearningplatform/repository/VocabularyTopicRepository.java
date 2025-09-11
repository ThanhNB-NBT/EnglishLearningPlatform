package com.thanhnb.englishlearningplatform.repository;

import com.thanhnb.englishlearningplatform.entity.VocabularyTopic;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface VocabularyTopicRepository extends JpaRepository<VocabularyTopic, Long> {
    
    // Basic queries
    List<VocabularyTopic> findByIsActiveTrueOrderByOrderIndex();
    
    @Query("SELECT t FROM VocabularyTopic t WHERE t.name LIKE %:name% AND t.isActive = true")
    List<VocabularyTopic> searchByName(@Param("name") String name);
    
    @Query("SELECT COUNT(t) FROM VocabularyTopic t WHERE t.isActive = true")
    Long countActiveTopics();
}
