package com.auction.platform.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;

@Entity
@Table(name="wallets")
public class Wallet {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private long id;
	
	
	private double balance;
	
	private double lockBalance;
	
	@OneToOne
	@JoinColumn(name="user_id", unique = true)
	private User user;
	
	public Wallet () {}
	
	public Wallet (User user) {
		this.user=user;
		this.balance=0.0;
	}
	
	public double getAvailableBalance() {
		return balance - lockBalance;
	}

	public double getLockBalance() {
		return lockBalance;
	}

	public void setLockBalance(double lockBalance) {
		this.lockBalance = lockBalance;
	}

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public double getBalance() {
		return balance;
	}

	public void setBalance(double balance) {
		this.balance = balance;
	}

	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
	}
	
	
	
}
