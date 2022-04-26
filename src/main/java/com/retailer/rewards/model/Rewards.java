package com.retailer.rewards.model;

import java.util.Map;

import com.fasterxml.jackson.annotation.JsonProperty;


public class Rewards {
	
	@JsonProperty("customerId")
	private String customerId;
	
	@JsonProperty("total")
	private double total;
	
	@JsonProperty("mothlyRewards")
	private Map<String, Double> monthlyRewards;

	public Rewards(String customerId, double total, Map<String, Double> monthlyRewards) {
		this.customerId = customerId;
		this.total = total;
		this.monthlyRewards = monthlyRewards;
	}
	
	

	
}
