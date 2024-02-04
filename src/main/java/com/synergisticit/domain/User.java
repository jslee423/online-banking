package com.synergisticit.domain;

import java.util.ArrayList;
import java.util.List;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import jakarta.validation.constraints.Email;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "user")
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class User {
	@Id
	//@GeneratedValue(strategy = GenerationType.IDENTITY)
	//@NotNull
	private Long userId;
	
	//@NotEmpty
	@Column(name = "name")
	private String username;
	
	//@NotEmpty
	private String password;
	
	@Email
	//@NotEmpty
	private String email;
	
	@ManyToMany(fetch = FetchType.EAGER)
	@JoinTable(
			name = "user_role",
			joinColumns = {@JoinColumn(name="user_id")},
			inverseJoinColumns = {@JoinColumn(name="role_id")}
	)
	private List<Role> roles = new ArrayList<>(); 
	
	@OneToOne(cascade = CascadeType.ALL) //fix error org.hibernate.TransientPropertyValueException: object references an unsaved transient instance - save the transient instance before flushing
	@JoinColumn(name = "customerId")
	private Customer customer;
}
