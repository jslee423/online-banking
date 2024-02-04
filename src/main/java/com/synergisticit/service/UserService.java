package com.synergisticit.service;

import java.util.List;

import com.synergisticit.domain.User;

public interface UserService {
	
	public User saveUser(User user); 
	
	public List<User> findAll();
	
	public User findById(Long userId);
	
	public void deleteById(Long userId);
	
	public User findUserByUsername(String username);
}
