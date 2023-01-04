package com.businesseval.domain.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.businesseval.domain.model.Question;

public interface QuestionRepository extends JpaRepository<Question, Long>{
	List<Question> findByDescriptionContains(String description);
	List<Question> findByDescription(String description);
}
