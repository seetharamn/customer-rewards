package com.retailer.rewards.service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

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
		if(purchases==null || purchases.size()==0) throw new CustomerNotFoundException();
		Map<String, Double> monthlyRewards = new HashMap<String, Double>();
		double rewards = 0,total=0;
		for (Purchase p : purchases) {
			if (p.getAmount() > 50) {
				if (p.getAmount() > 100)
					rewards = 50.0 + ((p.getAmount() - 100.0) * 2);
				else
					rewards = p.getAmount()-50.0;
			}
			String month =p.getTransactionDate().toLocalDate().getMonth().toString();
			if(monthlyRewards.get(month)==null)
				monthlyRewards.put(month, rewards);
			else
				monthlyRewards.put(month, monthlyRewards.get(month)+rewards);
			total+=rewards;
			rewards=0;			
		}
		Rewards response= new Rewards(customerId,total,monthlyRewards);		
		return response;
	}

	public void addTransaction(List<Purchase> purchases) {
		purchaseDAO.saveAll(purchases);
	}

	public List<Purchase> getAll() {
		return purchaseDAO.findAll();
	}

}
