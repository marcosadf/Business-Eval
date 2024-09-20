package com.businesseval.domain.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.businesseval.domain.model.Timeline;

public interface TimelineRepository extends JpaRepository<Timeline, Long>{
	List<Timeline> findByDescriptionContains(String description);
	List<Timeline> findByDescription(String description);
}
