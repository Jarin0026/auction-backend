package com.auction.platform.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.auction.platform.entity.Payment;
import com.auction.platform.entity.User;

public interface PaymentRepository extends JpaRepository<Payment, Long> {

	List<Payment> findByBuyerIdOrSellerIdOrderByPaymentTimeDesc(Long buyerId, Long sellerId);
	
	List<Payment> findBySeller(User seller);

}
