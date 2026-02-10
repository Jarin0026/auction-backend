package com.auction.platform.service;

import java.util.List;

import com.auction.platform.dto.PaymentVerifyRequest;
import com.auction.platform.entity.Auction;
import com.auction.platform.entity.Bid;
import com.auction.platform.entity.Payment;

public interface PaymentService {
	void settleAuctionPayment(Auction auction, Bid highestBid);
	
	String createOrder(double amount);
	public void verifyAndCredit(PaymentVerifyRequest request);
	
	List<Payment> getSellerPayments(String email);
}
