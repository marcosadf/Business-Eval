package com.businesseval.domain.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.businesseval.domain.model.Answer;
import com.businesseval.domain.model.BusinessUser;

public interface AnswerRepository extends JpaRepository<Answer, Long>{
	List<Answer> findByBusinessUser(BusinessUser businessUser);
}