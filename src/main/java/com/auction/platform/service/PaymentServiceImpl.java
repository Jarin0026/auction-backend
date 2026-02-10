package com.auction.platform.service;

import java.time.LocalDateTime;
import java.util.List;

import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.auction.platform.dto.PaymentVerifyRequest;
import com.auction.platform.entity.Auction;
import com.auction.platform.entity.Bid;
import com.auction.platform.entity.Payment;
import com.auction.platform.entity.User;
import com.auction.platform.repository.PaymentRepository;
import com.auction.platform.repository.UserRepository;
import com.razorpay.Order;
import com.razorpay.RazorpayClient;
import com.razorpay.Utils;

import jakarta.transaction.Transactional;

@Service
@Transactional    //If debit fails → credit won’t happen → DB rolls back automatically.
public class PaymentServiceImpl implements PaymentService {

	@Autowired
	private WalletService walletService;
	
	@Autowired
	private PaymentRepository paymentRepository;
	
	@Autowired
	private UserRepository userRepository;
	
	@Value("${razorpay.key.id}")
	private String keyId;
	
	@Value("${razorpay.key.secret}")
	private String keySecret;
	
	@Override
	@Transactional
	public void settleAuctionPayment(Auction auction, Bid highestBid) {

	    User buyer = highestBid.getBidder();
	    User seller = auction.getSeller();
	    double amount = highestBid.getAmount();
	    
	    if (auction.getSeller() == null) {
	        throw new RuntimeException("Auction has no seller");
	    }


	    // convert locked → actual debit
	    walletService.settleLockedAmount(buyer.getId(), amount);

	    // credit seller
	    walletService.credit(seller.getId(), amount);

	    Payment payment = new Payment();
	    payment.setBuyer(buyer);
	    payment.setSeller(seller);
	    payment.setAuction(auction);
	    payment.setAmount(amount);
	    payment.setType("BID");
	    payment.setStatus("SUCCESS");
	    payment.setPaymentTime(LocalDateTime.now());

	    paymentRepository.save(payment);
	}



	@Override
	public String createOrder(double amount) {
		try {
			RazorpayClient client = new RazorpayClient(keyId, keySecret);
			
			JSONObject orderRequest = new JSONObject();
			orderRequest.put("amount", amount*100);   // in paise
			orderRequest.put("currency","INR");
			orderRequest.put("receipt", "wallet_topup");
			
			Order order = client.orders.create(orderRequest);
			
			return order.toString();
			
		} catch (Exception e) {
			throw new RuntimeException("Payment order Failed.");
		}
	}

	@Override
	public void verifyAndCredit(PaymentVerifyRequest request) {

	    try {
	        JSONObject options = new JSONObject();
	        options.put("razorpay_order_id", request.getRazorpayOrderId());
	        options.put("razorpay_payment_id", request.getRazorpayPaymentId());
	        options.put("razorpay_signature", request.getRazorpaySignature());

	        boolean isValid = Utils.verifyPaymentSignature(options, keySecret);

	        if (!isValid) {
	            throw new RuntimeException("Invalid payment signature");
	        }
	        
	       
	        // Signature verified — now credit wallet
	        walletService.credit(
	                request.getUserId(),
	                request.getAmount()
	        );
	        
	        Payment payment = new Payment();
	        payment.setAmount(request.getAmount());
	        payment.setType("TOPUP");
	        payment.setStatus("SUCCESS");
	        payment.setPaymentTime(LocalDateTime.now());

	        User user = userRepository.findById(request.getUserId()).orElseThrow();
	        payment.setBuyer(user);

	        paymentRepository.save(payment);


	    } catch (Exception e) {
	        throw new RuntimeException("Payment verification failed");
	    }
	}

	@Override
	public List<Payment> getSellerPayments(String email) {
		User seller = userRepository.findByEmail(email);
		return paymentRepository.findBySeller(seller);
	}

}
