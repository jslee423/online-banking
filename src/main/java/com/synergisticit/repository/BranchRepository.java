package com.synergisticit.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.synergisticit.domain.Branch;

public interface BranchRepository extends JpaRepository<Branch, Long> {

}
