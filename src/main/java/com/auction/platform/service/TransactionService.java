package com.auction.platform.service;

import java.util.List;
import java.util.Map;

import com.auction.platform.entity.Transaction;


public interface TransactionService {
	
	List<Transaction> getUserTransaction(long userId);
	
	public Map<Integer, Double> getMonthlyEarnings(long sellerId);

	
	

}
