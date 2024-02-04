package com.synergisticit.service;

import java.util.List;

import com.synergisticit.domain.Role;

public interface RoleService {

	public Role saveRole(Role role); 
	
	public List<Role> findAll();
	
	public Role findById(Long roleId);
	
	public void deleteById(Long roleId);
}
