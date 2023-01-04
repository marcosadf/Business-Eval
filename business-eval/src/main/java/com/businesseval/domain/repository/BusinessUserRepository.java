package com.businesseval.domain.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.businesseval.domain.model.Business;
import com.businesseval.domain.model.BusinessUser;
import com.businesseval.domain.model.User;

public interface BusinessUserRepository extends JpaRepository<BusinessUser, Long>{
	List<BusinessUser> findByUser(User user);
	List<BusinessUser> findByBusiness(Business business);
}
