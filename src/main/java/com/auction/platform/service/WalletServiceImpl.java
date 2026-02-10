package com.auction.platform.service;

import java.time.LocalDateTime;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.auction.platform.entity.Transaction;
import com.auction.platform.entity.User;
import com.auction.platform.entity.Wallet;
import com.auction.platform.repository.BidRepository;
import com.auction.platform.repository.TransactionRepository;
import com.auction.platform.repository.UserRepository;
import com.auction.platform.repository.WalletRepository;

import jakarta.transaction.Transactional;

@Service
public class WalletServiceImpl implements WalletService {

    private final BidRepository bidRepository;
	
	@Autowired
	private UserRepository userRepository;
	
	@Autowired
	private WalletRepository walletRepository;
	
	@Autowired
	private TransactionRepository transactionRepository;

    WalletServiceImpl(BidRepository bidRepository) {
        this.bidRepository = bidRepository;
    }
	
	public Wallet getWallet(User user) {
		Wallet wallet = walletRepository.findByUser(user);
		
		if(wallet == null) {
			wallet = new Wallet(user);
			walletRepository.save(wallet);
		}
		
		return wallet;
	}

	@Override
	public void credit(long userId, double amount) {
		
		User user = userRepository.findById(userId).orElseThrow(()-> new RuntimeException("User not found."));
		Wallet wallet = getWallet(user);

		wallet.setUser(user);
		wallet.setBalance(wallet.getBalance()+amount);
		
		walletRepository.save(wallet);
		
		Transaction tx=new Transaction(user, amount, "CREDIT", "TOPUP");
		
		transactionRepository.save(tx);
		
	}

	@Override
	public boolean debit(long userId, double amount) {

	    User user = userRepository.findById(userId)
	            .orElseThrow(() -> new RuntimeException("User not found"));

	    Wallet wallet = getWallet(user);

	    
	    if (wallet.getBalance() < amount) {
	        return false;   
	    }

	    wallet.setBalance(wallet.getBalance() - amount);
	    walletRepository.save(wallet);

	    Transaction tx = new Transaction(user, amount, "DEBIT", "AUCTION");
	    transactionRepository.save(tx);

	    return true; 
	}


	@Override
	public double getBalance(long userId) {
		User user = userRepository.findById(userId).orElseThrow(()->new RuntimeException("User not found."));
		Wallet wallet = getWallet(user);
		return wallet.getBalance();
	}

	@Override
	public boolean lockAmount(long userId, double amount) {
	    User user = userRepository.findById(userId).orElseThrow();
	    Wallet wallet = getWallet(user);

	    if (wallet.getAvailableBalance() < amount) return false;

	    wallet.setLockBalance(wallet.getLockBalance() + amount);
	    walletRepository.save(wallet);
	    return true;
	}


	@Override
	public void unlockAmount(long userId, double amount) {
	    Wallet wallet = getWallet(userRepository.findById(userId).orElseThrow());
	    
	    if (wallet.getLockBalance() < amount) {
	        throw new RuntimeException("Unlock amount exceeds locked balance");
	    }

	    
	    wallet.setLockBalance(wallet.getLockBalance() - amount);
	    walletRepository.save(wallet);
	}


	@Override
	@Transactional
	public void settleLockedAmount(long userId, double amount) {

	    User user = userRepository.findById(userId)
	            .orElseThrow(() -> new RuntimeException("User not found"));

	    Wallet wallet = getWallet(user);

	    if (wallet.getLockBalance() < amount) {
	        throw new RuntimeException("Insufficient locked balance");
	    }

	    // Winner pays
	    wallet.setLockBalance(wallet.getLockBalance() - amount);
	    wallet.setBalance(wallet.getBalance() - amount);

	    walletRepository.save(wallet);

	    Transaction tx = new Transaction(
	            user,
	            amount,
	            "DEBIT",
	            "AUCTION_PAYMENT"
	    );
	    transactionRepository.save(tx);
	}




	@Override
	public Wallet getWalletByUserId(long userId) {
	    User user = userRepository.findById(userId)
	        .orElseThrow(() -> new RuntimeException("User not found"));

	    return getWallet(user);
	}

	@Override
	@Transactional
	public void releaseAllLockedAmount(long userId) {

	    User user = userRepository.findById(userId)
	            .orElseThrow(() -> new RuntimeException("User not found"));

	    Wallet wallet = getWallet(user);

	    wallet.setLockBalance(0);
	    walletRepository.save(wallet);
	}


	
	
}
