package com.thanhnb.englishlearningplatform.repository;

import com.thanhnb.englishlearningplatform.entity.Skill;
import org.springframework.stereotype.Repository;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

@Repository
public interface SkillRepository extends JpaRepository<Skill, Long> {
    Optional<Skill> findByName(String name);

    List<Skill> findByIsActiveTrue();

    @Query("SELECT s FROM Skill s WHERE s.isActive = true ORDER BY s.name ASC")
    List<Skill> findAllActiveSkillsOrderedByName();

    @Query("SELECT COUNT(s) FROM Skill s WHERE s.isActive = true")
    Long countActiveSkills();
}
