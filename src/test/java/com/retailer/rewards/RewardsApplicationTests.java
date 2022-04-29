package com.retailer.rewards;

import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.retailer.rewards.dao.PurchaseDAO;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = RewardsApplication.class)
@WebAppConfiguration
@AutoConfigureMockMvc
class RewardsApplicationTests {
	
	@Autowired
	WebApplicationContext webApplicationContext;
	
	@Autowired
	ObjectMapper objectMapper;
	
	@Autowired
	protected MockMvc mvc;
	
	@MockBean
	@Autowired
	PurchaseDAO purchaseDAO;
	
	protected void setUp() {
		//mvc = MockMvcBuilders.webAppContextSetup(webApplicationContext).build();
	}
	
	@Test
	void contextLoads() {
		
	}

}
