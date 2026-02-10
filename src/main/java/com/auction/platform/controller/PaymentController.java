package com.auction.platform.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.auction.platform.dto.PaymentVerifyRequest;
import com.auction.platform.entity.Payment;
import com.auction.platform.repository.PaymentRepository;
import com.auction.platform.service.PaymentService;

@RestController
@RequestMapping("/api/payments")
public class PaymentController {
	
	@Autowired
	private PaymentService paymentService;

	@Autowired
	private PaymentRepository paymentRepository;
	
	@PostMapping("/create-order")
	public String createOString(@RequestParam double amount) {
		return paymentService.createOrder(amount);
	}
	
	@PostMapping("/verify")
	public String verifyPayment(@RequestBody PaymentVerifyRequest request) {
		paymentService.verifyAndCredit(request);
		return "Payment verified and wallet credited";
	}
	
	@GetMapping("/history/{userId}")
	public List<Payment> getHistory(@PathVariable long userId){
		return paymentRepository.findByBuyerIdOrSellerIdOrderByPaymentTimeDesc(userId, userId);
	}
	
	@GetMapping("/seller/earnings")
	public List<Payment> getSellerEarnings(){
		String email = SecurityContextHolder.getContext().getAuthentication().getName();
		
		return paymentService.getSellerPayments(email);
	}
	
}
