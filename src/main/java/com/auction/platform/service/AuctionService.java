package com.auction.platform.service;

import java.util.List;

import com.auction.platform.entity.Auction;

public interface AuctionService {
	
	Auction createAuction(Auction auction);
	Auction createAuctionWithSeller(Auction auction, String email);

	
	public List<Auction> getAllAuctions();
	
	public Auction getAuctionById(Long id);
	
	public void forceCloseAuction(long id);
	
	public void deleteAuction(long id);
	
	void settleAuction(Auction auction);

	
}
