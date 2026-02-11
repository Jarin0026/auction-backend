package com.auction.platform.service;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import com.auction.platform.entity.Auction;
import com.auction.platform.repository.AuctionRepository;

@Service
public class AuctionSchedularService {

    @Autowired
    private AuctionRepository auctionRepository;

    @Autowired
    private AuctionService auctionService;

    @Scheduled(fixedRate = 60000)
    public void closeExpiredAuctions() {

        LocalDateTime nowIST = LocalDateTime.now(ZoneId.of("Asia/Kolkata"));

        System.out.println("⏱ Scheduler running...");
        System.out.println("Current IST Time: " + nowIST);

        List<Auction> expiredAuctions =
                auctionRepository.findByStatusAndEndTimeBefore(
                        "ACTIVE",
                        nowIST
                );

        System.out.println("Expired Auctions Found: " + expiredAuctions.size());

        for (Auction auction : expiredAuctions) {
            auctionService.settleAuction(auction);
        }
    }
}
