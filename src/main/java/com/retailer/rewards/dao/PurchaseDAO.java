package com.retailer.rewards.dao;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.retailer.rewards.model.Purchase;

public interface PurchaseDAO extends JpaRepository<Purchase, Integer> {

	List<Purchase> findByCustomerId(String customerId);

}
