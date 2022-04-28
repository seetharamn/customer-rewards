package com.retailer.rewards;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.sql.Date;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.List;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import com.retailer.rewards.bo.RewardsCalculator;
import com.retailer.rewards.model.Purchase;
import com.retailer.rewards.model.Rewards;

public class RewardControllerTest extends RewardsApplicationTests {

	RewardsCalculator rewardsCalculator = new RewardsCalculator();

	@Override
	@Before
	public void setUp() {
		super.setUp();
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

		String inputJson = super.mspToJson(purchases);
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

		Mockito.when(purchaseDAO.findByCustomerId("COO2")).thenReturn(Arrays.asList(p, p2));
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
		assertTrue(r.getTotal() == 110.60 && r.getMonthlyRewards().size()==1);
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
		c.set(Calendar.DAY_OF_MONTH, -1);
		p.setTransactionDate(new Date(c.getTimeInMillis()));
		Mockito.when(purchaseDAO.findByCustomerId("C001")).thenReturn(Arrays.asList(p,p1));
		Rewards r = rewardsCalculator.calucateRewards("C001", Arrays.asList(p,p1));
		assertTrue(r.getTotal() == 141.20 && r.getMonthlyRewards().size()==2);
	}
}
