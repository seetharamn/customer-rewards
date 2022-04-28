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
import lombok.NonNull;


@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Purchase {
	
	@Id
	@GeneratedValue
	@JsonProperty("id")
	private int id;
	
	@JsonProperty("customerId")
	@NonNull
	private String customerId;
	
	@JsonProperty("amount")
	@NonNull
	private double amount;
	
	
	@JsonFormat(pattern = "MM/dd/yyyy")
	@NonNull
	private Date transactionDate;

	public double getAmount() {
		return amount;
	}

	public Date getTransactionDate() {
		return transactionDate;
	}

	public void setCustomerId(String customerId) {
		this.customerId = customerId;
	}

	public void setAmount(double amount) {
		this.amount = amount;
	}

	public void setTransactionDate(Date transactionDate) {
		this.transactionDate = transactionDate;
	}
	
	
	
}
