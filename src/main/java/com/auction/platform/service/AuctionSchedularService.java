package com.auction.platform.service;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import com.auction.platform.entity.Auction;
import com.auction.platform.entity.Bid;
import com.auction.platform.repository.AuctionRepository;
import com.auction.platform.repository.BidRepository;

@Service
public class AuctionSchedularService {

    @Autowired
    private AuctionRepository auctionRepository;

    @Autowired
    private AuctionService auctionService;

    @Scheduled(fixedRate = 60000)
    public void closeExpiredAuctions() {

        System.out.println("⏱ Scheduler running...");
        System.out.println("Server Time: " + LocalDateTime.now());


        List<Auction> expiredAuctions =
                auctionRepository.findByStatusAndEndTimeBefore(
                        "ACTIVE", LocalDateTime.now()
                );

        for (Auction auction : expiredAuctions) {
            auctionService.settleAuction(auction); // 🔥 ONE LINE
        }
    }
}
