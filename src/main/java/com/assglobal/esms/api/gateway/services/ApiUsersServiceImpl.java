package com.assglobal.esms.api.gateway.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.assglobal.esms.api.gateway.model.ApiUsers;
import com.assglobal.esms.api.gateway.repo.ApiUsersRepository;

@Service
public class ApiUsersServiceImpl {
	
	@Autowired
	private ApiUsersRepository apiUserRepository;
	
	
	public ApiUsers getApiUser(String clientId,String productId) {
		
		return apiUserRepository.findByClientIdAndProductId(clientId,productId);
	}

}
