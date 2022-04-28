package com.retailer.rewards.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.retailer.rewards.bo.RewardsCalculator;
import com.retailer.rewards.dao.PurchaseDAO;
import com.retailer.rewards.exception.CustomerNotFoundException;
import com.retailer.rewards.model.Purchase;
import com.retailer.rewards.model.Rewards;

@Service
public class RewardsService {

	@Autowired
	private PurchaseDAO purchaseDAO;

	public Rewards getRewards(String customerId) {
		List<Purchase> purchases = purchaseDAO.findByCustomerId(customerId);
		if (purchases == null || purchases.size() == 0)
			throw new CustomerNotFoundException();
		return new RewardsCalculator().calucateRewards(customerId, purchases);
	}

	public void addTransaction(List<Purchase> purchases) {
		purchaseDAO.saveAll(purchases);
	}

	public List<Purchase> getAll() {
		return purchaseDAO.findAll();
	}

}
