package com.synergisticit.domain;

import java.util.ArrayList;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;

import jakarta.persistence.Column;
import jakarta.persistence.Embedded;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@NoArgsConstructor
@AllArgsConstructor
@Setter
@Getter
public class Branch {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	//@NotNull
	private Long branchId;
	
	//@NotEmpty
	@Column(name = "branchName")
	private String branchName;
	
	@Valid
	@Embedded
	private Address branchAddress;
	
	//@JsonManagedReference
	@JsonBackReference
	@OneToMany(mappedBy = "accountBranch")
	private List<Account> branchAccounts = new ArrayList<>();
}
