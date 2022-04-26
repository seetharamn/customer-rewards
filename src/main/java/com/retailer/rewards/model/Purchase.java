package com.retailer.rewards.model;

import java.sql.Date;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.Setter;


@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Setter
public class Purchase {
	
	@Id
	@GeneratedValue
	@JsonProperty("id")
	private int id;
	
	@JsonProperty("customerId")
	private String customerId;
	
	@JsonProperty("amount")
	private double amount;
	
	@JsonFormat(pattern = "MM/dd/yyyy")
	private Date transactionDate;

	public double getAmount() {
		return amount;
	}

	public Date getTransactionDate() {
		return transactionDate;
	}
	
	
	
}
