package com.retailer.rewards;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.io.IOException;
import java.sql.Date;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.List;

import org.junit.Before;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.retailer.rewards.bo.RewardsCalculator;
import com.retailer.rewards.model.Purchase;
import com.retailer.rewards.model.Rewards;

public class RewardsTest extends RewardsApplicationTests{

	
	
	RewardsCalculator rewardsCalculator = new RewardsCalculator();
	
	@Override
	@Before
	public void setUp() {
		super.setUp();
	}

	protected String mspToJson(Object obj) throws JsonProcessingException {
		return objectMapper.writeValueAsString(obj);
	}

	protected <T> T mapFromJson(String json, Class<T> clazz)
			throws JsonParseException, JsonMappingException, IOException {
		return objectMapper.readValue(json, clazz);
	}

	@Test
	public void getPurchases() throws Exception {
		Purchase p = new Purchase();
		p.setAmount(120.30);
		p.setCustomerId("C002");
		p.setTransactionDate(new Date(System.currentTimeMillis()));
		Purchase p1 = new Purchase();
		p1.setAmount(120.30);
		p1.setCustomerId("C001");
		p1.setTransactionDate(new Date(System.currentTimeMillis()));
		Purchase p2 = new Purchase();
		p2.setAmount(630.91);
		p2.setCustomerId("C002");
		p2.setTransactionDate(new Date(System.currentTimeMillis()));

		Mockito.when(purchaseDAO.findAll()).thenReturn(Arrays.asList(p, p1, p2));

		MvcResult result = mvc
				.perform(MockMvcRequestBuilders.get("/api/purchases").accept(MediaType.APPLICATION_JSON_VALUE))
				.andReturn();
		String content = result.getResponse().getContentAsString();
		Purchase[] purchaseList = mapFromJson(content, Purchase[].class);
		assertTrue(purchaseList.length > 0);
	}

	@Test
	public void putTransactions_InvalidOrNull() throws Exception {
		mvc.perform(MockMvcRequestBuilders.post("/api/purchases").contentType(MediaType.APPLICATION_JSON_VALUE))
				.andExpect(status().isBadRequest());
	}

	@Test
	public void putTransactions() throws Exception {
		String uri = "/api/purchases";
		Purchase p = new Purchase();
		p.setAmount(120.30);
		p.setCustomerId("C002");
		p.setTransactionDate(new Date(System.currentTimeMillis()));
		Purchase p1 = new Purchase();
		p1.setAmount(120.30);
		p1.setCustomerId("C001");
		p1.setTransactionDate(new Date(System.currentTimeMillis()));
		Purchase p2 = new Purchase();
		p2.setAmount(630.91);
		p2.setCustomerId("C002");
		p2.setTransactionDate(new Date(System.currentTimeMillis()));

		List<Purchase> purchases = Arrays.asList(p, p1, p2);
		Mockito.when(purchaseDAO.saveAll(purchases)).thenReturn(purchases);

		String inputJson = mspToJson(purchases);
		MvcResult result = mvc.perform(
				MockMvcRequestBuilders.post(uri).contentType(MediaType.APPLICATION_JSON_VALUE).content(inputJson))
				.andReturn();
		int status = result.getResponse().getStatus();
		assertEquals(200, status);

	}

	@Test
	public void getRewards() throws Exception {
		Purchase p = new Purchase();
		p.setAmount(120.30);
		p.setCustomerId("C002");
		p.setTransactionDate(new Date(System.currentTimeMillis()));
		Purchase p1 = new Purchase();
		p1.setAmount(120.30);
		p1.setCustomerId("C001");
		p1.setTransactionDate(new Date(System.currentTimeMillis()));
		Purchase p2 = new Purchase();
		p2.setAmount(630.91);
		p2.setCustomerId("C002");
		p2.setTransactionDate(new Date(System.currentTimeMillis()));

		Mockito.when(purchaseDAO.findByCustomerId("C002")).thenReturn(Arrays.asList(p, p2));
		MvcResult result = mvc
				.perform(MockMvcRequestBuilders.get("/api/rewards/C002").accept(MediaType.APPLICATION_JSON_VALUE))
				.andReturn();
		String content = result.getResponse().getContentAsString();
		Rewards rewards = mapFromJson(content, Rewards.class);
		assertTrue(rewards != null);
	}

	@Test
	public void getRewards_invalidcustomer() throws Exception {
		Mockito.when(purchaseDAO.findByCustomerId("COO3")).thenReturn(null);
		MvcResult result = mvc
				.perform(MockMvcRequestBuilders.get("/api/rewards/C003").accept(MediaType.APPLICATION_JSON_VALUE))
				.andReturn();
		int status = result.getResponse().getStatus();
		assertEquals(status, 400);
		String content = result.getResponse().getContentAsString();
		assertEquals("Customer Not Found", content);
	}

	@Test
	public void caluclateRewardsWithEmpty() {
		Mockito.when(purchaseDAO.findByCustomerId("C001")).thenReturn(new ArrayList<Purchase>());
		Rewards r = rewardsCalculator.calucateRewards("C001", new ArrayList<Purchase>());
		assertTrue(r.getTotal() == 0.0 && r.getMonthlyRewards().isEmpty());
	}
	
	@Test
	public void caluclateRewardsWithNullDate() {
		Purchase p = new Purchase();
		p.setAmount(120.30);
		p.setCustomerId("C001");
		p.setTransactionDate(null);
		Mockito.when(purchaseDAO.findByCustomerId("C001")).thenReturn(Arrays.asList(p));
		Rewards r = rewardsCalculator.calucateRewards("C001", new ArrayList<Purchase>());
		assertTrue(r.getTotal() == 0.0 && r.getMonthlyRewards().isEmpty());
	}
	
	@Test
	public void caluclateRewardsWithOneRecord() {
		Purchase p = new Purchase();
		p.setAmount(120.30);
		p.setCustomerId("C001");
		p.setTransactionDate(new Date(System.currentTimeMillis()));
		Mockito.when(purchaseDAO.findByCustomerId("C001")).thenReturn(Arrays.asList(p));
		Rewards r = rewardsCalculator.calucateRewards("C001", Arrays.asList(p));
		assertTrue(r.getTotal() == 90.60 && r.getMonthlyRewards().size()==1);
	}
	
	@Test
	public void caluclateRewardsWithOneRecordWithAmountUnder50() {
		Purchase p = new Purchase();
		p.setAmount(40.0);
		p.setCustomerId("C001");
		p.setTransactionDate(new Date(System.currentTimeMillis()));
		Mockito.when(purchaseDAO.findByCustomerId("C001")).thenReturn(Arrays.asList(p));
		Rewards r = rewardsCalculator.calucateRewards("C001", Arrays.asList(p));
		assertTrue(r.getTotal() == 0.0 && r.getMonthlyRewards().size()==1);
	}
	
	@Test
	public void caluclateRewardsWithTwoRecords() {
		Purchase p = new Purchase();
		p.setAmount(80.6);
		p.setCustomerId("C001");
		p.setTransactionDate(new Date(System.currentTimeMillis()));
		Purchase p1 = new Purchase();
		p1.setAmount(120.30);
		p1.setCustomerId("C001");
		Calendar c=Calendar.getInstance();
		c.set(Calendar.MONTH, -1);
		p1.setTransactionDate(new Date(c.getTimeInMillis()));
		Mockito.when(purchaseDAO.findByCustomerId("C001")).thenReturn(Arrays.asList(p,p1));
		Rewards r = rewardsCalculator.calucateRewards("C001", Arrays.asList(p,p1));
		assertTrue(r.getMonthlyRewards().size()==2);
	}
	
	@Test
	public void calculateRewardspurchasesMadeInLastThreeMonths() throws Exception{
		String json="[{ \"customerId\":\"C001\", \"amount\":\"120.30\", \"transactionDate\":\"04/25/2022\" },{ \"customerId\":\"C001\", \"amount\":\"598.10\", \"transactionDate\":\"04/20/2022\" },{ \"customerId\":\"C001\", \"amount\":\"150.00\", \"transactionDate\":\"03/10/2022\" },{ \"customerId\":\"C001\", \"amount\":\" 50.00\", \"transactionDate\":\"03/30/2022\" },{ \"customerId\":\"C001\", \"amount\":\"50.40\", \"transactionDate\":\"03/05/2022\" },{ \"customerId\":\"C001\", \"amount\":\"72.60\", \"transactionDate\":\"02/10/2022\" },{ \"customerId\":\"C001\", \"amount\":\"117.30\", \"transactionDate\":\"04/25/2022\" },{ \"customerId\":\"C001\", \"amount\":\"99.30\", \"transactionDate\":\"04/15/2022\" },{ \"customerId\":\"C001\", \"amount\":\"1017.30\", \"transactionDate\":\"03/20/2022\" },{ \"customerId\":\"C001\", \"amount\":\"315.99\", \"transactionDate\":\"02/25/2022\" },{ \"customerId\":\"C001\", \"amount\":\"119.67\", \"transactionDate\":\"03/05/2022\" }, { \"customerId\":\"C001\", \"amount\":\"617.39\", \"transactionDate\":\"02/28/2022\" }]";
		Purchase[] purchases=mapFromJson(json, Purchase[].class);
		Mockito.when(purchaseDAO.findByCustomerId("C001")).thenReturn(Arrays.asList(purchases));
		Rewards r = rewardsCalculator.calucateRewards("C001", Arrays.asList(purchases));
		assertTrue(r.getTotal()==4984.4 && r.getMonthlyRewards().size()==3);
	
	}
}
