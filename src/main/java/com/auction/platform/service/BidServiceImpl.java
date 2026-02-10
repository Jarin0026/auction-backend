package com.auction.platform.service;

import java.time.LocalDateTime;
import org.springframework.messaging.simp.SimpMessagingTemplate;

import java.util.List;

import javax.management.RuntimeErrorException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.auction.platform.entity.Auction;
import com.auction.platform.entity.Bid;
import com.auction.platform.entity.User;
import com.auction.platform.repository.AuctionRepository;
import com.auction.platform.repository.BidRepository;
import com.auction.platform.repository.UserRepository;

import jakarta.transaction.Transactional;

@Service
public class BidServiceImpl implements BidService {

	@Autowired
	private BidRepository bidRepository;

	@Autowired
	private AuctionRepository auctionRepository;

	@Autowired
	private UserRepository userRepository;

	@Autowired
	private WalletService walletService;

	@Autowired
	private SimpMessagingTemplate messagingTemplate;

	@Override
	@Transactional
	public Bid placeBid(Long auctionId, String email, double amount) {

	    Auction auction = auctionRepository.findById(auctionId)
	            .orElseThrow(() -> new RuntimeException("Auction not found"));

	    if (!auction.getStatus().equals("ACTIVE")) {
	        throw new RuntimeException("Auction is closed. Bidding not allowed.");
	    }

	    User user = userRepository.findByEmail(email);
	    if (user == null) {
	        throw new RuntimeException("User not found");
	    }

	    if (!user.isEnabled()) {
	        throw new RuntimeException("Your account is blocked by admin");
	    }

	    Bid highestBid = bidRepository.findTopByAuctionOrderByAmountDesc(auction);
	    
	    double minBid = (highestBid != null) ? highestBid.getAmount() : auction.getStartPrice();
	    
	    if(amount <= minBid) {
	    	throw new RuntimeException("Bid must be higher than ₹"+minBid);
	    }

	    if (highestBid != null &&
	    	    highestBid.getBidder().getId() != user.getId()) {

	    	    walletService.unlockAmount(
	    	        highestBid.getBidder().getId(),
	    	        highestBid.getAmount()
	    	    );
	    	}


	   
	    boolean locked = walletService.lockAmount(user.getId(), amount);
	    if (!locked) {
	        throw new RuntimeException("Insufficient wallet balance or consecutive bidding.");
	    }

	    Bid bid = new Bid();
	    bid.setAuction(auction);
	    bid.setBidder(user);
	    bid.setAmount(amount);
	    bid.setBidTime(LocalDateTime.now());

	    Bid savedBid = bidRepository.save(bid);

	    messagingTemplate.convertAndSend(
	        "/topic/bids/" + auctionId,
	        savedBid
	    );

	    return savedBid;
	}


	@Override
	public List<Bid> getBidsByAuction(Long auctionId) {
		return bidRepository.findByAuctionId(auctionId);
	}

	@Override
	public List<Bid> getUserBids(Long userId) {
		return bidRepository.findByBidderIdOrderByBidTimeDesc(userId);
	}
}
