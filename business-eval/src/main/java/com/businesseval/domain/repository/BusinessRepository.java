package com.businesseval.domain.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.businesseval.domain.model.Business;
import com.businesseval.domain.model.User;

@Repository
public interface BusinessRepository extends JpaRepository<Business, Long>{
	List<Business> findByName(String name);
	List<Business> findByNameContains(String name);
	List<Business> findByCnpjCpf(String cnpjCpf);
	List<Business> findByCnpjCpfContains(String cnpjCpf);
	List<Business> findByManager(User manager);
}
