package com.businesseval.domain.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.businesseval.domain.model.User;

@Repository
public interface UserRepository extends JpaRepository<User, Long>{
	List<User> findByNameContains(String name);
	List<User> findByEmailContains(String email);
	List<User> findByEmail(String email);
}
