package com.auction.platform.service;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.auction.platform.entity.Transaction;
import com.auction.platform.entity.User;
import com.auction.platform.repository.TransactionRepository;
import com.auction.platform.repository.UserRepository;

@Service
public class TransactionServiceImpl implements TransactionService {
	
	@Autowired
	private TransactionRepository transactionRepository;
	
	@Autowired
	private UserRepository userRepository;

	@Override
	public List<Transaction> getUserTransaction(long userId) {

	    User user = userRepository.findById(userId)
	            .orElseThrow(() -> new RuntimeException("User not found"));

	    return transactionRepository.findByUserOrderByTimeDesc(user);
	}

	@Override
	public Map<Integer, Double> getMonthlyEarnings(long sellerId) {
		List<Object[]> data = transactionRepository.getMonthlyEarnings(sellerId);

	    Map<Integer, Double> result = new LinkedHashMap<>();

	    for (Object[] row : data) {
	        result.put(
	            ((Number) row[0]).intValue(),
	            ((Number) row[1]).doubleValue()
	        );
	    }

	    return result;
	}

}
