package com.auction.platform.service;

import java.time.LocalDateTime;
import java.util.List;

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
public class AuctionServiceImpl implements AuctionService {

	@Autowired
	private AuctionRepository auctionRepository;

	@Autowired
	private UserRepository userRepository;

	@Autowired
	private BidRepository bidRepository;

	@Autowired
	private PaymentService paymentService;
	
	@Autowired
	private WalletService walletService;

	@Override
	public Auction createAuction(Auction auction) {

	    
	    if (auction.getEndTime().isBefore(auction.getStartTime()) ||
	        auction.getEndTime().isEqual(auction.getStartTime())) {

	        throw new RuntimeException(
	            "End time must be greater than start time"
	        );
	    }


	    if (auction.getEndTime().isBefore(LocalDateTime.now())) {
	        throw new RuntimeException(
	            "Auction end time cannot be in the past"
	        );
	    }

	    auction.setStatus("ACTIVE");

	    return auctionRepository.save(auction);
	}

	@Override
	public List<Auction> getAllAuctions() {

		List<Auction> auctions = auctionRepository.findAll();

		auctions.forEach(a -> {
			Bid bid = bidRepository.findTopByAuctionOrderByAmountDesc(a);
			if (bid != null) {
				a.setHighestBid(bid.getAmount());
			}
		});

		return auctions;
	}

	@Override
	public Auction createAuctionWithSeller(Auction auction, String email) {

		User seller = userRepository.findByEmail(email);

		auction.setSeller(seller);

		return auctionRepository.save(auction);
	}

	@Override
	public Auction getAuctionById(Long id) {
		return auctionRepository.findById(id).orElseThrow();
	}

	@Override
	@Transactional
	public void forceCloseAuction(long id) {

		Auction auction = auctionRepository.findById(id).orElseThrow(() -> new RuntimeException("Auction not found"));
		
		

		settleAuction(auction);
	}

	@Override
	public void deleteAuction(long id) {
		auctionRepository.deleteById(id);
	}

	@Override
	@Transactional
	public void settleAuction(Auction auction) {

	    if ("CLOSED".equals(auction.getStatus())) return;

	    Bid highestBid =
	            bidRepository.findTopByAuctionOrderByAmountDesc(auction);

	    if (highestBid == null) {
	        auction.setStatus("CLOSED");
	        auction.setEndTime(LocalDateTime.now());
	        auctionRepository.save(auction);
	        return;
	    }

	    User winner = highestBid.getBidder();
	    auction.setWinner(winner);

	    // 💰 Winner pays from locked amount
	    paymentService.settleAuctionPayment(auction, highestBid);

	    // 🔓 Unlock all other bidders
	    List<Bid> bids = bidRepository.findByAuctionId(auction.getId());
	    for (Bid bid : bids) {
	        long bidderId = bid.getBidder().getId();
	        if (bidderId != winner.getId()) {
	            walletService.releaseAllLockedAmount(bidderId);
	        }
	    }

	    auction.setStatus("CLOSED");
	    auction.setEndTime(LocalDateTime.now());
	    auctionRepository.save(auction);
	}


}
