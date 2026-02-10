package com.auction.platform.controller;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.auction.platform.entity.Transaction;
import com.auction.platform.service.TransactionService;

@RestController
@RequestMapping("/api/transactions")
public class TransactionController {

	@Autowired
	private TransactionService transactionService;
	
	@GetMapping("/user/{userId}")
	public List<Transaction> getUserTransaction(@PathVariable long userId){
		return transactionService.getUserTransaction(userId);
	}
	
	@GetMapping("/seller/monthly/{sellerId}")
	public Map<Integer, Double> monthly(@PathVariable long sellerId) {
	    return transactionService.getMonthlyEarnings(sellerId);
	}

}
