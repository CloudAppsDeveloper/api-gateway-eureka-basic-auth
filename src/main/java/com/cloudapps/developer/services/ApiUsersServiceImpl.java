package com.cloudapps.developer.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.cloudapps.developer.model.ApiUsers;
import com.cloudapps.developer.repo.ApiUsersRepository;

@Service
public class ApiUsersServiceImpl {
	
	@Autowired
	private ApiUsersRepository apiUserRepository;
	
	
	public ApiUsers getApiUser(String clientId,String productId) {
		
		return apiUserRepository.findByClientIdAndProductId(clientId,productId);
	}

}
