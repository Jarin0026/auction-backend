package com.auction.platform.controller;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.auction.platform.entity.Auction;
import com.auction.platform.service.AuctionService;

@RestController
@RequestMapping("/api/auctions")
public class AuctionController {

	@Autowired
	private AuctionService auctionService;

	@PostMapping("/create")
	public Auction createAuction(
	        @RequestParam String title,
	        @RequestParam String description,
	        @RequestParam double startPrice,
	        @RequestParam String startTime,
	        @RequestParam String endTime,
	        @RequestParam List<MultipartFile> images
	) throws Exception {

	    String email = SecurityContextHolder.getContext()
	                        .getAuthentication().getName();

	    List<String> imagePaths = new ArrayList<>();

	    for (MultipartFile image : images) {

	        String fileName = System.currentTimeMillis() + "_" + image.getOriginalFilename();
	        Path path = Paths.get("uploads/" + fileName);

	        Files.createDirectories(path.getParent());
	        Files.write(path, image.getBytes());

	        imagePaths.add("/uploads/" + fileName);
	    }

	    Auction auction = new Auction();
	    auction.setTitle(title);
	    auction.setDescription(description);
	    auction.setStartPrice(startPrice);
	    auction.setStartTime(LocalDateTime.parse(startTime));
	    auction.setEndTime(LocalDateTime.parse(endTime));
	    auction.setStatus("ACTIVE");

	    auction.setImageUrls(imagePaths);   

	    return auctionService.createAuctionWithSeller(auction, email);
	}


	@GetMapping("/all")
	public List<Auction> getAllAuctions() {
		return auctionService.getAllAuctions();
	}
	
	@GetMapping("/{id}")
	public Auction getAuctionById(@PathVariable Long id) {
	    return auctionService.getAuctionById(id);
	}
	
	 

}
