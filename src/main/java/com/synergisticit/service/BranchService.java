package com.synergisticit.service;

import java.util.List;

import com.synergisticit.domain.Branch;

public interface BranchService {

	public Branch saveBranch(Branch branch);
	
	public List<Branch> findAll();
	
	public Branch findById(Long branchId);
	
	public void deleteById(Long branchId);
}
