package com.auction.platform.service;

import com.auction.platform.entity.Wallet;

public interface WalletService {
	
	void credit(long userId, double amount);
	boolean debit(long userId, double amount);
	double getBalance(long userId);
	boolean lockAmount(long userId, double amount);
	void unlockAmount(long userId, double amount);
	void settleLockedAmount(long userId, double amount);
	Wallet getWalletByUserId(long userId);
	public void releaseAllLockedAmount(long userId);


	
}
