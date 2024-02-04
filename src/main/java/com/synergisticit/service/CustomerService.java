package com.synergisticit.service;

import java.util.List;

import com.synergisticit.domain.Customer;

public interface CustomerService {
	
	public Customer saveCustomer(Customer customer);
	
	public List<Customer> findAll();
	
	public Customer findById(Long customerId);
	
	public void deleteById(Long customerId);
}
