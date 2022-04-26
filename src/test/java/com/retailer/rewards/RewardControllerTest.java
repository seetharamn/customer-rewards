package com.retailer.rewards;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.sql.Date;
import java.util.ArrayList;
import java.util.List;

import org.junit.Before;
import org.junit.Test;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import com.retailer.rewards.model.Purchase;
import com.retailer.rewards.model.Rewards;

public class RewardControllerTest extends RewardsApplicationTests {

	@Override
	@Before
	public void setUp() {
		super.setUp();
	}

	@Test
	public void getPurchases() throws Exception {
		String uri = "/api/purchases";
		MvcResult result = mvc.perform(MockMvcRequestBuilders.get(uri).accept(MediaType.APPLICATION_JSON_VALUE))
				.andReturn();
		String content = result.getResponse().getContentAsString();
		Purchase[] purchaseList = mapFromJson(content, Purchase[].class);
		assertTrue(purchaseList.length > 0);
	}

	public void putTransactions() throws Exception {
		String uri = "/api/purchases";
		List<Purchase> list = new ArrayList<Purchase>();
		Purchase p = new Purchase();
		p.setAmount(120.30);
		p.setCustomerId("C002");
		p.setTransactionDate(new Date(System.currentTimeMillis()));
		list.add(p);
		p = new Purchase();
		p.setAmount(120.30);
		p.setCustomerId("C001");
		p.setTransactionDate(new Date(System.currentTimeMillis()));
		list.add(p);
		p = new Purchase();
		p.setAmount(630.91);
		p.setCustomerId("C002");
		p.setTransactionDate(new Date(System.currentTimeMillis()));
		list.add(p);

		String inputJson = super.mspToJson(list);
		MvcResult result = mvc.perform(
				MockMvcRequestBuilders.post(uri).contentType(MediaType.APPLICATION_JSON_VALUE).content(inputJson))
				.andReturn();
		int status =result.getResponse().getStatus();
		assertEquals(200, status);

	}

	@Test
	public void getRewards() throws Exception {
		String uri = "/api/rewards/C002";
		MvcResult result = mvc.perform(MockMvcRequestBuilders.get(uri).accept(MediaType.APPLICATION_JSON_VALUE))
				.andReturn();
		String content = result.getResponse().getContentAsString();
		Rewards rewards = mapFromJson(content, Rewards.class);
		assertTrue(rewards != null);
	}
}
