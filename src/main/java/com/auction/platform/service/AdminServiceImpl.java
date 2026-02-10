package com.auction.platform.service;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.auction.platform.dto.AdminStatsResponse;
import com.auction.platform.entity.Auction;
import com.auction.platform.repository.AuctionRepository;
import com.auction.platform.repository.TransactionRepository;
import com.auction.platform.repository.UserRepository;

import jakarta.transaction.Transactional;

@Service
public class AdminServiceImpl implements AdminService {

	@Autowired
	private UserRepository userRepository;

	@Autowired
	private AuctionRepository auctionRepository;

	@Autowired
	private TransactionRepository transactionRepository;

	@Override
	public AdminStatsResponse getStats() {
		long users = userRepository.count();
		long auctions = auctionRepository.count();
		long active = auctionRepository.countByStatus("ACTIVE");
		double revenue = transactionRepository.getPlatformRevenue();
		return new AdminStatsResponse(users, auctions, active, revenue);
	}

	@Override
	public Map<Integer, Double> getMonthlyRevenue() {

	    List<Object[]> rows =
	            transactionRepository.getMonthlyPlatformRevenue();

	    Map<Integer, Double> result = new LinkedHashMap<>();

	    for (Object[] row : rows) {
	        Integer month = (Integer) row[0];
	        Double amount = (Double) row[1];
	        result.put(month, amount);
	    }

	    return result;
	}

	

}
