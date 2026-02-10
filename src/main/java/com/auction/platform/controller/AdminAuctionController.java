package com.auction.platform.controller;


import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.auction.platform.entity.Auction;
import com.auction.platform.service.AuctionService;

@RestController
@RequestMapping("/api/admin/auctions")
@PreAuthorize("hasRole('ADMIN')")
public class AdminAuctionController {

    @Autowired
    private AuctionService auctionService;

    @GetMapping
    public List<Auction> getAllAuctions() {
        return auctionService.getAllAuctions();
    }

    @PutMapping("/{id}/close")
    public void forceClose(@PathVariable long id) {
        auctionService.forceCloseAuction(id);
    }

    @DeleteMapping("/{id}")
    public void deleteAuction(@PathVariable long id) {
        auctionService.deleteAuction(id);
    }
}
