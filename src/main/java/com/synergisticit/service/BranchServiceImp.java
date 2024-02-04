package com.synergisticit.service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.synergisticit.domain.Branch;
import com.synergisticit.repository.BranchRepository;

@Service(value = "branchService")
public class BranchServiceImp implements BranchService {
	
	@Autowired BranchRepository branchRepository;

	@Override
	public Branch saveBranch(Branch branch) {
		return branchRepository.save(branch);
	}

	@Override
	public List<Branch> findAll() {
		return branchRepository.findAll();
	}

	@Override
	public Branch findById(Long branchId) {
		Optional<Branch> optBranch = branchRepository.findById(branchId);
		if (optBranch.isPresent()) {
			return optBranch.get();
		}
		return null;
	}

	@Override
	public void deleteById(Long branchId) {
		branchRepository.deleteById(branchId);
		
	}

}
