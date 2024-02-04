package com.synergisticit.domain;

import java.time.LocalDateTime;

import org.springframework.format.annotation.DateTimeFormat;

import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@NoArgsConstructor
@AllArgsConstructor
@Setter
@Getter
public class BankTransaction {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long bankTransactionId;
	
	private Long bankTransactionFromAccount;
	
	private Long bankTransactionToAccount;
	
	private double bankTransactionAmount;
	
	@Enumerated(EnumType.STRING)
	private TransactionType bankTransactionType;
	
	@DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
	private LocalDateTime bankTransactionDateTime;
	
	private String comment;
}
