package com.retailer.rewards.bo;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.retailer.rewards.model.Purchase;
import com.retailer.rewards.model.Rewards;

public class RewardsCalculator {

	public Rewards calucateRewards(String customerId, List<Purchase> purchases) {
		Map<String, Double> monthlyRewards = new HashMap<String, Double>();
		double rewards = 0, total = 0;
		for (Purchase p : purchases) {
			if (p.getTransactionDate() != null) {
				if (p.getAmount() > 50) {
					if (p.getAmount() > 100)
						rewards = 50.0 + ((p.getAmount() - 100.0) * 2);
					else
						rewards = p.getAmount() - 50.0;
				}
				String month = p.getTransactionDate().toLocalDate().getMonth().toString();
				if (monthlyRewards.get(month) == null)
					monthlyRewards.put(month, rewards);
				else
					monthlyRewards.put(month, monthlyRewards.get(month) + rewards);
				total += rewards;
				rewards = 0;
			}
		}
		Rewards response = new Rewards(customerId, total, monthlyRewards);
		return response;
	}

}
