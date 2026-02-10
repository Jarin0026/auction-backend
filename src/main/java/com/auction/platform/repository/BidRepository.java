package com.auction.platform.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.auction.platform.entity.Auction;
import com.auction.platform.entity.Bid;

public interface BidRepository extends JpaRepository<Bid, Long> {
	List<Bid> findByAuctionId(Long auctionId);

    Bid findTopByAuctionOrderByAmountDesc(Auction auction);
    
    List<Bid> findByBidderIdOrderByBidTimeDesc(Long id);



}
