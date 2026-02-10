package com.auction.platform.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.auction.platform.entity.Wallet;
import com.auction.platform.service.WalletService;

@RestController
@RequestMapping("/api/wallet")
public class WalletController {
	
	@Autowired
	private WalletService walletService;
	
	@PostMapping("/credit/{userId}")
	public String creditWallet(@PathVariable long userId, @RequestParam double amount) {
		walletService.credit(userId, amount);
		return "Wallet credited successfully";
	}
	
	@PostMapping("/debit/{userId}")
	public String debitWallet(@PathVariable long userId,@RequestParam double amount) {
		walletService.debit(userId, amount);
		return "Wallet debit successfully";
	}
	
	@GetMapping("/balance/{userId}")
	public double getbalance(@PathVariable long userId) {
		return walletService.getBalance(userId);
	}
	
	@GetMapping("/summary/{userId}")
	public Wallet getWalletSummary(@PathVariable long userId) {
		return walletService.getWalletByUserId(userId);
	}
}
