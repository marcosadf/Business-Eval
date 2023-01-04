package com.businesseval.domain.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.businesseval.domain.model.Category;

@Repository
public interface CategoryRepository extends JpaRepository<Category, Long>{
	List<Category> findByNameContains(String Name);
	List<Category> findByName(String name);
}
