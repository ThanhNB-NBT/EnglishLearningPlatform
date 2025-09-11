package com.thanhnb.englishlearningplatform.repository;

import com.thanhnb.englishlearningplatform.entity.QuestionOption;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface QuestionOptionRepository extends JpaRepository<QuestionOption, Long> {

    List<QuestionOption> findByIsActiveTrue();

    @Query("SELECT qo FROM QuestionOption qo WHERE qo.questionId = :questionId AND qo.isActive = true")
    List<QuestionOption> findCorrectOptionbyQuestion(@Param("questionId") Long questionId);

    @Query("SELECT o FROM QuestionOption o WHERE o.questionId = :questionId AND o.isCorrect = true")
    Optional<QuestionOption> findCorrectOptionByQuestion(@Param("questionId") Long questionId);

    @Query("SELECT COUNT(o) FROM QuestionOption o WHERE o.questionId = :questionId")
    Long countOptionsByQuestion(@Param("questionId") Long questionId);

    Optional<QuestionOption> findByIdAndIsActiveTrue(Long id);
    
}
