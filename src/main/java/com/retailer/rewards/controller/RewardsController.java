package com.retailer.rewards.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.retailer.rewards.exception.CustomerNotFoundException;
import com.retailer.rewards.model.Purchase;
import com.retailer.rewards.model.Rewards;
import com.retailer.rewards.service.RewardsService;

@RequestMapping("api")
@RestController
public class RewardsController {
	
	private final RewardsService service;
	
	@Autowired
	public RewardsController(RewardsService rewardsService) {
		this.service=rewardsService;
	}
	
	
	@ExceptionHandler(CustomerNotFoundException.class)
	public ResponseEntity<Object> handleExceptions(CustomerNotFoundException exception) {
		return new ResponseEntity<>("Customer Not Found",HttpStatus.BAD_REQUEST);
	}
	
	@PostMapping("/purchases")
	public void createTransaction(@RequestBody List<Purchase> purchases) {
		service.addTransaction(purchases);
	}
	
	@GetMapping("/purchases")
	public List<Purchase> getAll(){ 
		return service.getAll();
	}
	
	@GetMapping(path = "/rewards/{customerId}")
	public Rewards getRewards(@PathVariable(name = "customerId") String customerId) {
		return service.getRewards(customerId);
	}

}
