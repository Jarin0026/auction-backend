package com.auction.platform.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.auction.platform.entity.User;
import com.auction.platform.entity.Wallet;
import java.util.List;


public interface WalletRepository extends JpaRepository<Wallet, Long> {
	
	Wallet findByUser(User user);

}
