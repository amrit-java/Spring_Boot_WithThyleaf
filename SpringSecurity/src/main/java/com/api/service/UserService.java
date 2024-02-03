package com.api.service;

import org.springframework.security.core.userdetails.UserDetailsService;

import com.api.model.User;
import com.api.model.UserRegistrationDto;

public interface UserService extends UserDetailsService{
	User save(UserRegistrationDto registrationDto);
}
