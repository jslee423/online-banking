package com.synergisticit.domain;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import org.springframework.format.annotation.DateTimeFormat;

import com.fasterxml.jackson.annotation.JsonBackReference;

import jakarta.persistence.Embedded;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToMany;
import jakarta.persistence.OneToOne;
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
public class Customer {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long customerId;
	
	//@NotEmpty
	private String customerName;
	
	@Enumerated(EnumType.STRING)
	private Gender customerGender;
	
	@DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
	private LocalDate customerDOB;
	
	private String customerPhoneNum;
	
	@Valid
	@Embedded
	private Address customerAddress;
	
	private String customerRealId;
	
	@OneToMany(mappedBy = "accountCustomer")
	private List<Account> customerAccounts = new ArrayList<>();
	
	@JsonBackReference
	@OneToOne
	@JoinColumn(name = "userId")
	private User user;
}
