package com.auction.platform.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.auction.platform.dto.BidRequest;
import com.auction.platform.entity.Bid;
import com.auction.platform.service.BidService;

@RestController
@RequestMapping("/api/bids")
public class BidController {

	@Autowired
	private BidService bidService;

	@PostMapping("/place")
	public Bid placeBid(@RequestBody BidRequest request) {

	    String email = SecurityContextHolder
	            .getContext()
	            .getAuthentication()
	            .getName();

	    return bidService.placeBid(
	            request.getAuctionId(),
	            email,
	            request.getAmount()
	    );
	}


	@GetMapping("/auction/{id}")
	public List<Bid> getBidsByAuction(@PathVariable Long id) {
	    return bidService.getBidsByAuction(id);
	}
	
	@GetMapping("/user/{userId}")
	public List<Bid> getUserBids(@PathVariable Long userId) {
	    return bidService.getUserBids(userId);
	}


}
