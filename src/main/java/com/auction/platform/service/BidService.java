package com.auction.platform.service;

import java.util.List;

import com.auction.platform.entity.Bid;

public interface BidService {
	public Bid placeBid(Long auctionId, String email, double amount);
	public List<Bid> getBidsByAuction(Long auctionId);
	public List<Bid> getUserBids(Long userId);
}
