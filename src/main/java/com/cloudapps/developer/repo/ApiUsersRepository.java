package com.cloudapps.developer.repo;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.cloudapps.developer.model.ApiUsers;
@Repository
public interface ApiUsersRepository extends JpaRepository<ApiUsers, Long>{

	public ApiUsers findByClientIdAndProductId(String clientId, String productId);

}
