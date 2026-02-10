package com.auction.platform.repository;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.auction.platform.entity.Auction;

public interface AuctionRepository extends JpaRepository<Auction, Long> {
	
	List<Auction> findByStatusAndEndTimeBefore(String status, LocalDateTime time);
	
	long countByStatus(String status);

}
